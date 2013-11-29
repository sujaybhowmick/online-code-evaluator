package com.optimuscode.core.java.metrics.result;

import org.gradle.api.Action;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/29/13
 * Time: 10:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricSerializerTest {

    static final String METRICS_FOLDER = "./optimuscode-api/src/test/resources";

    MetricsResult metricsResult;

    MetricSerializer serializer;

    @Before
    public void setUp(){
        this.metricsResult = new MetricsResult();
        this.serializer = new MetricSerializer(new File(METRICS_FOLDER));

    }

    @After
    public void tearDown(){

    }

    @Test
    public void testWrite() throws Exception {
        Metric metric1 = new Metric("5436", Metric.ERROR_TYPE,
                                "CyclomaticComplexity", "Message 1");
        this.metricsResult.addMetric(metric1);

        Metric metric2 = new Metric("1234", Metric.EXCEPTION_TYPE,
                                 "NPathComplexity", "Message 2");
        this.metricsResult.addMetric(metric2);

        serializer.write(metricsResult);

        serializer.read(new Action<MetricsResult>() {
            @Override
            public void execute(MetricsResult metricResult) {
                List<Metric> metrics = metricResult.getMetrics();
                assertEquals(2, metrics.size());
                int i = 1;
                for(Metric metric: metrics){
                    assertEquals("Message " + i, metric.getMessage());
                    System.out.println(metric.getId());
                    System.out.println(metric.getErrorType());
                    System.out.println(metric.getMessage());
                    i++;
                }
            }
        });
    }
}
