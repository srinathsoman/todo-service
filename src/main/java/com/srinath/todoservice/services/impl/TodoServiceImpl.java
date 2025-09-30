package com.srinath.todoservice.services.impl;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.services.TodoService;
import org.springframework.stereotype.Service;

@Service
public class TodoServiceImpl implements TodoService {

    @Override
    public TodoDetails createTodoItem(CreateTodoRequest createTodoRequest) {
        return null;
    }
}
