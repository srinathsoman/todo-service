package com.srinath.todoservice.exceptions.handlers;

import com.srinath.todoservice.exceptions.InvalidParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@ControllerAdvice()
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
public class ExceptionHandlerAdvice {

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Object> handleInvalidParameterException(
            InvalidParameterException invalidParameterException) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(
                        buildBody(InvalidParameterException.class.getSimpleName(),
                                invalidParameterException.getStatusCode(),
                                invalidParameterException.getStatusDescription()));
    }

    private String buildBody(String title, String statusCode, String description) {
        return "{\n" + "\"title\" : \"" + title + "\" ,\n" + "\"status\" : \"" + statusCode + "\",\n"
                + "\"detail\" : \"" + description + "\"\n" + "}";
    }
}
