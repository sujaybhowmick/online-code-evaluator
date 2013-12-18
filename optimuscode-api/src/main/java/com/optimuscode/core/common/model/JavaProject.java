package com.optimuscode.core.common.model;

import com.optimuscode.core.common.OptimusRuntimeException;
import org.apache.commons.vfs2.*;

public class JavaProject extends Project {

    public  final static String DEFAULT_CHECKSTYLE_CONFIG = "checkstyle.xml";

    public static final String CHECKSTYLE_RESULT_DIR =
            "build/results/binary/checkstyle/";



    private String csResultFolder;


    private JavaProject(final String projectName, final String projectId,
                        final String... baseFolder){
        super(projectName, projectId, baseFolder);
        setSingleTest(true);
    }

    @Override
    protected Project createCopyProjectFiles() {
        return copyCheckStyleFile().createCheckStyleResultFolder();
    }


    public static Project create(final String projectName,
                                 final String projectId,
                                 final String... baseFolder){
        Project project = new JavaProject(projectName, projectId, baseFolder);
        return project;
    }

    protected JavaProject copyCheckStyleFile(){
        try {
            FileObject projectFolder = resolveProjectFolder();
            FileObject cwd = fsManager.resolveFile(getBaseFolder());
            FileObject src = fsManager.resolveFile(cwd,
                    DEFAULT_CHECKSTYLE_CONFIG);
            FileObject destination = fsManager.resolveFile(projectFolder,
                    DEFAULT_CHECKSTYLE_CONFIG);
            if (projectFolder.exists() && !destination.exists() &&
                    projectFolder.getType() == FileType.FOLDER) {
                destination.copyFrom(src, Selectors.SELECT_FILES);
            }
            return this;
        }catch(FileSystemException e){
            throw new OptimusRuntimeException(e);
        }
    }

    protected JavaProject createCheckStyleResultFolder(){
        try {
            FileObject projectFolder = resolveProjectFolder();
            FileObject checkStyleResultFolder =
                    fsManager.resolveFile(projectFolder,  CHECKSTYLE_RESULT_DIR);
            checkStyleResultFolder.createFolder();
            csResultFolder = checkStyleResultFolder.getURL().getPath();
            return this;
        }catch(FileSystemException e){
            throw new OptimusRuntimeException(e);
        }
    }

    public String getCsResultFolder() {
        return csResultFolder;
    }

    public void setCsResultFolder(String csResultFolder) {
        this.csResultFolder = csResultFolder;
    }

}
