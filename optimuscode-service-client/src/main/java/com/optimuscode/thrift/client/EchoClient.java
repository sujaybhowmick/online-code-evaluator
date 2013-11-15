package com.optimuscode.thrift.client;

import com.optimuscode.thrift.commons.Configuration;
import com.optimuscode.thrift.commons.ConfigurationManager;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/14/13
 * Time: 5:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class EchoClient {
    public static void main(String args[]) throws Exception{
        ConfigurationManager mgr = ConfigurationManager.getInstance();
        Configuration conf = mgr.register("server-config", "config.yml", "dev");
        System.out.println("list of zk hosts :" + conf.getHosts());
        Client client = new OptimusPrimeRpcClient();
        String echoedMsg = client.echo("Hello Optimus Prime !!!");
        System.out.println(echoedMsg);
    }
}
