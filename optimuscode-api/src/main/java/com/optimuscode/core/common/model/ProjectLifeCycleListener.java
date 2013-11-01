package com.optimuscode.core.common.model;

import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/14/13
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectLifeCycleListener extends EventListener {
    void startProject(ProjectEvent event);

    void finishedProject(ProjectEvent event);

    void startMetrics(MetricsEvent event);

    void finishedMetrics(MetricsEvent event);
}
