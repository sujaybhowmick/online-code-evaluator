package com.optimuscode.thrift.client;

import com.optimuscode.thrift.api.CompilerResult;
import com.optimuscode.thrift.api.Session;
import com.optimuscode.thrift.api.SourceUnit;
import com.optimuscode.thrift.api.TestResult;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/4/13
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Client {
    CompilerResult compile(Session session, SourceUnit sourceUnit);

    TestResult runTest(Session session, SourceUnit sourceUnit);

    String echo(final String msg);
}
