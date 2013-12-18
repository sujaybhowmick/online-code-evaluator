package com.optimuscode.core.common.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sujay
 * Date: 12/18/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public final class CompileEvent extends EventObject {
    private Boolean success;

    private List<String> errors;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @param success
     * @param errors
     * @throws IllegalArgumentException
     *          if source is null.
     */

    public CompileEvent(Object source, Boolean success, List<String> errors) {
        super(source);
        this.success = success;
        this.errors = errors;
    }

    public CompileEvent(Object source, Boolean success) {
        super(source);
        this.success = success;
        this.errors = Collections.EMPTY_LIST;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Boolean hasErrors(){
        if(errors.isEmpty()){
            return false;
        }
        return true;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
