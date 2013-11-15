package com.optimuscode.thrift.client;

import com.optimuscode.thrift.api.*;
import com.optimuscode.thrift.commons.ClusterFactory;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.builder.Cluster;
import com.twitter.finagle.service.RetryPolicy;
import com.twitter.finagle.service.SimpleRetryPolicy;
import com.twitter.finagle.stats.InMemoryStatsReceiver;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.util.Duration;
import com.twitter.util.Try;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.slf4j.LoggerFactory;
import scala.runtime.Nothing$;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
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
    protected org.slf4j.Logger log = LoggerFactory.getLogger(OptimusPrimeRpcClient.class);

    protected Service<ThriftClientRequest, byte[]>  service;
    protected RpcCompileNTestService.FinagledClient client;

    public OptimusPrimeRpcClient(){
    }


    protected synchronized void openConnection(){
        log.info("Opening remote connection");
        Cluster cluster = ClusterFactory.getForService("RpcCompileNTestService");

        // Querying for a list of online servers is not necessary for Finagle,
        // but can be used for vanilla thrift servers.
        List<SocketAddress> onlineServers =
                ClusterFactory.getOnlineServers("RpcCompileNTestService");
        log.info("Online servers: " + onlineServers.toString());

        RetryPolicy<Try<Nothing$>> retryPolicy =
                                    new SimpleRetryPolicy<Try<Nothing$>>() {

            @Override
            public Duration backoffAt(int retry) {
                if (retry > 3) {
                    return Duration.fromTimeUnit(16, TimeUnit.SECONDS);
                } else {
                    return Duration.fromTimeUnit((int) Math.pow(2.0, retry),
                                                            TimeUnit.SECONDS);
                }
            }

            @Override
            public boolean shouldRetry(Try<Nothing$> arg0) {
                log.info("Retrying connection...");
                return true;
            }
        };

        Service<ThriftClientRequest, byte[]> service =
            ClientBuilder.safeBuild(ClientBuilder.get()
                    .cluster(cluster)// this is where service discovery happens
                    //.hosts(new InetSocketAddress("162.243.58.79", 9990))
                    .name("RpcCompileNTestService client")
                    .codec(ThriftClientFramedCodec.get())
                    .retryPolicy(retryPolicy)
                    .hostConnectionLimit(1)
                    .tcpConnectTimeout(Duration.apply(1, TimeUnit.MINUTES))
                    .logger(Logger.getLogger("ROOT"))
            );

        this.service = service;
        this.client = new RpcCompileNTestService.FinagledClient(
                service,
                new TBinaryProtocol.Factory(),
                "RpcCompileNTestService",
                new InMemoryStatsReceiver()
        );

    }

    @Override
    public CompilerResult compile(Session session, SourceUnit sourceUnit) {
        CompilerResult result;
        openConnection();
        try {
            result = this.client.compile(session, sourceUnit).toJavaFuture().get();
        } catch (Exception e) {
            log.info("Compilation failed for session with session id " +
                    session.getUuid(), e);
            result = new CompilerResult(false, Collections.EMPTY_LIST);
        }finally{
            closeConnection();
        }
        return result;
    }

    @Override
    public TestResult runTest(Session session, SourceUnit sourceUnit) {
        TestResult result;
        openConnection();
        try {
            result = this.client.runTest(session, sourceUnit).toJavaFuture().get();
        } catch (Exception e) {
            log.info("Exception in running test for session with session id " +
                                        session.getUuid(), e);
            result = new TestResult(0, 0, 0, 0, 0.0,
                                Collections.EMPTY_LIST);
        }finally{
            closeConnection();
        }
        return result;
    }

    @Override
    public String echo(String msg) {
        String echoedMsg = null;
        openConnection();
        try {
            echoedMsg = this.client.echo(msg).toJavaFuture().get();
        } catch (Exception e) {
            log.info("error processing echo", e);
        }finally {
            closeConnection();
        }
        return echoedMsg;
    }

    protected void closeConnection() {
        log.info("Closing remote connection");
        if(this.service != null){
            this.service.close();
        }

    }
}
