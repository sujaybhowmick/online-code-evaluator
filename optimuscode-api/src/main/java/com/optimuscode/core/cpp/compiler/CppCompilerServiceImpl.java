package com.optimuscode.core.cpp.compiler;

import com.optimuscode.core.common.OptimusRuntimeException;
import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.compiler.CompileEvent;
import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.java.compiler.DefaultCompilationListener;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 12/12/13
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CppCompilerServiceImpl implements CompilerService {

    private ExecutorService executor;

    private CompilationListener compilationListener;

    private CppCompilerServiceImpl(){
        this.executor = Executors.newCachedThreadPool();
    }


    public static CompilerService create(){
        return new CppCompilerServiceImpl();
    }

    @Override
    public void compile(Project project) {
        if(this.compilationListener == null){
            this.compilationListener = new DefaultCompilationListener();
        }
        final CppCompileTask compileTask = CppCompileTask.create(project);
        final Future<Boolean> compileFuture = this.executor.submit(compileTask);
        try {
            Boolean result = compileFuture.get();
            compilationListener.notify(new CompileEvent(this, result));
        } catch (InterruptedException ex) {
            throw new OptimusRuntimeException(ex);
        } catch (ExecutionException ex) {
            throw new OptimusRuntimeException(ex);
        }
    }

    @Override
    public void setCompilationListener(CompilationListener listener) {
        this.compilationListener = listener;
    }

    @Override
    public CompilationListener getCompilationListener() {
        return this.compilationListener;
    }
}
