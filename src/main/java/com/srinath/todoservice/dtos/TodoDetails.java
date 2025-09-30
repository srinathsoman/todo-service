package com.srinath.todoservice.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record TodoDetails(UUID id, String description, String status, LocalDateTime createdAt,
                          LocalDateTime dueDate, LocalDateTime completedAt) {
}
