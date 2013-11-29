package com.optimuscode.core.java.metrics;

import com.optimuscode.core.common.model.Project;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/14/13
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricsServiceImpl implements MetricsService{

    private MetricsListener metricsListener;

    @Override
    public void runMetrics(Project project) {

    }

    @Override
    public MetricsListener getMetricsListener() {
        return this.metricsListener;
    }

    @Override
    public void setMetricsListener(MetricsListener metricsListener) {
        this.metricsListener = metricsListener;
    }
}
