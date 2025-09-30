package com.srinath.todoservice.controllers;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.requests.CreateTodoRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/todo")
public class TodoController {

    @PostMapping
    public TodoDetails createTodoItem(@RequestBody CreateTodoRequest createTodoRequest){
        return null;
    }
}
