package com.optimuscode.core.common.model;

import com.optimuscode.core.utils.CommonUtils;
import com.optimuscode.core.utils.SupportedLanguage;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 12/17/13
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class CppProjectTest {
    Project project;

    @Before
    public void setUp() throws Exception {
        project = CppProject.create("session1", CommonUtils.generateUUID(),
                "/home/sbhowmick/tmp/");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCppProject() throws Exception {
        final String cppFile = "./optimuscode-api/src/test/resources/hello.cpp";
        final String className = "hello";
        String fileContents = FileUtils.fileRead(cppFile);
        CompilationUnit unit = new CompilationUnit(SupportedLanguage.Cpp);
        unit.addSource(className, fileContents);
        project.setUnit(unit);
        assertFalse(project.exists());
        project.open();
        assertTrue(project.exists());
        project.dumpSource();
        project.close();
        assertFalse(project.exists());

    }
}
