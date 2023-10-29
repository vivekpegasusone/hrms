package com.whizzy.hrms.core.exception;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -783431806928578821L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
