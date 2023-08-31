package com.synacy.leavemanagement.web.exceptions;

import com.synacy.leavemanagement.web.exceptions.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAdminException.class)
    public ApiErrorResponse handleInvalidAdminException(InvalidAdminException e) {
        return new ApiErrorResponse("INVALID_RIGHTS", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidManagerException.class)
    public ApiErrorResponse handleInvalidManagerException(InvalidManagerException e) {
        return new ApiErrorResponse("INVALID_MANAGER", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPaginationException.class)
    public ApiErrorResponse handleInvalidPaginationException(InvalidPaginationException e) {
        return new ApiErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ApiErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ApiErrorResponse("USER_NOT_FOUND", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String errorMessage = fieldErrors.stream().map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ApiErrorResponse("VALIDATION_ERROR", errorMessage);
    }
}
