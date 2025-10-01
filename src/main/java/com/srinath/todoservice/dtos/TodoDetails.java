package com.srinath.todoservice.dtos;

import com.srinath.todoservice.entities.Todo;

import java.time.LocalDateTime;
import java.util.UUID;

public record TodoDetails(UUID id, String description, String status, LocalDateTime createdAt,
                          LocalDateTime dueDate, LocalDateTime completedAt) {
    public static TodoDetails fromEntity(Todo todo) {
        return new TodoDetails(
                todo.getId(),
                todo.getDescription(),
                todo.getStatus().toString(),
                todo.getCreatedAt(),
                todo.getDueDate(),
                todo.getCompletedAt()
        );
    }
}
