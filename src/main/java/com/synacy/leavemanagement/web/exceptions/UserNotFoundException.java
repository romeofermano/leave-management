package com.synacy.leavemanagement.web.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String e) {
        super(e);
    }
}
