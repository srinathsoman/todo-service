package com.srinath.todoservice.exceptions.statuscodes;

import lombok.Getter;

@Getter
public enum StatusCodes {

    DESCRIPTION_NOT_EMPTY("1001", "Description must not be empty"),
    DUE_DATE_CANNOT_BE_PAST("1002", "Due date must not be in the past"),
    TODO_NOT_FOUND("2001", "Todo item with given id not found"),
    PAST_TODO_CONNOT_MODIFY("2002" , "Past due todo items cannot be modified"),
    ;

    private final String statusCode;
    private final String statusDescription;

    StatusCodes(String statusCode, String statusDescription) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
    }
}
