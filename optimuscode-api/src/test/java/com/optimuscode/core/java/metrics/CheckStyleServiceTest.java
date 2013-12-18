package com.optimuscode.core.java.metrics;

import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.java.metrics.result.Metric;
import com.optimuscode.core.common.model.JavaProject;
import com.optimuscode.core.utils.CommonUtils;
import com.optimuscode.core.utils.SupportedLanguage;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/30/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckStyleServiceTest {
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
    public void testRunMetrics() throws Exception {
        System.out.println("runTest");
        final String javaFileName1 = "./optimuscode-api/src/test/resources/Reverse.txt";
        final String javaFileName2 = "./optimuscode-api/src/test/resources/ReverseTest.txt";
        final String className1 = "Reverse";
        final String className2 = "ReverseTest";
        String fileContents1 = FileUtils.fileRead(javaFileName1);
        String fileContents2 = FileUtils.fileRead(javaFileName2);
        assertNotNull(fileContents1);
        CompilationUnit unit = new CompilationUnit(SupportedLanguage.Java);
        unit.addSource(className1, fileContents1);
        unit.addSource(className2, fileContents2);
        project.setTestClassName(className2);
        project.setUnit(unit);

        MetricsService metricsService = CheckStyleService.create();
        metricsService.runMetrics(project);
        MetricsListener listener = metricsService.getMetricsListener();
        Map<String, Collection<Metric>> groupedMetrics =
                                            listener.getMetricGroups();
        for(String key: groupedMetrics.keySet()){
            System.out.println(key);
            Collection<Metric> metrics = groupedMetrics.get(key);
            for(Metric metric: metrics){
                System.out.println(metric.getMessage());
            }
        }


    }


    @Test
    public void testRunMetricsErrors() throws Exception {
        System.out.println("runTest");
        final String javaFileName1 = "./optimuscode-api/src/test/resources/TestSourceFile.txt";
        final String javaFileName2 = "./optimuscode-api/src/test/resources/TestSourceFileTest.txt";
        final String className1 = "TestSourceFile";
        final String className2 = "TestSourceFileTest";
        String fileContents1 = FileUtils.fileRead(javaFileName1);
        String fileContents2 = FileUtils.fileRead(javaFileName2);
        assertNotNull(fileContents1);
        CompilationUnit unit = new CompilationUnit(SupportedLanguage.Java);
        unit.addSource(className1, fileContents1);
        unit.addSource(className2, fileContents2);
        project.setTestClassName(className2);
        project.setUnit(unit);

        MetricsService metricsService = CheckStyleService.create();
        metricsService.runMetrics(project);
        MetricsListener listener = metricsService.getMetricsListener();
        Map<String, Collection<Metric>> groupedMetrics =
                listener.getMetricGroups();
        for(String key: groupedMetrics.keySet()){
            System.out.println(key);
            Collection<Metric> metrics = groupedMetrics.get(key);
            for(Metric metric: metrics){
                System.out.println(metric.getMessage());
            }
        }


    }
}
