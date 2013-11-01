package com.optimuscode.core.common.model;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public abstract class ProcessingUnit extends EventObject{

    List<String> errors;

    public ProcessingUnit(Object source){
        super(source);
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
}
