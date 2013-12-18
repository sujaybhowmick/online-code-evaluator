package com.optimuscode.core.cpp.compiler;

import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.CppProject;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.utils.CommonUtils;
import com.optimuscode.core.utils.SupportedLanguage;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 12/18/13
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class CppCompilerServiceImplTest {
    Project project;

    public CppCompilerServiceImplTest() {
    }

    @Before
    public void setUp() {
        project = CppProject.create("session1", CommonUtils.generateUUID(),
                "/home/sbhowmick/tmp/");
    }

    @After
    public void tearDown() {
        project.close();
    }

    @Test
    public void testCompile() throws Exception {
        final String cppFile = "./optimuscode-api/src/test/resources/hello.cpp";
        final String className = "hello";
        String fileContents = FileUtils.fileRead(cppFile);
        CompilationUnit unit = new CompilationUnit(SupportedLanguage.Cpp);
        unit.addSource(className, fileContents);
        project.setUnit(unit);
        project.setClassName(className);
        CompilerService runner = CppCompilerServiceImpl.create();
        runner.compile(project);
        CompilationListener listener = runner.getCompilationListener();
        assertTrue(listener.isSuccess());
        assertFalse(listener.hasErrors());

    }

    @Test
    public void testCompileFailure() throws Exception {
        final String cppFile = "./optimuscode-api/src/test/resources/error.cpp";
        final String className = "error";
        String fileContents = FileUtils.fileRead(cppFile);
        CompilationUnit unit = new CompilationUnit(SupportedLanguage.Cpp);
        unit.addSource(className, fileContents);
        project.setUnit(unit);
        project.setClassName(className);
        CompilerService runner = CppCompilerServiceImpl.create();
        runner.compile(project);
        CompilationListener listener = runner.getCompilationListener();
        assertFalse(listener.isSuccess());
    }
}
