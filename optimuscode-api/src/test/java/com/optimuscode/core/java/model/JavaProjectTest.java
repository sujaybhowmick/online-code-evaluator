package com.optimuscode.core.java.model;

import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.java.compiler.JavaCompilerServiceImpl;
import com.optimuscode.core.java.metrics.MetricsService;
import com.optimuscode.core.java.testrunner.GradleTestRunnerService;
import com.optimuscode.core.java.testrunner.TestRunnerService;
import com.optimuscode.core.utils.CommonUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/14/13
 * Time: 9:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class JavaProjectTest {
    CompilerService compilerService;

    TestRunnerService testRunner;

    MetricsService metricsService;

    @Before
    public void setUp(){
        compilerService = JavaCompilerServiceImpl.create();
        testRunner = GradleTestRunnerService.create();

    }

    @Test
    public void testOpenClose() throws Exception {
        Project project = null;
        try {
            String javaFileName = "../optimuscode-api/src/test/resources/TestSourceFile.txt";
            String className = "TestSourceFile";
            String fileContents = FileUtils.fileRead(javaFileName);
            CompilationUnit unit = new CompilationUnit(this);
            unit.addSource(className, fileContents);
            project = JavaProject.create("session1",
                    CommonUtils.generateUUID(), compilerService, testRunner,
                    metricsService,"/home/sbhowmick/tmp/");
            project.setTestClassName(className);
            project.setUnit(unit);
            project.open();
        }catch(Exception e){
            e.printStackTrace();
            fail("project creation failed");
        }finally {
            if(project != null){
                project.close();
            }
        }



    }

    @Test
    public void testRunTests() throws Exception {

    }
}
