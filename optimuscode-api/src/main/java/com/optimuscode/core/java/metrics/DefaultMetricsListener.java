package com.optimuscode.core.java.metrics;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.optimuscode.core.java.metrics.result.Metric;
import com.optimuscode.core.java.metrics.result.MetricsResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/30/13
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultMetricsListener implements MetricsListener {

    private MetricsResult metricsResult;

    @Override
    public void notify(MetricsResult metricsResult) {
        this.metricsResult = metricsResult;
    }

    @Override
    public Map<String, Collection<Metric>> getGroupedMetrics() {
        List<Metric> metrics = Lists.newArrayList(metricsResult.getMetrics());
        Multimap<String, Metric> index =
                Multimaps.index(metrics, new Function<Metric, String>() {
                    @Override
                    public String apply(Metric metric) {
                        return metric.getSourceName();
                    }
                });
        return index.asMap();
    }
}
