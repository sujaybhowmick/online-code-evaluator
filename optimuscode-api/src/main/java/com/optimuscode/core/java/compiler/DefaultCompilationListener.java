package com.optimuscode.core.java.compiler;

import com.optimuscode.core.common.compiler.CompilationListener;
import com.optimuscode.core.common.model.CompilationUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class DefaultCompilationListener implements CompilationListener {
    private Logger log = LoggerFactory.getLogger(
                                            DefaultCompilationListener.class);
    private CompilationUnit unit;
    @Override
    public void notify(CompilationUnit unit) {
        log.info("Notified of compilation");
        this.unit = unit;
    }

    @Override
    public Boolean isSuccess() {
        return this.unit.isSuccess();
    }

    @Override
    public Boolean hasErrors() {
        return unit.hasErrors();
    }

    @Override
    public List<String> getErrors() {
        if(hasErrors()){
            return unit.getErrors();
        }
        return Collections.EMPTY_LIST;
    }
}
