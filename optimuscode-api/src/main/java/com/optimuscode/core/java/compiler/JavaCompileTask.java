package com.optimuscode.core.java.compiler;

import com.optimuscode.core.common.OptimusRuntimeException;
import com.optimuscode.core.common.model.CompilationUnit;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.common.model.SourceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class JavaCompileTask implements Callable<Boolean> {

    final Logger log = LoggerFactory.getLogger(JavaCompileTask.class);

    private Project project;

    private JavaCompileTask(final Project project){
        this.project = project;
    }

    public static JavaCompileTask create(final Project project){
        return new JavaCompileTask(project);
    }

    protected  Boolean compile(){
        final CompilationUnit unit = project.getUnit();
        final List<SourceUnit> sourceUnits = unit.getSources();
        if (sourceUnits == null || sourceUnits.isEmpty()) {
            log.debug("No Classes to compile");
            throw new IllegalArgumentException("No Java class to compile");
        }
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();


        final DiagnosticCollector<JavaFileObject> diagnostics =
                new DiagnosticCollector<JavaFileObject>();

        JavaFileManager fileManager = compiler.getStandardFileManager(
                diagnostics, null, null);

        fileManager = new ForwardingJavaFileManager<JavaFileManager>(fileManager){
            @Override
            public JavaFileObject getJavaFileForOutput(JavaFileManager.Location
                                  location, String className, JavaFileObject.Kind kind,
                                  FileObject sibling) throws IOException {
                // Create the Structure of the project before
                if(!project.exists()){
                    project.open();
                }
                File[] classOutputDir = {new File(project.getFolderPath())};
                ((StandardJavaFileManager)fileManager).setLocation(StandardLocation.CLASS_OUTPUT,
                        Arrays.asList(classOutputDir));
                return fileManager.getJavaFileForOutput(location, className, kind, sibling);
            }
        };

        try {

            List<JavaFileObject> javaSources = buildSource(sourceUnits);

            JavaCompiler.CompilationTask compileTask = compiler.getTask(null,
                    fileManager, diagnostics,
                    null, null, javaSources);
            Boolean result = compileTask.call();
            addErrors(unit, diagnostics);
            return result;

        }finally{
            try{
                fileManager.close();
            }catch(IOException ioe){
                throw new OptimusRuntimeException(ioe);
            }
        }
    }

    private List<JavaFileObject> buildSource(List<SourceUnit> sourceUnits) {
        List<JavaFileObject> javaFileObjects = new ArrayList<JavaFileObject>();
        for (SourceUnit source: sourceUnits){
            StringBuilderJavaSource javaSource =
                    new StringBuilderJavaSource(source.getName());
            javaSource.append(source.getSource());
            javaFileObjects.add(javaSource);
        }
        return javaFileObjects;
    }

    private void addErrors(final CompilationUnit unit,
                           final DiagnosticCollector<JavaFileObject> d){
        for (Diagnostic<? extends JavaFileObject> diagnostic :
                                                        d.getDiagnostics()) {
            Diagnostic.Kind kind = diagnostic.getKind();
            if(Diagnostic.Kind.ERROR.equals(kind)){
                String error = diagnostic.getKind() + ": "
                        + diagnostic.getMessage(null) +
                        " for class:" + diagnostic.getSource().getName() +
                        " @ line:" + diagnostic.getLineNumber() +
                        " @ column:" + diagnostic.getColumnNumber();
                unit.addError(error);
            }
        }
    }

    @Override
    public Boolean call() throws Exception {
        return compile();
    }
}
