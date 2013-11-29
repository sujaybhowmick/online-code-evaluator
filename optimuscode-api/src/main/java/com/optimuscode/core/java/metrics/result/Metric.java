package com.optimuscode.core.java.metrics.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/29/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Metric {
    public static final String ERROR_TYPE = "Error";
    public static final String EXCEPTION_TYPE = "Exception";
    private String id;
    private String errorType;
    private String sourceName;
    private String message;


    public Metric(String id, String errorType, String sourceName,
                 String message){
        this.id = id;
        this.errorType = errorType;
        this.sourceName = sourceName;
        this.message = message;
    }

    public Metric(){
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public String getId(){
        return this.id;
    }

    public void setId(final String id){
        this.id = id;
    }

    public String getErrorType(){
        return this.errorType;
    }

    public void setErrorType(final String errorType){
        this.errorType = errorType;
    }


    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
