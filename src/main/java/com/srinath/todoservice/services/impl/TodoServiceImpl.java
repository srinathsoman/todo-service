package com.srinath.todoservice.services.impl;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.entities.Todo;
import com.srinath.todoservice.enums.TodoStatus;
import com.srinath.todoservice.exceptions.InvalidParameterException;
import com.srinath.todoservice.exceptions.TodoCannotBeModifiedException;
import com.srinath.todoservice.exceptions.TodoNotFoundException;
import com.srinath.todoservice.exceptions.statuscodes.StatusCodes;
import com.srinath.todoservice.repositories.TodoRepository;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.requests.UpdateTodoRequest;
import com.srinath.todoservice.services.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Override
    public TodoDetails createTodoItem(CreateTodoRequest createTodoRequest) {
        validateCreateRequest(createTodoRequest);
        Todo newTodo= Todo.builder()
                .description(createTodoRequest.getDescription())
                .createdAt(LocalDateTime.now())
                .status(TodoStatus.NOT_DONE)
                .dueDate(createTodoRequest.getDueDate())
                .build();
        newTodo =todoRepository.save(newTodo);
        return TodoDetails.fromEntity(newTodo);
    }

    @Override
    public TodoDetails updateTodoDescription(UUID id, UpdateTodoRequest updateTodoRequest) {
        Todo currentTodo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(StatusCodes.TODO_NOT_FOUND));
        if(isPastDue(currentTodo)){
            throw new TodoCannotBeModifiedException(StatusCodes.PAST_TODO_CONNOT_MODIFY);
        }
        currentTodo.setDescription(updateTodoRequest.getDescription());
        currentTodo = todoRepository.save(currentTodo);
        return TodoDetails.fromEntity(currentTodo);
    }

    private void validateCreateRequest(CreateTodoRequest createTodoRequest){
        if (createTodoRequest.getDescription() == null ||
                createTodoRequest.getDescription().trim().isEmpty()) {
            throw new InvalidParameterException(StatusCodes.DESCRIPTION_NOT_EMPTY);
        }

        if (createTodoRequest.getDueDate() == null ||
                createTodoRequest.getDueDate().isBefore(LocalDateTime.now())) {
            throw new InvalidParameterException(StatusCodes.DUE_DATE_CANNOT_BE_PAST);
        }
    }

    private boolean isPastDue(Todo todo) {
        return todo.getStatus() == TodoStatus.PAST_DUE ||
                todo.getDueDate().isBefore(LocalDateTime.now());
    }
}
