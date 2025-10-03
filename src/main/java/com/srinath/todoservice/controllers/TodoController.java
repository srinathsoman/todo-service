package com.srinath.todoservice.controllers;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.requests.UpdateTodoRequest;
import com.srinath.todoservice.services.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDetails createTodoItem(@RequestBody @Valid CreateTodoRequest createTodoRequest) {

        return todoService.createTodoItem(createTodoRequest);
    }

    @PutMapping("/{id}")
    public TodoDetails updateTodoDescription(@PathVariable UUID id,
                                             @RequestBody @Valid UpdateTodoRequest updateTodoRequest) {
        return todoService.updateTodoDescription(id, updateTodoRequest);
    }

    @PatchMapping("/{id}/done")
    public TodoDetails markTodoAsDone(@PathVariable UUID id){
        return null;
    }

    @PatchMapping("/{id}/not-done")
    public TodoDetails markTodoAsNotDone(@PathVariable UUID id){
        return null;
    }
}
