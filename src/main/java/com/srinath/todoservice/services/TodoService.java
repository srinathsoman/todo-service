package com.srinath.todoservice.services;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.requests.UpdateTodoRequest;

import java.util.List;
import java.util.UUID;

public interface TodoService {

    TodoDetails createTodoItem(CreateTodoRequest createTodoRequest);
    TodoDetails updateTodoDescription(UUID id, UpdateTodoRequest updateTodoRequest);
    TodoDetails markTodoAsDone(UUID id);
    TodoDetails markTodoAsNotDone(UUID id);
    List<TodoDetails> getAllTodos(Boolean includeAll);
    TodoDetails getTodoById(UUID id);

}
