package com.srinath.todoservice.services.impl;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.entities.Todo;
import com.srinath.todoservice.enums.TodoStatus;
import com.srinath.todoservice.repositories.TodoRepository;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.services.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public TodoDetails createTodoItem(CreateTodoRequest createTodoRequest) {
        if (createTodoRequest.getDescription() == null ||
                createTodoRequest.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty");
        }

        if (createTodoRequest.getDueDate() == null ||
                createTodoRequest.getDueDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Due date must not be in the past");
        }
        Todo newTodo= Todo.builder()
                .description(createTodoRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .status(TodoStatus.NOT_DONE)
                .dueDate(createTodoRequest.getDueDate())
                .build();
        newTodo =todoRepository.save(newTodo);
        return TodoDetails.fromEntity(newTodo);
    }
}
