package com.synacy.leavemanagement.web.exceptions;

import lombok.AccessLevel;
import lombok.Getter;

public class InvalidPaginationException extends RuntimeException {

    @Getter(AccessLevel.PACKAGE)
    private final String errorCode;

    @Getter(AccessLevel.PACKAGE)
    private final String errorMessage;

    public InvalidPaginationException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}