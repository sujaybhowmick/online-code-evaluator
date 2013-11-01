package com.optimuscode.core.java.testrunner;

import com.optimuscode.core.common.testrunner.TestRunnerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultTestRunnerListener implements TestRunnerListener{
    private Logger log = LoggerFactory.getLogger(
                                    DefaultTestRunnerListener.class);
    private TestSuiteResultNode testSuiteResultNode;

    @Override
    public void notify(TestSuiteResultNode testSuiteResultNode) {
        log.info("Notified of compilation");
        this.testSuiteResultNode = testSuiteResultNode;
    }

    @Override
    public String getSuitName() {
        return this.testSuiteResultNode.getSuitName();
    }

    @Override
    public int getErrorCount() {
        return this.testSuiteResultNode.getErrorCount();
    }

    @Override
    public int getFailureCount() {
        return this.testSuiteResultNode.getFailureCount();
    }

    @Override
    public int getTestCaseCount() {
        return this.testSuiteResultNode.getTestCaseCount();
    }

    @Override
    public int getSuccessCount() {
        return this.testSuiteResultNode.getSuccessCount();
    }

    @Override
    public double getRunTime() {
        return this.testSuiteResultNode.getRunTime();
    }
}
