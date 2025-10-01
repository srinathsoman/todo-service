package com.srinath.todoservice.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiException extends RuntimeException {

    private String statusCode;
    private String statusDescription;
}

