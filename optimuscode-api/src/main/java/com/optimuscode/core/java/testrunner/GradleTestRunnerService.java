/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.optimuscode.core.java.testrunner;

import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.common.testrunner.TestRunnerListener;
import com.optimuscode.core.java.testrunner.result.TestClassResult;
import com.optimuscode.core.java.testrunner.result.TestResultSerializer;
import org.gradle.api.Action;
import org.gradle.tooling.BuildException;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author sujay
 */
public class GradleTestRunnerService implements TestRunnerService{

    protected static final String DEFAULT_Xmx = "128m";

    protected static final String DEFAULT_MaxPermSize = "32m";

    private TestRunnerListener testRunnerListener;

    private GradleTestRunnerService() {
    }

    public static TestRunnerService create() {
        return new GradleTestRunnerService();
    }

    public synchronized void runTest(final Project project) {

        if(project == null){
            throw new RuntimeException("Should have project to run");
        }
        project.reOpen();
        if(this.testRunnerListener == null){
            this.testRunnerListener = new DefaultTestRunnerListener();
        }
        GradleConnector connector = GradleConnector.newConnector();
        final String projectDir = project.getProjectFolder();
        final String absolutePath = projectDir + File.separatorChar +
                                    Project.TEST_RESULT_DIR;
        final TestSuiteResultNode results = TestSuiteResultNode.create(this);
        connector.forProjectDirectory(new File(projectDir));
        
        ProjectConnection connection = connector.connect();
        
        try {
            
            BuildLauncher launcher = connection.newBuild();
            
            launcher.forTasks("test");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            launcher.setStandardOutput(outputStream);
            launcher.setStandardError(outputStream);
            launcher.withArguments(prepareBuildArguments(project));
            launcher.setJvmArguments(prepareJvmArguments());
            try {
                launcher.run();
            } catch (BuildException e) {}


            TestResultSerializer serializer = new TestResultSerializer(
                    new File(absolutePath));
            results.setSuiteName(project.getTestClassName());
            serializer.read(new Action<TestClassResult>() {
                @Override
                public void execute(TestClassResult t) {

                    results.setTestCaseCount(t.getTestsCount());
                    results.setFailureCount(t.getFailuresCount());
                    int successCount = t.getTestsCount()
                            - t.getFailuresCount();
                    results.setSuccessCount(successCount);
                    int errorCount = t.getTestsCount()
                            - (t.getFailuresCount() + successCount);
                    results.setErrorCount(errorCount);
                    BigDecimal r = (new BigDecimal(t.getDuration())).
                                        divide(new BigDecimal(1000)).
                                                setScale(3, RoundingMode.UP);
                    results.setRunTime(r.doubleValue());
                }
            });
           
            this.testRunnerListener.notify(results);
        } finally {
            // Clean up
            connection.close();
        }
    }

    @Override
    public TestRunnerListener getTestRunnerListener() {
        return this.testRunnerListener;
    }


    private String[] prepareBuildArguments(final Project project){
        return new String[]{
                "-b=build-" + project.getUnit().getLanguage() + "" + ".gradle",
                "-Dtest.single=" + project.getTestClassName()
        };
    }


    private String[] prepareJvmArguments(){
       return new String[]{
              "-Xmx" + DEFAULT_Xmx,
              "-XX:MaxPermSize=" + DEFAULT_MaxPermSize
        };
    }

    @Override
    public void setTestRunnerListener(TestRunnerListener testRunnerListener) {
        this.testRunnerListener = testRunnerListener;
    }
}
