package com.optimuscode.core.java.compiler;

import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.compiler.CompileEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class DefaultCompilationListener implements CompilationListener {
    private Logger log = LoggerFactory.getLogger(
                                            DefaultCompilationListener.class);
    private CompileEvent event;
    @Override
    public void notify(CompileEvent event) {
        log.info("Notified of compilation");
        this.event = event;
    }

    @Override
    public Boolean isSuccess() {
        return this.event.isSuccess();
    }

    @Override
    public Boolean hasErrors() {
        return event.hasErrors();
    }

    @Override
    public List<String> getErrors() {
        if(hasErrors()){
            return event.getErrors();
        }
        return Collections.EMPTY_LIST;
    }
}
