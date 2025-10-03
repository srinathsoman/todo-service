package com.srinath.todoservice.controllers;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.requests.UpdateTodoRequest;
import com.srinath.todoservice.services.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    /**
     * Create a new todo item
     * POST api/v1/todo
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDetails createTodoItem(@RequestBody @Valid CreateTodoRequest createTodoRequest) {

        return todoService.createTodoItem(createTodoRequest);
    }

    /**
     * Update the description of a todo
     * PUT /api/v1/todo/{id}
     */
    @PutMapping("/{id}")
    public TodoDetails updateTodoDescription(@PathVariable UUID id,
                                             @RequestBody @Valid UpdateTodoRequest updateTodoRequest) {
        return todoService.updateTodoDescription(id, updateTodoRequest);
    }

    /**
     * Mark a todo as done
     * PATCH /api/v1/todo/{id}/done
     */
    @PatchMapping("/{id}/done")
    public TodoDetails markTodoAsDone(@PathVariable UUID id){
        return todoService.markTodoAsDone(id);
    }

    /**
     * Mark a todo as not done
     * PATCH /api/v1/todo/{id}/not-done
     */
    @PatchMapping("/{id}/not-done")
    public TodoDetails markTodoAsNotDone(@PathVariable UUID id){
        return todoService.markTodoAsNotDone(id);
    }

    /**
     * Get all todos with optional filtering
     * GET /api/v1/todo?includeAll=true
     */
    @GetMapping
    public List<TodoDetails> getAllTodos(@RequestParam(value = "includeAll" , required = false)
                                         Boolean includeAll){
        return todoService.getAllTodos(includeAll);
    }

    /**
     * Get a specific todo by ID
     * GET /api/v1/todo/{id}
     */
    @GetMapping("/{id}")
    public TodoDetails getTodoById(@PathVariable UUID id){
        return todoService.getTodoById(id);
    }
}
