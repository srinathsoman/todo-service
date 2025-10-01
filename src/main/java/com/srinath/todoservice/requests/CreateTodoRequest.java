package com.srinath.todoservice.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTodoRequest {

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;
}
