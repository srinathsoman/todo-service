package com.srinath.todoservice.entities;

import com.srinath.todoservice.enums.TodoStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
public class Todo {

    @Id
    @UuidGenerator
    private UUID id;
    private String description;
    private TodoStatus status = TodoStatus.NOT_DONE;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private LocalDateTime completedAt;
}
