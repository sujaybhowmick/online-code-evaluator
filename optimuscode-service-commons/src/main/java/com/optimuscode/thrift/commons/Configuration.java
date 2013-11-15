package com.optimuscode.thrift.commons;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/9/13
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {

    private String basefolder;
    private List<String> hosts;


    public Configuration(){
    }


    public String getBasefolder(){
        return basefolder;
    }

    public void setBasefolder(String basefolder){
        this.basefolder = basefolder;
    }


    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }
}
