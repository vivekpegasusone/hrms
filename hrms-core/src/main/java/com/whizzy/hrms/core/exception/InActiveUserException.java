package com.whizzy.hrms.core.exception;

public class InActiveUserException extends RuntimeException {

    private static final long serialVersionUID = -1948494571621008038L;

    public InActiveUserException(String message) {
        super(message);
    }
}
