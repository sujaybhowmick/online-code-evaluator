package com.optimuscode.core.common.model;

import com.optimuscode.core.utils.SupportedLanguage;

import java.util.ArrayList;
import java.util.List;

public class CompilationUnit extends ProcessingUnit{

    protected List<SourceUnit> sources;

    private Boolean success;

    private SupportedLanguage language = SupportedLanguage.Java;

    public CompilationUnit(Object source, SupportedLanguage language){
        super(source);
        this.sources = new ArrayList<SourceUnit>();
        this.language = language;
    }

    public void addSource(final String name, final String source){
        final String extension = sourceExtension.get(language);
        this.sources.add(new SourceUnit(this, name, source, extension));
    }

    public List<SourceUnit> getSources(){
        return this.sources;
    }


    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public SupportedLanguage getLanguage() {
        return language;
    }

    public void setLanguage(final SupportedLanguage language) {
        this.language = language;
    }
}
