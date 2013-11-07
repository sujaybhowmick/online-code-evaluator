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
import com.twitter.util.Future;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/6/13
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class RpcCompileNTestServiceHandler implements
                                    RpcCompileNTestService.FutureIface {
    private String msg;
    public RpcCompileNTestServiceHandler(final String msg){
        this.msg = msg;
    }
    @Override
    public Future<CompilerResult> compile(Session session, SourceUnit unit) {
        Project project = JavaProject.create(session.getUuid(),
                                            session.getUuid(),
                                            "/home/sbhowmick/tmp/");
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
        System.out.println("Inside run test");
        Project project = JavaProject.create(session.getUuid(),
                session.getUuid(),
                "/home/sbhowmick/tmp/");
        CompilationUnit compilationUnit = new CompilationUnit(this);
        compilationUnit.addSource(unit.getClassName(), unit.getSourceCode());
        compilationUnit.addSource(unit.getTestClassName(),
                                                   unit.getTestSourceCode());
        project.setTestClassName(unit.getTestClassName());
        TestRunnerService runnerService = GradleTestRunnerService.create();
        project.setUnit(compilationUnit);

        try{
            runnerService.runTest(project);
        }catch(Exception e){
            e.printStackTrace();
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
