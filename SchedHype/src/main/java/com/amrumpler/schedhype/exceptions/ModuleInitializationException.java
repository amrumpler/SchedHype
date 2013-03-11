package com.amrumpler.schedhype.exceptions;

public class ModuleInitializationException extends RuntimeException {

    private static final long serialVersionUID = 794345114295550370L;

    public ModuleInitializationException(Exception e) {
        super(e);
    }

    public ModuleInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}