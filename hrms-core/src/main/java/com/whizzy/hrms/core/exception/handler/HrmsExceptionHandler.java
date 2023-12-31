package com.whizzy.hrms.core.exception.handler;

import com.whizzy.hrms.core.exception.EntityNotFoundException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HrmsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundException(Exception ex, WebRequest request) {
        return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
                .title("Entity not found in database.")
                .type(URI.create(request.getContextPath()))
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException(Exception ex, WebRequest request) {
        return ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage())
                .title("Authentication Exception")
                .type(URI.create(request.getContextPath()))
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(Exception ex, WebRequest request) {
        return ErrorResponse.builder(ex, HttpStatus.UNAUTHORIZED, ex.getMessage())
                .title("Access Denied Exception")
                .type(URI.create(request.getContextPath()))
                .build();
    }

    @ExceptionHandler
    public ErrorResponse handleSignatureException(SignatureException se, WebRequest request) {
        return ErrorResponse.builder(se, HttpStatus.UNAUTHORIZED, se.getMessage())
                .title("Invalid Authorization Token")
                .type(URI.create(request.getContextPath()))
                .build();
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllException(Exception ex, WebRequest request) {
        return ErrorResponse.builder(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage())
                .title("Generic Exception")
                .type(URI.create(request.getContextPath()))
                .build();
    }
}
