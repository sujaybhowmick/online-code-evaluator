package com.optimuscode.core.common.compiler;

import com.optimuscode.core.common.model.CompilationUnit;

import java.util.EventListener;
import java.util.List;

public interface CompilationListener extends EventListener{

    void notify(CompilationUnit unit);

    Boolean isSuccess();

    Boolean hasErrors();

    List<String> getErrors();
}
