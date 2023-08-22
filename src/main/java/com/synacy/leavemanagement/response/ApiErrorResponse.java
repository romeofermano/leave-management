package com.synacy.leavemanagement.response;

import lombok.Getter;

@Getter
public class ApiErrorResponse {
    private final String errorCode;
    private final String errorMessage;

    public ApiErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
