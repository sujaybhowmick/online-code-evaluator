package com.optimuscode.core.cpp.compiler;

import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.common.model.SourceUnit;
import org.gradle.tooling.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 12/16/13
 * Time: 11:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class CppCompileTask implements Callable<Boolean> {

    protected static final String DEFAULT_Xmx = "128m";

    protected static final String DEFAULT_MaxPermSize = "32m";

    final Logger log = LoggerFactory.getLogger(CppCompileTask.class);

    private static final String SUFFIX_EXCUTABLE = "Executable";

    private static final String SUFFIX_LIBRARY = "Library";

    private Project project;

    private CppCompileTask(final Project project){
        this.project = project;
    }

    public static CppCompileTask create(final Project project){
        return new CppCompileTask(project);
    }


    @Override
    public Boolean call() throws Exception {
       return compile();
    }

    protected Boolean compile(){

        final CompilationUnit unit = project.getUnit();
        final List<SourceUnit> sourceUnits = unit.getSources();
        if (sourceUnits == null || sourceUnits.isEmpty()) {
            log.debug("No Classes to compile");
            throw new IllegalArgumentException("no cpp classes to compile");
        }

        if(!project.exists()){
            project.open();
            project.dumpSource();
        }

        GradleConnector connector = GradleConnector.newConnector();
        final String projectDir = project.getProjectFolder();
        connector.forProjectDirectory(new File(projectDir));

        ProjectConnection connection = connector.connect();
        BuildLauncher launcher = connection.newBuild();

        launcher.forTasks(project.getClassName() + SUFFIX_EXCUTABLE);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        launcher.setStandardOutput(outputStream);
        launcher.setStandardError(outputStream);
        launcher.withArguments(prepareBuildArguments(project));
        launcher.setJvmArguments(prepareJvmArguments());

        try {
            launcher.run();
            return true;
        } catch (BuildException e) {
            log.debug("cpp compile failed", e.getMessage());
            return false;
        }finally {
            connection.close();
        }
    }

    private String[] prepareJvmArguments() {
        return new String[]{
                "-Xmx" + DEFAULT_Xmx,
                "-XX:MaxPermSize=" + DEFAULT_MaxPermSize
        };
    }

    private String[] prepareBuildArguments(Project project) {
        return new String[]{ "-b=" + project.getBuildFile(),
                            "-PcppExecutable=" + project.getClassName() };
    }
}


