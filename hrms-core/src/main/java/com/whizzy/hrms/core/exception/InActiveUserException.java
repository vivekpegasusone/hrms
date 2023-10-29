package com.whizzy.hrms.core.exception;

import java.io.Serial;

public class InActiveUserException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1948494571621008038L;

    public InActiveUserException(String message) {
        super(message);
    }
}
