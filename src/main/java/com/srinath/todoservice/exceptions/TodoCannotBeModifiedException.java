package com.srinath.todoservice.exceptions;

import com.srinath.todoservice.exceptions.statuscodes.StatusCodes;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TodoCannotBeModifiedException extends ApiException{

    public TodoCannotBeModifiedException(String statusCode, String statusDescription) {
        super(statusCode, statusDescription);
    }

    public TodoCannotBeModifiedException(StatusCodes statusCode) {
        this(statusCode.getStatusCode(), statusCode.getStatusDescription());
    }
}
