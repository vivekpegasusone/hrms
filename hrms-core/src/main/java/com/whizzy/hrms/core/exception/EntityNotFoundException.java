package com.whizzy.hrms.core.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -783431806928578821L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
