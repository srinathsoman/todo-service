package com.srinath.todoservice.requests;

import com.srinath.todoservice.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTodoRequest {

    private String description;
    private LocalDateTime dueDate;
}
