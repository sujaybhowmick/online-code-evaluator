package com.optimuscode.core.common.testrunner;

import com.optimuscode.core.java.testrunner.TestSuiteResultNode;

import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/30/13
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TestRunnerListener extends EventListener{

    void notify(TestSuiteResultNode testSuiteResultNode);

    String getSuitName();

    int getErrorCount();

    int getFailureCount();

    int getTestCaseCount();

    int getSuccessCount();

    double getRunTime();

}
