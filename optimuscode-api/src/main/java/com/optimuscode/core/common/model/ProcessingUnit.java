package com.optimuscode.core.common.model;

import com.optimuscode.core.utils.SupportedLanguage;

import java.util.*;

public abstract class ProcessingUnit {
    protected static Map<SupportedLanguage, String> sourceExtension;
    static{
        sourceExtension = new HashMap<SupportedLanguage, String>();
        sourceExtension.put(SupportedLanguage.Java, ".java");
        sourceExtension.put(SupportedLanguage.Groovy, ".groovy");
        sourceExtension.put(SupportedLanguage.C, ".c");
        sourceExtension.put(SupportedLanguage.Cpp, ".cpp");
    }

    List<String> errors;

    public ProcessingUnit(){
        this.errors = new ArrayList<String>();

    }

    public void addError(String error){
        errors.add(error);
    }

    public Boolean hasErrors(){
        return !errors.isEmpty();
    }

    public List<String> getErrors(){
        return this.errors;
    }

    public static String getSourceExtensionFor(SupportedLanguage language){
        return sourceExtension.get(language);
    }
}
