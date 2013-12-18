/**
 * Copyright 2013 #name#
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.optimuscode.core.java.compiler;

import com.optimuscode.core.common.OptimusRuntimeException;
import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.compiler.CompilerService;
import com.optimuscode.core.common.model.Project;
import com.optimuscode.core.common.compiler.CompileEvent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author sujay
 */
public final class JavaCompilerServiceImpl implements CompilerService {

    private ExecutorService executor;

    private CompilationListener compilationListener;
    
    private JavaCompilerServiceImpl(){
        this.executor = Executors.newCachedThreadPool();
    }
    
    public static CompilerService create(){
        return new JavaCompilerServiceImpl();
    }

    @Override
    public void compile(final Project project) {
        if(this.compilationListener == null){
            this.compilationListener = new DefaultCompilationListener();
        }
        final JavaCompileTask compileTask = JavaCompileTask.create(project);
        final Future<Boolean> compileFuture = executor.submit(compileTask);

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
