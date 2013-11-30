package com.optimuscode.thrift.server;

import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.common.testrunner.TestRunnerListener;
import com.optimuscode.core.java.compiler.JavaCompilerServiceImpl;
import com.optimuscode.core.java.metrics.CheckStyleService;
import com.optimuscode.core.java.metrics.MetricsListener;
import com.optimuscode.core.java.metrics.MetricsService;
import com.optimuscode.core.java.metrics.result.Metric;
import com.optimuscode.core.java.model.JavaProject;
import com.optimuscode.core.java.testrunner.GradleTestRunnerService;
import com.optimuscode.core.java.testrunner.TestRunnerService;
import com.optimuscode.core.utils.SupportedLanguage;
import com.optimuscode.thrift.api.*;
import com.optimuscode.thrift.commons.Configuration;
import com.optimuscode.thrift.commons.ConfigurationManager;
import com.twitter.util.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 11/6/13
 * Time: 12:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class RpcCompileNTestServiceHandler implements
                                    RpcCompileNTestService.FutureIface {
    private static Map<String, SupportedLanguage> supportedLanguages;
    static {
        supportedLanguages = new HashMap<String, SupportedLanguage>();
        supportedLanguages.put(SupportedLanguage.Java.toString(),
                                    SupportedLanguage.Java);
        supportedLanguages.put(SupportedLanguage.Groovy.toString(),
                                    SupportedLanguage.Groovy);
        supportedLanguages.put(SupportedLanguage.C.toString(),
                                    SupportedLanguage.C);
        supportedLanguages.put(SupportedLanguage.Cpp.toString(),
                                    SupportedLanguage.Cpp);

    }
    protected Logger log =
                LoggerFactory.getLogger(RpcCompileNTestServiceHandler.class);

    private String msg;

    private ConfigurationManager mgr = ConfigurationManager.getInstance();

    public RpcCompileNTestServiceHandler(final String msg){
        this.msg = msg;
    }

    @Override
    public Future<String> echo(String msg) {
        if(msg != null && msg.length() > 0){
            log.info("echoing incoming message..." + msg);
            return Future.value("echoed message:" + msg);
        }
        return Future.value("INFO: null or 0 length message");
    }

    @Override
    public Future<CompilerResult> compile(Session session, SourceUnit unit) {

        log.info("Compiling code for session - " + session.getUuid());

        Project project = prepareProject(session, unit);

        CompilerService runner = JavaCompilerServiceImpl.create();

        runner.compile(project);

        CompilationListener listener = runner.getCompilationListener();

        return Future.value(prepareCompileResult(listener));
    }

    private SupportedLanguage getLanguageEnum(String language) {
        return supportedLanguages.get(language);
    }

    @Override
    public Future<TestResult> runTest(Session session, SourceUnit unit) {

        log.info("Running Tests for session - " + session.getUuid());

        Project project = prepareProject(session, unit);

        TestRunnerService runnerService = GradleTestRunnerService.create();

        try{
            runnerService.runTest(project);
        }catch(Exception e){
            log.info("build exception, can be due to failed tests", e);
        }

        TestRunnerListener listener =
                runnerService.getTestRunnerListener();

        return Future.value(prepareTestResult(listener));
    }

    @Override
    public Future<Map<String, List<CodeMetric>>> runMetrics(Session session,
                                                            SourceUnit unit) {

        log.info("Running Tests for session - " + session.getUuid());

        Project project = prepareProject(session, unit);

        MetricsService metricsService = CheckStyleService.create();

        metricsService.runMetrics(project);

        MetricsListener listener = metricsService.getMetricsListener();

        Map<String, Collection<Metric>> metricsGroups = listener.getMetricGroups();

        return Future.value(prepareCodeMetrics(metricsGroups));
    }

    private CompilationUnit prepareCompilationUnit(SourceUnit unit){
        SupportedLanguage enumLang = getLanguageEnum(unit.getLanguage());

        CompilationUnit compilationUnit = new CompilationUnit(this, enumLang);

        compilationUnit.addSource(unit.getClassName(), unit.getSourceCode());

        compilationUnit.addSource(unit.getTestClassName(),
                                        unit.getTestSourceCode());
        return compilationUnit;
    }

    private Project prepareProject(Session session, SourceUnit unit){
        Configuration config = mgr.getConfig("server-config");

        CompilationUnit compilationUnit = prepareCompilationUnit(unit);

        Project project = JavaProject.create(session.getUuid(),
                                            session.getUuid(),
                                            config.getBasefolder());
        project.setTestClassName(unit.getTestClassName());

        project.setUnit(compilationUnit);

        return project;
    }

    private Map<String, List<CodeMetric>> prepareCodeMetrics(
                        Map<String,Collection<Metric>> metricsGroups){
        Map<String, List<CodeMetric>> thriftMetrics =
                                    new HashMap<String, List<CodeMetric>>();

        for(String key: metricsGroups.keySet()){
            Collection<Metric> metrics = metricsGroups.get(key);
            List<CodeMetric> codeMetricsList = prepareCodeMetricList(metrics);
            thriftMetrics.put(key, codeMetricsList);
        }

        return thriftMetrics;
    }

    private List<CodeMetric> prepareCodeMetricList(Collection<Metric> metrics){
        List<CodeMetric> codeMetricsList = new ArrayList<CodeMetric>();

        for(Metric metric: metrics){
            CodeMetric codeMetric = new CodeMetric.Builder().
                    metricMessage(metric.getMessage()).
                    errorType(metric.getErrorType()).
                    build();
            codeMetricsList.add(codeMetric);
        }

        return codeMetricsList;
    }

    private CompilerResult prepareCompileResult(CompilationListener listener){
        CompilerResult compilerResult = new CompilerResult.Builder().
                passed(listener.isSuccess()).
                errors(listener.getErrors()).build();

        return compilerResult;
    }

    private TestResult prepareTestResult(TestRunnerListener listener){
        TestResult testResult = new TestResult.Builder().
                testCaseCount(listener.getTestCaseCount()).
                errorCount(listener.getErrorCount()).
                failureCount(listener.getFailureCount()).
                successCount(listener.getSuccessCount()).
                runTime(listener.getRunTime()).build();

        return testResult;
    }

}
