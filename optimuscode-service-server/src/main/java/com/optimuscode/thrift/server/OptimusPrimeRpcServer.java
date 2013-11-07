package com.optimuscode.thrift.server;

import com.optimuscode.thrift.api.RpcCompileNTestService;
import com.optimuscode.thrift.commons.ClusterFactory;
import com.twitter.finagle.builder.Server;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import com.twitter.ostrich.admin.AdminHttpService;
import com.twitter.ostrich.admin.RuntimeEnvironment;
import org.apache.thrift.protocol.TBinaryProtocol;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/6/13
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class OptimusPrimeRpcServer {

    private static final int SERVER_PORT = 9990;

    public static void main(String args[]){
        int port = SERVER_PORT;
        if(args.length > 0){
           port = Integer.parseInt(args[0]);
        }

        RpcCompileNTestServiceHandler handler =
                                   new RpcCompileNTestServiceHandler(
                                           "started handler on server:" + port);

        Server server = ServerBuilder.safeBuild(new
                RpcCompileNTestService.FinagledService(handler,
                new TBinaryProtocol.Factory()),
                ServerBuilder.get().
                name("RpcCompileNTestService").
                codec(ThriftServerFramedCodec.get()).
                maxConcurrentRequests(50).
                logger(Logger.getLogger("ROOT")).
                bindTo(new InetSocketAddress(port))
        );

        ClusterFactory.reportServerUpAndRunning(server,
                                                "RpcCompileNTestService");

        System.out.println("The server, running from port " + port +
                                " joined the RpcCompileNTestService cluster.");

        int ostrichPort = port + 1;
        RuntimeEnvironment runtime = new RuntimeEnvironment("");
        AdminHttpService admin = new AdminHttpService(ostrichPort, 0, runtime);
        admin.start();
        System.out.println("Ostrich reporting started on port " + ostrichPort);

    }
}
