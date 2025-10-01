package com.srinath.todoservice.controllers;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.services.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDetails createTodoItem(@RequestBody @Valid CreateTodoRequest createTodoRequest){

        return todoService.createTodoItem(createTodoRequest);
    }
}
