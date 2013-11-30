package com.optimuscode.core.java.compiler;

import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.java.model.JavaProject;
import com.optimuscode.core.utils.CommonUtils;
import com.optimuscode.core.utils.SupportedLanguage;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class JavaCompileTaskTest {
    Project project;
    @Before
    public void setUp() throws Exception {
        project = JavaProject.create("session1", CommonUtils.generateUUID(),
                                        "/home/sbhowmick/tmp/");
    }

    @After
    public void tearDown() throws Exception {
        project.close();

    }

    @Test
    public void testCompile() throws Exception {
        final String javaFileName1 = "../optimuscode-api/src/test/resources/TestSourceFile.txt";
        final String javaFileName2 = "../optimuscode-api/src/test/resources/TestSourceFileTest.txt";
        final String className1 = "TestSourceFile";
        final String className2 = "TestSourceFileTest";
        String fileContents1 = FileUtils.fileRead(javaFileName1);
        String fileContents2 = FileUtils.fileRead(javaFileName2);
        CompilationUnit unit = new CompilationUnit(this, SupportedLanguage.Java);
        unit.addSource(className1, fileContents1);
        unit.addSource(className2, fileContents2);
        project.setTestClassName(className2);
        project.setUnit(unit);
        JavaCompileTask compileTask = JavaCompileTask.create(project);
        Boolean result = compileTask.call();
        List<String> errors = unit.getErrors();
        for(String error: errors){
           System.out.println(error);
        }
        assertTrue(result);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testCompileFailure() throws Exception {
        final String javaFileName =
                "../optimuscode-api/src/test/resources/CompileErrorTestFile.txt";
        final String classFileName = "CompileErrorTestFile";
        String fileContents = FileUtils.fileRead(javaFileName);
        assertNotNull(fileContents);
        CompilationUnit unit = new CompilationUnit(this, SupportedLanguage.Java);
        unit.addSource(classFileName, fileContents);
        project.setTestClassName(classFileName);
        project.setUnit(unit);
        JavaCompileTask compileTask = JavaCompileTask.create(project);
        Boolean result = compileTask.call();
        List<String> errors = unit.getErrors();
        assertFalse(result);
        assertFalse(errors.isEmpty());

        for (String error: errors){
            System.out.println(error);
        }

    }
}
