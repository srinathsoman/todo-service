package com.srinath.todoservice.exceptions.statuscodes;

import lombok.Getter;

@Getter
public enum StatusCodes {

    DESCRIPTION_NOT_EMPTY("1001", "Description must not be empty"),
    DUE_DATE_CANNOT_BE_PAST("1002", "Due date must not be in the past"),
    ;

    private final String statusCode;
    private final String statusDescription;

    StatusCodes(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }
}
