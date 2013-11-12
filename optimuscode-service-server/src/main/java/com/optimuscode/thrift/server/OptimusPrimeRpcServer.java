package com.optimuscode.thrift.server;

import com.optimuscode.thrift.api.RpcCompileNTestService;
import com.optimuscode.thrift.commons.ClusterFactory;
import com.optimuscode.thrift.commons.Configuration;
import com.optimuscode.thrift.commons.ConfigurationManager;
import com.twitter.finagle.builder.Server;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import com.twitter.ostrich.admin.AdminHttpService;
import com.twitter.ostrich.admin.RuntimeEnvironment;
import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/6/13
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class OptimusPrimeRpcServer {
   private static org.slf4j.Logger log =
                        LoggerFactory.getLogger(OptimusPrimeRpcServer.class);

    private static ConfigurationManager confMan =
                                            ConfigurationManager.getInstance();

    static Map<String, String> options = new HashMap<String, String>(0);

    public static void main(String args[]) throws Exception{

        parseOptions("OptimusPrimeRpcServer start", args);
        if(options.isEmpty()){
            printHelp();
            System.exit(0);
        }
        if(options.size() < 3){
            printHelp();
            System.exit(0);
        }

        String env = options.get("env");
        String config = options.get("config");
        int port = Integer.parseInt(options.get("port"));
        String[] environment = {env};

        Configuration conf = confMan.register("server-config", config, env);
        log.info("base folder - " + conf.getBasefolder());
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

        log.info("The server, running from port " + port +
                                " joined the RpcCompileNTestService cluster.");

        int ostrichPort = port + 1;
        RuntimeEnvironment runtime = new RuntimeEnvironment("");
        AdminHttpService admin = new AdminHttpService(ostrichPort, 0, runtime);
        admin.start();
        log.info("Ostrich reporting started on port " + ostrichPort);

    }

    static void parseOptions(String cmd, String[] args) {
        int c;
        String arg;
        LongOpt[] longOpts = new LongOpt[3];

        longOpts[0] = new LongOpt("env",
                                   LongOpt.REQUIRED_ARGUMENT, null, 'e');

        longOpts[1] = new LongOpt("config",
                                   LongOpt.REQUIRED_ARGUMENT, null, 'c');

        longOpts[2] = new LongOpt("port",
                                   LongOpt.REQUIRED_ARGUMENT, null, 'p');

        Getopt g = new Getopt("OptimusPrimeServer", args, "e:c:p", longOpts);

        g.setOpterr(true);

        while ((c = g.getopt()) != -1) {
            switch (c) {
                case 'e':
                    arg = g.getOptarg();
                    if(!isvalidEnv(arg)){
                        printHelp();
                        System.exit(0);
                    }
                    options.put("env", arg);
                    break;

                case 'c':
                    arg = g.getOptarg();
                    options.put("config", arg);
                    break;

                case 'p':
                    arg = g.getOptarg();
                    try{
                        Integer.parseInt(arg);
                    }catch(NumberFormatException e){
                        printHelp();
                        System.exit(1);
                    }
                    options.put("port", arg);
                    break;

                default:
                    printHelp();
                    System.exit(0);
                    break;
            }

        }
    }

    static void printHelp(){
        System.out.println(
             "USEAGE:com.optimuscode.thrift.server.OptimusPrimeServer" +
             "[--env=<dev/test/prod>][--config=<dev-config.yml>][--port=<9900>]"
        );
    }
    static boolean isvalidEnv(final String arg){
       return (arg != null &&
                            Arrays.asList("dev", "test", "prod").contains(arg));

    }
}
