package com.optimuscode.thrift.commons;

import com.google.common.collect.ImmutableSet;
import com.twitter.common.net.pool.DynamicHostSet;
import com.twitter.common.quantity.Amount;
import com.twitter.common.quantity.Time;
import com.twitter.common.zookeeper.ServerSet;
import com.twitter.common.zookeeper.ServerSetImpl;
import com.twitter.common.zookeeper.ZooKeeperClient;
import com.twitter.finagle.builder.Server;
import com.twitter.finagle.zookeeper.ZookeeperServerSetCluster;
import com.twitter.thrift.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import scala.collection.JavaConversions;
import scala.collection.immutable.HashMap;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/29/13
 * Time: 8:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterFactory {
    private static Logger log = LoggerFactory.getLogger(ClusterFactory.class);

    private static final String CONFIG_KEY = "server-config";

    private static ConfigurationManager mgr = ConfigurationManager.getInstance();

    static Amount<Integer,Time> sessionTimeout = Amount.of(2, Time.MINUTES);

    static List<InetSocketAddress> nodes = new ArrayList<InetSocketAddress>();
            //Arrays.asList(
            // Use a cluster of nodes...
            // new InetSocketAddress("zk1.myapp.com", 2181),
            // new InetSocketAddress("zk2.myapp.com", 2181),
            // new InetSocketAddress("zk3.myapp.com", 2181),
            // new InetSocketAddress("zk4.myapp.com", 2181),
            // new InetSocketAddress("zk5.myapp.com", 2181),

            // ...or use local ZooKeeper node
            //new InetSocketAddress("162.243.58.79", 2181)
    //);

    public static ZookeeperServerSetCluster getForService(String clusterName) {

        Configuration conf = mgr.getConfig(CONFIG_KEY);

        addNodes(conf.getHosts());

        ServerSet serverSet = new ServerSetImpl(getZooKeeperClient(),
                                                        getPath(clusterName));
        return new ZookeeperServerSetCluster(serverSet);
    }

    public static void reportServerUpAndRunning(Server server,
                                                        String clusterName) {
        log.info("adding server to cluster:" + server.localAddress().toString());
        getForService(clusterName).join(server.localAddress(),
                            new HashMap<String, InetSocketAddress>());
    }

    public static List<SocketAddress> getOnlineServers(String clusterName) {
        try {
            ZookeeperServerSetCluster cluster = getForService(clusterName);
            // Run the monitor() method, which will block the thread until
            // the initial list of servers arrives.
            new ServerSetImpl(zooKeeperClient, getPath(clusterName)).monitor(
                    new DynamicHostSet.HostChangeMonitor<ServiceInstance>(){
                public void onChange(ImmutableSet<ServiceInstance>
                                                            serviceInstances) {
                    // do nothing
                }
            });
            return JavaConversions.asJavaList(cluster.snap()._1());
            //return JavaConversions.as(cluster.snap());
        } catch (DynamicHostSet.MonitorException e) {
            throw new RuntimeException("Couldn't get list of online servers", e);
        }
    }

    private static String getPath(String clusterName) {
        return "/optimus/services/"+clusterName;
    }

    private static ZooKeeperClient zooKeeperClient;

    private static ZooKeeperClient getZooKeeperClient() {
        if (zooKeeperClient == null) {
            zooKeeperClient = new ZooKeeperClient(sessionTimeout, nodes);
        }
        return zooKeeperClient;
    }

    private static void addNodes(final List<String> hosts){

        for(String host: hosts){
            log.info("adding host -> " + host);
            InetSocketAddress address = new InetSocketAddress(host, 2181);
            if(!nodes.contains(address)){
                nodes.add(new InetSocketAddress(host, 2181));
            }

        }
    }
}
