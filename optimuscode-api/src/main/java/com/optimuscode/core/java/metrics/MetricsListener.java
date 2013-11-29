package com.optimuscode.core.java.metrics;

import com.optimuscode.core.java.metrics.result.Metric;
import com.optimuscode.core.java.metrics.result.MetricsResult;

import java.util.Collection;
import java.util.EventListener;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/13/13
 * Time: 10:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MetricsListener extends EventListener {
    void notify(MetricsResult metricsResult);

    Map<String, Collection<Metric>> getGroupedMetrics();

}
