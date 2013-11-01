package com.optimuscode.core.common.model;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/14/13
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface MetricCheckListener {
    public void started(MetricCheckEvent event);

    public void process(MetricCheckEvent event);

    public void done(MetricCheckEvent event);
}
