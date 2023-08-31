package com.synacy.leavemanagement.web.exceptions;

import lombok.AccessLevel;
import lombok.Getter;

public class DateException extends RuntimeException{

    public DateException(String e) {
    super(e);
}
}
