package com.amrumpler.schedhype.domain;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import lombok.Data;

import com.amrumpler.schedhype.util.JsonUtils;

@Data
public class ErrorList {
    private Map<String, LinkedHashSet<String>> errors = new HashMap<String, LinkedHashSet<String>>();
    private static final String ERRORS = "errors";
    private static final String BLANK = "blank";
    
    public ErrorList append(ErrorList errorList) {
        errors.get(ERRORS).addAll(errorList.errors.get(ERRORS));
        errors.get(BLANK).addAll(errorList.errors.get(BLANK));
        return this;
    }

    public ErrorList() {
        errors.put(ERRORS, new LinkedHashSet<String>());
        errors.put(BLANK, new LinkedHashSet<String>());
    }
    
    public ErrorList(String initialError){
    	this();
    	addError(initialError);
    }

    public final ErrorList addError(String errorLabel) {
        errors.get(ERRORS).add(errorLabel);
        return this;
    }

    public ErrorList addBlankField(String fieldName) {
        errors.get(BLANK).add(fieldName);
        return this;
    }

    public int size() {
        return errors.get(ERRORS).size() + errors.get(BLANK).size();
    }

    public boolean hasErrors() {
        return errors.get(ERRORS).size() > 0;
    }

    public Set<String> getErrorKeys() {
        return errors.get(ERRORS);
    }

    public Set<String> getBlankKeys() {
        return errors.get(BLANK);
    }

    public String toJSON() {
        return JsonUtils.toJson(errors);
    }
}