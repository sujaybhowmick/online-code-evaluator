package com.optimuscode.core.common.compiler;

import java.util.EventListener;
import java.util.List;

public interface CompilationListener extends EventListener{

    void notify(CompileEvent event);

    Boolean isSuccess();

    Boolean hasErrors();

    List<String> getErrors();
}
