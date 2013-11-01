package com.optimuscode.core.java.testrunner;

import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.common.testrunner.TestRunnerListener;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 9/12/13
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TestRunnerService {
    void runTest(Project project);

    TestRunnerListener getTestRunnerListener();
}
