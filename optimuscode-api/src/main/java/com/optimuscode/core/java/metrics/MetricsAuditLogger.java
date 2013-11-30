package com.optimuscode.core.java.metrics;

import com.optimuscode.core.java.metrics.result.Metric;
import com.optimuscode.core.java.metrics.result.MetricsSerializer;
import com.optimuscode.core.java.metrics.result.MetricsResult;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/12/13
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetricsAuditLogger extends AutomaticBean
                                            implements AuditListener{
    protected static Logger log = LoggerFactory.getLogger(MetricsAuditLogger.class);
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
        MetricsSerializer serializer = new MetricsSerializer(this.dir);
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
        String source = auditEvent.getSourceName();
        int start = source.lastIndexOf('.') + 1;
        int end = source.length();
        source = source.substring(start, end);

        Metric metric = new Metric(UUID.randomUUID().toString(),
                            Metric.ERROR_TYPE,
                            source,
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
