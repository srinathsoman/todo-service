package com.srinath.todoservice.exceptions;

import com.srinath.todoservice.exceptions.statuscodes.StatusCodes;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidParameterException extends ApiException{

    public InvalidParameterException(String statusCode, String statusDescription) {
        super(statusCode, statusDescription);
    }

    public InvalidParameterException(StatusCodes statusCode) {
        this(statusCode.getStatusCode(), statusCode.getStatusDescription());
    }
}
