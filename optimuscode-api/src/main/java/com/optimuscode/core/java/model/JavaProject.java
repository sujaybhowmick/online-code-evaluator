package com.optimuscode.core.java.model;

import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.java.metrics.MetricsService;
import com.optimuscode.core.java.testrunner.TestRunnerService;

public class JavaProject extends Project {

    private JavaProject(final String projectName, final String projectId,
                        final String... baseFolder){
        super(projectName, projectId, baseFolder);
    }

    public static Project create(final String projectName,
                                 final String projectId,
                                 final String... baseFolder){
        Project project = new JavaProject(projectName, projectId, baseFolder);
        return project;
    }
}
