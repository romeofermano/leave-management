package com.synacy.leavemanagement.web.exceptions;

import com.synacy.leavemanagement.response.ApiErrorResponse;
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ApiErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ApiErrorResponse("USER_NOT_FOUND", e.getMessage());
    }
}
