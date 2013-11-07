package com.optimuscode.thrift.client;

import com.optimuscode.thrift.api.*;
import com.optimuscode.thrift.commons.ClusterFactory;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.builder.Cluster;
import com.twitter.finagle.stats.InMemoryStatsReceiver;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Duration;
import com.twitter.util.Future;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.slf4j.LoggerFactory;
import scala.runtime.BoxedUnit;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/4/13
 * Time: 9:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class OptimusPrimeRpcClient implements Client{
    org.slf4j.Logger log = LoggerFactory.getLogger(OptimusPrimeRpcClient.class);

    private RpcCompileNTestService.FinagledClient client;

    public OptimusPrimeRpcClient(){
    }

    public synchronized Service<ThriftClientRequest, byte[]> getService(){
        Cluster cluster = ClusterFactory.getForService("RpcCompileNTestService");

        // Querying for a list of online servers is not necessary for Finagle,
        // but can be used for vanilla thrift servers.
        List<SocketAddress> onlineServers = ClusterFactory.getOnlineServers("RpcCompileNTestService");
        System.out.println("Online servers: "+onlineServers.toString());

        Service<ThriftClientRequest, byte[]> service =
            ClientBuilder.safeBuild(ClientBuilder.get()
                    .cluster(cluster)// this is where service discovery happens
                    .name("RpcCompileNTestService client")
                    .codec(ThriftClientFramedCodec.get())
                    .timeout(Duration.apply(2, TimeUnit.MINUTES))
                    .retries(4)
                    .hostConnectionLimit(1)
                    .logger(Logger.getLogger("ROOT"))
            );
        return service;

    }

    public synchronized RpcCompileNTestService.FinagledClient getClient(){
        return new RpcCompileNTestService.FinagledClient(
                getService(),
                new TBinaryProtocol.Factory(),
                "RpcCompileNTestService",
                new InMemoryStatsReceiver()
        );

    }

    @Override
    public CompilerResult compile(Session session, SourceUnit sourceUnit) {
        CompilerResult result;
        try {
            result = getClient().compile(session, sourceUnit).toJavaFuture().get();
        } catch (Exception e) {
            log.info("Compilation failed for session with session id " +
                    session.getUuid(), e);
            result = new CompilerResult(false, Collections.EMPTY_LIST);
        }
        return result;
    }

    @Override
    public TestResult runTest(Session session, SourceUnit sourceUnit) {
        TestResult result;
        try {
            result = getClient().runTest(session, sourceUnit).toJavaFuture().get();
        } catch (Exception e) {
            log.info("Exception in running test for session with session id " +
                                        session.getUuid(), e);
            result = new TestResult(0, 0, 0, 0, 0.0,
                                Collections.EMPTY_LIST);
        }
        return result;
    }
}
