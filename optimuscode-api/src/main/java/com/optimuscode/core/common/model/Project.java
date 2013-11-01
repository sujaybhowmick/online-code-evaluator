package com.optimuscode.core.common.model;

import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.java.metrics.MetricsService;
import com.optimuscode.core.java.testrunner.TestRunnerService;
import org.apache.commons.vfs2.*;

import java.io.File;

public abstract class Project{
    private String projectId;
    private String projectName;
    private final static String BUILD = "/build";
    private final static String SRC = "src";
    private final static String CLASSES = "/classes";
    private final static String CLASSES_TEST = "/test";
    private final static String DEFAULT_BASE_FOLDER = "/tmp";
    private final static String DEFAULT_BUILD_FILE = "build.gradle";
    private final static String BUILD_FILE_PREFIX = "build-";
    private final static String BUILD_FILE_EXT = ".gradle";

    public static final String TEST_RESULT_DIR =
            "build/test-results/binary/test/";

    private boolean exists = false;

    private String folderPath;

    private String projectFolder;

    private String baseFolder;

    private String sourceFolder;

    private FileSystemManager fsManager;

    private String testClassName;

    private String className;

    private CompilationUnit unit;

    private ProjectEvent event;

    public Project(final String projectName, final String projectId,
                   final CompilerService compilerService,
                   final TestRunnerService testRunnerService,
                   final MetricsService metricsService,
                   final String... baseFolder) {

        this.projectName = projectName;
        this.projectId = projectId;

        if (baseFolder != null && baseFolder.length > 0) {
            this.baseFolder = baseFolder[0];
        } else {
            this.baseFolder = DEFAULT_BASE_FOLDER;
        }
    }


    public void open(){
        event = new ProjectEvent(this);

        createProjectFolder().createBuildFolder().
                createClassesFolder().createClassesTestFolder();
    }


    public Project createProjectFolder() {
        try {
            fsManager = VFS.getManager();
            FileObject projectFolder =
                    fsManager.resolveFile(baseFolder + File.separatorChar +
                            getProjectId());
            exists = projectFolder.exists();
            if (!exists) {
                projectFolder.createFolder();
                folderPath = projectFolder.getURL().getPath();
                this.projectFolder = folderPath;
                copyBuildFile(projectFolder);
            } else {
                folderPath = projectFolder.getURL().getPath();
                this.projectFolder = folderPath;
                copyBuildFile(projectFolder);
            }
            createSrcFolder(projectFolder);
            return this;
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }
    }

    public void copyBuildFile(FileObject projectFolder) {
        String gradleBuildFile = BUILD_FILE_PREFIX + unit.getLanguage() +
                                                                BUILD_FILE_EXT;
        try {
            FileObject cwd =
                    fsManager.resolveFile(this.baseFolder);
            FileObject src =
                    fsManager.resolveFile(cwd, gradleBuildFile);
            if(!src.exists()){
                src = fsManager.resolveFile(cwd, DEFAULT_BUILD_FILE);
            }
            FileObject destination = fsManager.resolveFile(projectFolder,
                                                               gradleBuildFile);
            if (projectFolder.exists() && !destination.exists() &&
                    projectFolder.getType() == FileType.FOLDER) {
                destination.copyFrom(src, Selectors.SELECT_FILES);
            }
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }
    }

    public Project createBuildFolder() {
        try {
            FileObject buildFolder =
                    fsManager.resolveFile(getFolderPath() + BUILD);
            buildFolder.createFolder();
            folderPath = buildFolder.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSrcFolder(FileObject projectFolder) {
        try {
            FileObject srcFolder =
                    fsManager.resolveFile(projectFolder,  SRC);
            srcFolder.createFolder();
            sourceFolder = srcFolder.getURL().getPath();
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }

    }


    public Project createClassesFolder() {
        try {
            FileObject classesFolder =
                    fsManager.resolveFile(getFolderPath() + CLASSES);
            classesFolder.createFolder();
            folderPath = classesFolder.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }

    }

    public Project createClassesTestFolder() {
        try {
            FileObject testClassesFolder =
                    fsManager.resolveFile(getFolderPath() +
                            CLASSES_TEST);
            testClassesFolder.createFolder();
            folderPath = testClassesFolder.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }

    }

    public Project createFile(final String fileName) {
        try {
            FileObject fileObject =
                    fsManager.resolveFile(getFolderPath() +
                            fileName);
            fileObject.createFile();
            folderPath = fileObject.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public boolean exists() {
        return exists;
    }

    public void close() {
        try {
            if(this.projectFolder != null){
                FileObject projectFolder = fsManager.resolveFile(this.projectFolder);
                projectFolder.deleteAll();
            }
        } catch (FileSystemException e) {
            throw new RuntimeException(e);
        }finally{
        }
    }

    public void setUnit(final CompilationUnit unit) {
        this.unit = unit;
    }

    public CompilationUnit getUnit(){
        return this.unit;
    }

    public String getSourceFolder() {
        return sourceFolder;
    }


    public String getClassName() {
        return className;
    }
}