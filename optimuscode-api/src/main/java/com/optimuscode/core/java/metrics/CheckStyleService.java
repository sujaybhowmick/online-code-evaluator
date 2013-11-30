package com.optimuscode.core.java.metrics;

import com.google.common.collect.Lists;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.java.metrics.result.MetricsResult;
import com.optimuscode.core.java.metrics.result.MetricsSerializer;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.gradle.api.Action;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 10/14/13
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckStyleService implements MetricsService{

    private MetricsListener metricsListener;

    private CheckStyleService(){}

    public static MetricsService create(){
        return new CheckStyleService();
    }

    @Override
    public void runMetrics(Project project) {
        if(project == null){
            throw new RuntimeException("Should have project to run");
        }
        project.reOpen();

        if(this.metricsListener == null){
            this.metricsListener = new DefaultMetricsListener();
        }

        project.dumpSource();

        final String projectDir = project.getProjectFolder();
        final File checkStyleResultDir = new File(project.getCsResultFolder());

        Configuration config = loadConfig(
                projectDir + File.separatorChar +
                            Project.DEFAULT_CHECKSTYLE_CONFIG,
                System.getProperties());

        final AuditListener listener =
                            createListener(checkStyleResultDir);
        String[] dirs = {project.getSourceFolder()};
        final List<File> files = getFilesToProcess(dirs);
        final Checker c = createChecker(config, listener);

        final int numErrs = c.process(files);
        MetricsResult result = readResult(checkStyleResultDir);

        for(Configuration configuration: config.getChildren()){
            Configuration[] children = configuration.getChildren();
            for(Configuration configuration1: children){
                result.addAvailableCheck(
                        configuration1.getName() + "Check");
            }
        }
        this.metricsListener.notify(result);
        c.destroy();
    }

    private MetricsResult readResult(File checkStyleResultDir) {
        final MetricsResult result = new MetricsResult();
        MetricsSerializer serializer =
                new MetricsSerializer(checkStyleResultDir);
        serializer.read(new Action<MetricsResult>() {
            @Override
            public void execute(MetricsResult metricsResult) {
                result.setMetrics(metricsResult.getMetrics());
            }
        });
        return result;
    }

    private Checker createChecker(Configuration aConfig, AuditListener listener) {
        Checker c = null;
        try {
            c = new Checker();

            final ClassLoader moduleClassLoader =
                    Checker.class.getClassLoader();
            c.setModuleClassLoader(moduleClassLoader);
            c.configure(aConfig);
            c.addListener(listener);
        }
        catch (final Exception e) {
            System.out.println("Unable to create Checker: "
                    + e.getMessage());
            e.printStackTrace(System.out);
        }
        return c;
    }

    private Configuration loadConfig(String config, Properties aProps) {
        try {
            return ConfigurationLoader.loadConfiguration(
                    config, new PropertiesExpander(aProps));
        }
        catch (final CheckstyleException e) {
            System.out.println("Error loading configuration file");
            e.printStackTrace(System.out);
            System.exit(1);
            return null; // can never get here
        }
    }

    private List<File> getFilesToProcess(String[] dirs) {
        final List<File> files = Lists.newLinkedList();
        for (String element : dirs) {
            traverse(new File(element), files);
        }
        return files;
    }

    private void traverse(File aNode, List<File> aFiles) {
        if (aNode.canRead()) {
            if (aNode.isDirectory()) {
                final File[] nodes = aNode.listFiles();
                for (File element : nodes) {
                    traverse(element, aFiles);
                }
            }
            else if (aNode.isFile()) {
                aFiles.add(aNode);
            }
        }
    }

    private AuditListener createListener(File resultDir) {
        AuditListener listener = new MetricsAuditLogger(resultDir);
        return listener;
    }

    @Override
    public MetricsListener getMetricsListener() {
        return this.metricsListener;
    }

    @Override
    public void setMetricsListener(MetricsListener metricsListener) {
        this.metricsListener = metricsListener;
    }
}
