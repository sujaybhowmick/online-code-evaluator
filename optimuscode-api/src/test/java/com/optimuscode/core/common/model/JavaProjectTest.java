package com.optimuscode.core.common.model;

import com.optimuscode.core.utils.CommonUtils;
import com.optimuscode.core.utils.SupportedLanguage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 12/16/13
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class JavaProjectTest {
    Project project;

    @Before
    public void setUp() {
        project = JavaProject.create("session1", CommonUtils.generateUUID(),
                "/home/sbhowmick/tmp/");

    }

    @After
    public void tearDown() {
        project.close();
    }

    @Test
    public void testCreateProjectFolder() throws Exception {
        CompilationUnit unit = new CompilationUnit(SupportedLanguage.Java);
        project.setUnit(unit);
        assertFalse(project.exists());
        project.open();


    }
}
