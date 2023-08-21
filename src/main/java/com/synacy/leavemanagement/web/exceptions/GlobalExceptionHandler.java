package com.synacy.leavemanagement.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAdminException.class)
    public ApiErrorResponse handleInvalidAdminException(InvalidAdminException e) {
        return new ApiErrorResponse("INVALID_RIGHTS", e.getMessage());
    }
}
