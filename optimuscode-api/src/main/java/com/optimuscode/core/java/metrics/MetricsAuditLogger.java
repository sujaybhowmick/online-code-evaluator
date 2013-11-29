package com.optimuscode.core.java.metrics;

import com.optimuscode.core.java.metrics.result.Metric;
import com.optimuscode.core.java.metrics.result.MetricSerializer;
import com.optimuscode.core.java.metrics.result.MetricsResult;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/12/13
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricsAuditLogger extends AutomaticBean
                                            implements AuditListener{
    private File dir;
    private MetricsResult metricsResult;

    public MetricsAuditLogger(File dir){
        this.dir = dir;
    }

    @Override
    public void auditStarted(AuditEvent auditEvent) {
        this.metricsResult = new MetricsResult();
    }

    @Override
    public void auditFinished(AuditEvent auditEvent) {
        MetricSerializer serializer = new MetricSerializer(this.dir);
        serializer.write(this.metricsResult);
    }

    @Override
    public void fileStarted(AuditEvent auditEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fileFinished(AuditEvent auditEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addError(AuditEvent auditEvent) {
        Metric metric = new Metric(auditEvent.getModuleId(),
                            Metric.ERROR_TYPE,
                            auditEvent.getSourceName(),
                            auditEvent.getMessage());
        this.metricsResult.addMetric(metric);
    }

    @Override
    public void addException(AuditEvent auditEvent, Throwable throwable) {
        Metric metric = new Metric(auditEvent.getModuleId(),
                Metric.EXCEPTION_TYPE,
                auditEvent.getSourceName(),
                auditEvent.getMessage());
        this.metricsResult.addMetric(metric);
    }
}
