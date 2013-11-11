package com.optimuscode.thrift.commons;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/11/13
 * Time: 7:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationManager {
    private static ConfigurationManager SINGLETON = null;

    private Map<String, Configuration> registry;

    private ConfigurationManager(){
    }

    public static synchronized ConfigurationManager getInstance(){
        if (SINGLETON == null){
            SINGLETON = new ConfigurationManager();
        }

        return SINGLETON;
    }

    public synchronized Configuration register(final String ctx,
                                      final String configFile,
                                      final String... env)
                                                            throws Exception{
        registry =
              Collections.synchronizedMap(new HashMap<String, Configuration>());
        this.registry = new HashMap<String, Configuration>();
        Configuration config;
        if(env == null){
            config = load(configFile, "default");
        }else{
            config = load(configFile, env[0]);
        }
        this.registry.put(ctx, config);
        return config;
    }

    public void deRegister(final String ctx){
        if(registry != null && registry.containsKey(ctx)){
            registry.remove(ctx);
        }
    }

    private Configuration load(final String configFile,
                               final String env)
            throws FileNotFoundException {
        //InputStream in = getClass().getResourceAsStream(env + "-" + configFile);
        InputStream in = getClass().getClassLoader().
                            getResourceAsStream("conf" + File.separatorChar +
                                    env + "-" + configFile);
        return new Yaml().loadAs(in,
                            Configuration.class);
    }
}
