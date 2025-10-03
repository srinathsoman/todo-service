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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        Todo currentTodo = verifyAndGetTodoItemForUpdate(id);
        currentTodo.setDescription(updateTodoRequest.getDescription());
        currentTodo = todoRepository.save(currentTodo);
        return TodoDetails.fromEntity(currentTodo);
    }

    @Override
    public TodoDetails markTodoAsDone(UUID id) {
        Todo currentTodo = verifyAndGetTodoItemForUpdate(id);
        if(!currentTodo.getStatus().equals(TodoStatus.DONE)){
            currentTodo.setStatus(TodoStatus.DONE);
            currentTodo.setCompletedAt(LocalDateTime.now());
            currentTodo = todoRepository.save(currentTodo);
        }
        //If item is already in desired state. Return gracefully without throwing error
        return TodoDetails.fromEntity(currentTodo);
    }

    @Override
    public TodoDetails markTodoAsNotDone(UUID id) {
        Todo currentTodo = verifyAndGetTodoItemForUpdate(id);
        if(!currentTodo.getStatus().equals(TodoStatus.NOT_DONE)){
            currentTodo.setStatus(TodoStatus.NOT_DONE);
            currentTodo.setCompletedAt(null);
            currentTodo = todoRepository.save(currentTodo);
        }
        //If item is already in desired state. Return gracefully without throwing error
        return TodoDetails.fromEntity(currentTodo);
    }

    @Override
    public List<TodoDetails> getAllTodos(Boolean includeAll) {
        if(Boolean.TRUE.equals(includeAll)){
            return todoRepository.findAllByOrderByDueDateAsc().stream().map(TodoDetails::fromEntity).toList();
        }
        return todoRepository.findAllByStatusOrderByDueDateAsc(TodoStatus.NOT_DONE)
                .stream().map(TodoDetails::fromEntity).toList();
    }

    @Override
    public TodoDetails getTodoById(UUID id) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isEmpty()){
            throw new TodoNotFoundException(StatusCodes.TODO_NOT_FOUND);
        }
        return TodoDetails.fromEntity(todoOptional.get());
    }

    @Override
    public void updatePastDueTodos() {
        List<Todo> pastDueTodos = todoRepository.findAllByDueDateBeforeAndStatusNot(
                LocalDateTime.now(),TodoStatus.PAST_DUE);
        pastDueTodos.forEach(todo -> todo.setStatus(TodoStatus.PAST_DUE));
        todoRepository.saveAll(pastDueTodos);
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

    private Todo verifyAndGetTodoItemForUpdate(UUID id){
        Todo currentTodo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(StatusCodes.TODO_NOT_FOUND));
        if(isPastDue(currentTodo)){
            throw new TodoCannotBeModifiedException(StatusCodes.PAST_TODO_CONNOT_MODIFY);
        }
        return currentTodo;
    }
}
