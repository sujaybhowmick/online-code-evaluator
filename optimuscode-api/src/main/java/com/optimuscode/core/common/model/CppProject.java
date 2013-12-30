package com.optimuscode.core.common.model;

import com.optimuscode.core.common.OptimusRuntimeException;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.Selectors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 12/12/13
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class CppProject extends Project {

    protected static final String COMPILE = "compile";
    protected static final String CPP = "cpp";
    protected static final String HEADERS = "headers";

    private String cppFolderPath;

    private String headersFolderPath;

    private CppProject(String projectName, String projectId, String... baseFolder) {
        super(projectName, projectId, baseFolder);
    }

    @Override
    protected Project createCopyProjectFiles() {
        createExecFolder().createCppFolder();
        return this;
    }

    @Override
    protected Project copyBuildFile() {
        String gradleBuildFile = BUILD_FILE_PREFIX + unit.getLanguage() +
                BUILD_FILE_EXT;
        setBuildFile(gradleBuildFile);
        FileObject projectFolder = resolveProjectFolder();
        try {
            FileObject cwd =
                    fsManager.resolveFile(getBaseFolder());
            FileObject src =
                    fsManager.resolveFile(cwd, gradleBuildFile);
            if(!src.exists()){
                src = fsManager.resolveFile(cwd, DEFAULT_BUILD_FILE);
            }
            FileObject destination = fsManager.resolveFile(projectFolder,
                    gradleBuildFile);
            writeBuildFile(destination);

            return this;
        } catch (FileSystemException e) {
            throw new OptimusRuntimeException(e);
        }
    }

    private void writeBuildFile(final FileObject destination)
                                            throws FileSystemException {
        final StringBuilder buildFileContents =
                new StringBuilder("apply plugin: 'cpp'\n").
                append("libraries {\n").
                append("\t").append(getClassName()).append("\n").append("}");

        FileWriter writer = null;

        try {
            writer = new FileWriter(destination.getURL().getPath());
            writer.write(buildFileContents.toString());
        } catch (Exception e) {
            throw new OptimusRuntimeException(e);
        }finally{
            try {
                writer.close();
            } catch (IOException e) {
                throw new OptimusRuntimeException(e);
            }
        }

    }

    protected CppProject createExecFolder(){
        try {
            final FileObject projectFolder = resolveProjectFolder();
            final FileObject srcFolder = resolveSourceFolder(projectFolder);
            final FileObject compileFolder = resolveCompileFolder(srcFolder);
            compileFolder.createFolder();

            return this;
        }catch(FileSystemException e){
            throw new OptimusRuntimeException(e);
        }
    }

    protected CppProject createCppFolder(){
        try {
            final FileObject projectFolder = resolveProjectFolder();
            final FileObject srcFolder = resolveSourceFolder(projectFolder);
            final FileObject compileFolder = resolveCompileFolder(srcFolder);
            final FileObject cppFolder = resolveCppFolder(compileFolder);
            cppFolder.createFolder();
            this.cppFolderPath = cppFolder.getURL().getPath();
            return this;
        }catch(FileSystemException e){
            throw new OptimusRuntimeException(e);
        }
    }
    /*
    protected CppProject createHeadersFolder(){
        try {
            final FileObject projectFolder = resolveProjectFolder();
            final FileObject srcFolder = resolveSourceFolder(projectFolder);
            final FileObject compileFolder = resolveCompileFolder(srcFolder);
            final FileObject headersFolder = resolveHeadersFolder(compileFolder);
            headersFolder.createFolder();
            this.headersFolderPath = headersFolder.getURL().getPath();
            return this;
        }catch(FileSystemException e){
            throw new OptimusRuntimeException(e);
        }
    }*/

    private FileObject resolveHeadersFolder(FileObject compileFolder)
                                                throws FileSystemException {
        return fsManager.resolveFile(compileFolder, HEADERS);
    }

    private FileObject resolveCppFolder(FileObject compileFolder)
                                                throws FileSystemException {
        return fsManager.resolveFile(compileFolder, CPP);
    }

    private FileObject resolveCompileFolder(FileObject srcFolder)
                                                throws FileSystemException {
        return fsManager.resolveFile(srcFolder, this.getClassName());
    }

    private FileObject resolveSourceFolder(FileObject projectFolder)
                                                throws FileSystemException {
        return fsManager.resolveFile(projectFolder, SRC);
    }


    public static Project create(final String projectName,
                                 final String projectId,
                                 final String... baseFolder){
        Project project = new CppProject(projectName, projectId, baseFolder);
        return project;
    }


    public String getCppFolderPath() {
        return cppFolderPath;
    }

    public void setCppFolderPath(String cppFolderPath) {
        this.cppFolderPath = cppFolderPath;
    }

    public String getHeadersFolderPath() {
        return headersFolderPath;
    }

    public void setHeadersFolderPath(String headersFolderPath) {
        this.headersFolderPath = headersFolderPath;
    }

    @Override
    public void dumpSource() {
        final String srcDir = getCppFolderPath();
        List<SourceUnit> sources = this.unit.getSources();
        for(SourceUnit source: sources){
            source.write(srcDir);
        }
    }
}
