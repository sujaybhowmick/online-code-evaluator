/**
 * Copyright 2013 #name#
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimuscode.core.java.compiler;

import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.java.metrics.MetricsServiceImpl;
import com.optimuscode.core.java.model.JavaProject;
import com.optimuscode.core.java.testrunner.GradleTestRunnerService;
import com.optimuscode.core.utils.CommonUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author sujay
 */
public class JavaCompilerServiceTest {
    Project project;
    public JavaCompilerServiceTest() {
    }
    
    @Before
    public void setUp() {
        project = JavaProject.create("session1", CommonUtils.generateUUID(),
                JavaCompilerServiceImpl.create(),
                GradleTestRunnerService.create(),
                new MetricsServiceImpl(),
                "/home/sbhowmick/tmp/");
    }
    
    @After
    public void tearDown() {
        project.close();
    }

    /**
     * Test of run method, of class CompilerService.
     */
    @Test
    public void testCompile() throws Exception {
        String javaFileName = "../optimuscode-api/src/test/resources/TestSourceFile.txt";
        String className = "TestSourceFile";
        String fileContents = FileUtils.fileRead(javaFileName);
        CompilerService runner = JavaCompilerServiceImpl.create();
        CompilationUnit unit = new CompilationUnit(this);
        unit.addSource(className, fileContents);
        project.setTestClassName(className);
        project.setUnit(unit);
        runner.compile(project);
        CompilationListener listener = runner.getCompilationListener();
        assertTrue(listener.isSuccess());
        assertFalse(listener.hasErrors());
        
    }
}