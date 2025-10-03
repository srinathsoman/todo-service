package com.srinath.todoservice.exceptions;

import com.srinath.todoservice.exceptions.statuscodes.StatusCodes;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TodoNotFoundException extends ApiException{

    public TodoNotFoundException(String statusCode, String statusDescription) {
        super(statusCode, statusDescription);
    }

    public TodoNotFoundException(StatusCodes statusCode) {
        this(statusCode.getStatusCode(), statusCode.getStatusDescription());
    }
}
