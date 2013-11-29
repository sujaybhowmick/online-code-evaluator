package com.optimuscode.core.java.metrics;

import com.optimuscode.core.common.model.Project;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/14/13
 * Time: 5:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MetricsService {

    void runMetrics(Project project);

    MetricsListener getMetricsListener();

    void setMetricsListener(MetricsListener metricsListener);

}
