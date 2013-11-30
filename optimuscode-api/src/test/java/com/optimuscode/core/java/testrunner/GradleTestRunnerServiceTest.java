/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.optimuscode.core.java.testrunner;

import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.testrunner.TestRunnerListener;
import com.optimuscode.core.java.compiler.JavaCompilerServiceImpl;
import com.optimuscode.core.java.model.JavaProject;
import com.optimuscode.core.utils.CommonUtils;
import com.optimuscode.core.utils.SupportedLanguage;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author sujay
 */
public class GradleTestRunnerServiceTest {
    Project project;

    public GradleTestRunnerServiceTest() {
    }
    
    @Before
    public void setUp() {
        project = JavaProject.create("session1", CommonUtils.generateUUID(),
                "/home/sbhowmick/tmp/");
    }
    
    @After
    public void tearDown() {
        project.close();
    }

    /**
     * Test of runTest method, of class GradleTestRunnerService.
     */
    @Test
    public void testRunTest() throws Exception{
        System.out.println("runTest");
        final String javaFileName1 = "./optimuscode-api/src/test/resources/TestSourceFile.txt";
        final String javaFileName2 = "./optimuscode-api/src/test/resources/TestSourceFileTest.txt";
        final String className1 = "TestSourceFile";
        final String className2 = "TestSourceFileTest";
        String fileContents1 = FileUtils.fileRead(javaFileName1);
        String fileContents2 = FileUtils.fileRead(javaFileName2);
        assertNotNull(fileContents1);
        CompilationUnit unit = new CompilationUnit(this, SupportedLanguage.Java);
        unit.addSource(className1, fileContents1);
        unit.addSource(className2, fileContents2);
        project.setTestClassName(className2);
        project.setUnit(unit);
        CompilerService compileService = JavaCompilerServiceImpl.create();
        compileService.compile(project);
        CompilationListener listener = compileService.getCompilationListener();
        assertFalse(listener.hasErrors());

        TestRunnerService runnerService = GradleTestRunnerService.create();
        runnerService.runTest(project);

        TestRunnerListener testRunnerListener =
                runnerService.getTestRunnerListener();

        assertNotNull(testRunnerListener);
        System.out.println("TestSuiteName:" +
                testRunnerListener.getSuitName());
        System.out.println("TestCaseCount:" +
                testRunnerListener.getTestCaseCount());
        System.out.println("SuccessCount:" +
                testRunnerListener.getSuccessCount());
        System.out.println("FailureCount:" +
                testRunnerListener.getFailureCount());
        System.out.println("ErrorCount:" +
                testRunnerListener.getErrorCount());
        System.out.println("RunTime:" +
                testRunnerListener.getRunTime());
       
    }

    @Test
    public void testRunTestExit() throws Exception{
        System.out.println("runTest");
        final String javaFileName1 = "../optimuscode-api/src/test/resources/TimeOutTaskFile.txt";
        final String javaFileName2 = "../optimuscode-api/src/test/resources/TimeOutTaskFileTest.txt";
        final String className1 = "TimeOutTaskFile";
        final String className2 = "TimeOutTaskFileTest";
        String fileContents1 = FileUtils.fileRead(javaFileName1);
        String fileContents2 = FileUtils.fileRead(javaFileName2);
        assertNotNull(fileContents1);
        CompilationUnit unit = new CompilationUnit(this, SupportedLanguage.Java);
        unit.addSource(className1, fileContents1);
        unit.addSource(className2, fileContents2);
        project.setTestClassName(className2);
        project.setUnit(unit);
        CompilerService compileService = JavaCompilerServiceImpl.create();
        compileService.compile(project);
        CompilationListener listener = compileService.getCompilationListener();
        assertFalse(listener.hasErrors());

        TestRunnerService runnerService = GradleTestRunnerService.create();
        runnerService.runTest(project);
        TestRunnerListener testRunnerListener =
                                    runnerService.getTestRunnerListener();

        assertNotNull(testRunnerListener);
        System.out.println("TestSuiteName:" +
                            testRunnerListener.getSuitName());
        System.out.println("TestCaseCount:" +
                            testRunnerListener.getTestCaseCount());
        System.out.println("SuccessCount:" +
                            testRunnerListener.getSuccessCount());
        System.out.println("FailureCount:" +
                            testRunnerListener.getFailureCount());
        System.out.println("ErrorCount:" +
                            testRunnerListener.getErrorCount());
        System.out.println("RunTime:" +
                            testRunnerListener.getRunTime());

    }
}