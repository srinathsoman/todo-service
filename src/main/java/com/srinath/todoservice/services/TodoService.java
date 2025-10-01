package com.srinath.todoservice.services;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.requests.CreateTodoRequest;

public interface TodoService {

    TodoDetails createTodoItem(CreateTodoRequest createTodoRequest);
}
