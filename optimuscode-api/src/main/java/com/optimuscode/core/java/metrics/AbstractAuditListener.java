package com.optimuscode.core.java.metrics;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/12/13
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAuditListener implements AuditListener{
    private List<String> auditMessages;
    @Override
    public void auditStarted(AuditEvent auditEvent) {
       // auditMessages = Collections.synchronizedList(
       //         new ArrayList<String>());
    }

    @Override
    public void auditFinished(AuditEvent auditEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fileStarted(AuditEvent auditEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void fileFinished(AuditEvent auditEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
