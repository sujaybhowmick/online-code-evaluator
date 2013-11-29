package com.optimuscode.core.java.testrunner.result;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/29/13
 * Time: 9:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestFailure {
    private final String message;
    private final String stackTrace;
    private final String exceptionType;

    public TestFailure(String message, String stackTrace, String exceptionType) {
        this.message = message;
        this.stackTrace = stackTrace;
        this.exceptionType = exceptionType;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getExceptionType() {
        return exceptionType;
    }
}
