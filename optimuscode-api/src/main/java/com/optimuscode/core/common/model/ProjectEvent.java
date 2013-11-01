package com.optimuscode.core.common.model;

import java.util.EventObject;

public class ProjectEvent extends EventObject{

    private long projectStart;

    private long projectStop;

    public ProjectEvent(Object source){
        super(source);
    }

    public void start(){
        projectStart = System.currentTimeMillis();
    }

    public void stop(){
        projectStop = System.currentTimeMillis();
    }

}
