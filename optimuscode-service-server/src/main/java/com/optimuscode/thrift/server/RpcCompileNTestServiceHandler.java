package com.optimuscode.thrift.server;

import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.common.testrunner.TestRunnerListener;
import com.optimuscode.core.java.compiler.JavaCompilerServiceImpl;
import com.optimuscode.core.java.model.JavaProject;
import com.optimuscode.core.java.testrunner.GradleTestRunnerService;
import com.optimuscode.core.java.testrunner.TestRunnerService;
import com.optimuscode.thrift.api.*;
import com.optimuscode.thrift.commons.Configuration;
import com.optimuscode.thrift.commons.ConfigurationManager;
import com.twitter.util.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/6/13
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class RpcCompileNTestServiceHandler implements
                                    RpcCompileNTestService.FutureIface {
    protected Logger log =
                LoggerFactory.getLogger(RpcCompileNTestServiceHandler.class);

    private String msg;

    private ConfigurationManager mgr = ConfigurationManager.getInstance();

    public RpcCompileNTestServiceHandler(final String msg){
        this.msg = msg;
    }

    @Override
    public Future<String> echo(String msg) {
        if(msg != null && msg.length() > 0){
            log.info("echoing incoming message..." + msg);
            return Future.value("echoed message:" + msg);
        }
        return Future.value("INFO: null or 0 length message");
    }

    @Override
    public Future<CompilerResult> compile(Session session, SourceUnit unit) {
        Configuration config = mgr.getConfig("server-config");
        log.info("Compiling code for session - " + session.getUuid());
        Project project = JavaProject.create(session.getUuid(),
                                            session.getUuid(),
                                            config.getBasefolder());
        CompilerService runner = JavaCompilerServiceImpl.create();
        CompilationUnit compilationUnit = new CompilationUnit(this);
        compilationUnit.addSource(unit.getClassName(), unit.getSourceCode());
        compilationUnit.addSource(unit.getTestClassName(),
                                                    unit.getTestSourceCode());
        project.setTestClassName(unit.getTestClassName());
        project.setUnit(compilationUnit);
        runner.compile(project);
        CompilationListener listener = runner.getCompilationListener();
        CompilerResult compilerResult = new CompilerResult.Builder().
                                        passed(listener.isSuccess()).
                                        errors(listener.getErrors()).build();

        return Future.value(compilerResult);
    }

    @Override
    public Future<TestResult> runTest(Session session, SourceUnit unit) {
        Configuration config = mgr.getConfig("server-config");
        log.info("Running Tests for session - " + session.getUuid());
        Project project = JavaProject.create(session.getUuid(),
                session.getUuid(), config.getBasefolder());
        CompilationUnit compilationUnit = new CompilationUnit(this);
        compilationUnit.addSource(unit.getClassName(), unit.getSourceCode());
        compilationUnit.addSource(unit.getTestClassName(),
                                                   unit.getTestSourceCode());
        project.setTestClassName(unit.getTestClassName());
        project.setUnit(compilationUnit);

        TestRunnerService runnerService = GradleTestRunnerService.create();

        try{
            runnerService.runTest(project);
        }catch(Exception e){
            log.info("build exception, can be due to failed tests", e);
        }

        TestRunnerListener listener =
                runnerService.getTestRunnerListener();
        TestResult testResult = new TestResult.Builder().
                                testCaseCount(listener.getTestCaseCount()).
                                errorCount(listener.getErrorCount()).
                                failureCount(listener.getFailureCount()).
                                successCount(listener.getSuccessCount()).
                                runTime(listener.getRunTime()).build();
        return Future.value(testResult);
    }
}
