package com.optimuscode.core.common.model;

import com.optimuscode.core.common.OptimusRuntimeException;
import org.apache.commons.vfs2.*;

import java.io.File;
import java.util.List;

public abstract class Project {
    protected String projectId;
    protected String projectName;
    protected final static String BUILD = "/build";
    protected final static String SRC = "src";
    protected final static String CLASSES = "/classes";
    protected final static String CLASSES_TEST = "/test";
    protected final static String DEFAULT_BASE_FOLDER = "/tmp";
    protected final static String DEFAULT_BUILD_FILE = "build.gradle";
    protected final static String BUILD_FILE_PREFIX = "build-";
    protected final static String BUILD_FILE_EXT = ".gradle";

    protected  final static String DEFAULT_CHECKSTYLE_CONFIG = "checkstyle.xml";

    public static final String TEST_RESULT_DIR =
            "build/test-results/binary/test/";


    private boolean exists = false;

    private String folderPath;

    private String projectFolder;

    private String baseFolder;

    private String sourceFolder;

    private String testClassName;

    private String className;

    private String buildFile;

    private boolean singleTest = true;

    protected CompilationUnit unit;

    protected FileSystemManager fsManager;

    public Project(final String projectName, final String projectId,
                   final String... baseFolder) {

        this.projectName = projectName;
        this.projectId = projectId;

        if (baseFolder != null && baseFolder.length > 0) {
            this.baseFolder = baseFolder[0];
        } else {
            this.baseFolder = DEFAULT_BASE_FOLDER;
        }
        try {
            fsManager = VFS.getManager();
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }

    }


    public void open(){
        createProjectFolder().createBuildFolder().copyBuildFile().
                createClassesFolder().createClassesTestFolder().
                createCopyProjectFiles().createSrcFolder();
    }

    protected abstract Project createCopyProjectFiles();

    public void reOpen(){
        open();
    }


    protected Project createProjectFolder(){
        try {

            FileObject projectFolder =
                    fsManager.resolveFile(getBaseFolder() + File.separatorChar +
                            getProjectId());
            boolean exists = projectFolder.exists();
            if (!exists) {
                projectFolder.createFolder();
                this.exists = true;

            }
            setFolderPath(projectFolder.getURL().getPath());
            setProjectFolder(projectFolder.getURL().getPath());

            return this;
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }
    }


    protected Project copyBuildFile() {
        String gradleBuildFile = BUILD_FILE_PREFIX + unit.getLanguage() +
                                                                BUILD_FILE_EXT;
        setBuildFile(gradleBuildFile);
        FileObject projectFolder = resolveProjectFolder();
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
            return this;
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }
    }

    protected Project createBuildFolder() {
        try {
            FileObject buildFolder =
                    fsManager.resolveFile(getFolderPath() + BUILD);
            buildFolder.createFolder();
            folderPath = buildFolder.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }
    }

    protected Project createSrcFolder() {
        try {
            FileObject projectFolder = resolveProjectFolder();
            FileObject srcFolder =
                    fsManager.resolveFile(projectFolder,  SRC);
            srcFolder.createFolder();
            sourceFolder = srcFolder.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }

    }


    protected Project createClassesFolder() {
        try {
            FileObject classesFolder =
                    fsManager.resolveFile(getFolderPath() + CLASSES);
            classesFolder.createFolder();
            folderPath = classesFolder.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }

    }

    protected Project createClassesTestFolder() {
        try {
            FileObject testClassesFolder =
                    fsManager.resolveFile(getFolderPath() +
                            CLASSES_TEST);
            testClassesFolder.createFolder();
            folderPath = testClassesFolder.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }

    }



    protected Project createFile(final String fileName) {
        try {
            FileObject fileObject =
                    fsManager.resolveFile(getFolderPath() +
                            fileName);
            fileObject.createFile();
            folderPath = fileObject.getURL().getPath();
            return this;
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }
    }

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public boolean exists() {
        return this.exists;
    }

    public void close() {
        try {
            if(this.projectFolder != null){
                FileObject projectFolder = resolveProjectFolder();
                projectFolder.deleteAll();
                this.exists = false;
            }
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }
    }



    public void dumpSource(){
        final String srcDir = getSourceFolder();
        List<SourceUnit> sources = this.unit.getSources();
        for(SourceUnit source: sources){
            source.write(srcDir);
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectFolder() {
        return projectFolder;
    }

    public void setProjectFolder(String projectFolder) {
        this.projectFolder = projectFolder;
    }

    public String getBaseFolder() {
        return baseFolder;
    }

    public void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
    }
    public String getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setUnit(final CompilationUnit unit) {
        this.unit = unit;
    }

    public CompilationUnit getUnit(){
        return this.unit;
    }

    public String getBuildFile() {
        return buildFile;
    }

    public void setBuildFile(String buildFile) {
        this.buildFile = buildFile;
    }


    public boolean isSingleTest() {
        return singleTest;
    }

    public void setSingleTest(boolean singleTest) {
        this.singleTest = singleTest;
    }

    public String resolvePath(final String path){
        try {
            return fsManager.resolveFile(path).getURL().getPath();
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }
    }

    protected FileObject resolveProjectFolder(){
        try {
            return fsManager.resolveFile(getProjectFolder());
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }
    }
}
