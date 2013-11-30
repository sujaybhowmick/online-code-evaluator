package com.optimuscode.core.java.metrics.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/30/13
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class MetricsResult {
    private int numErrors;
    private List<Metric> metrics;
    private List<String> availableChecks;


    public MetricsResult(){
        this.metrics = new ArrayList<Metric>();
        this.availableChecks = new ArrayList<String>();
    }

    public List<Metric> getMetrics(){
        return this.metrics;
    }

    public void setMetrics(List<Metric> metrics){
        this.metrics = metrics;
    }

    public void addMetric(Metric metric){
        this.metrics.add(metric);
    }

    public void setNumErrors(int numErrors){
        this.numErrors = numErrors;
    }

    public int getNumErrors(){
        return this.numErrors;
    }

    public List<String> getAvailableChecks(){
        return this.availableChecks;
    }

    public void addAvailableCheck(final String check){
        this.availableChecks.add(check);
    }

}
