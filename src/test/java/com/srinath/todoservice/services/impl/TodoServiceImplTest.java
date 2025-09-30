package com.srinath.todoservice.services.impl;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.entities.Todo;
import com.srinath.todoservice.enums.TodoStatus;
import com.srinath.todoservice.repositories.TodoRepository;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.services.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo todo;
    private CreateTodoRequest createRequest;
    private LocalDateTime now;
    private LocalDateTime futureDate;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        futureDate = now.plusDays(1);
        todo = Todo.builder()
                .id(UUID.randomUUID())
                .description("Test Description")
                .createdAt(now)
                .dueDate(futureDate)
                .build();

        createRequest = new CreateTodoRequest("Test Description", futureDate);
    }

    @Test
    void testCreateTodo_withValidRequest_returnsTodoDetails() {
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        TodoDetails response = todoService.createTodoItem(createRequest);

        assertNotNull(response);
        assertEquals(todo.getId(), response.id());
        assertEquals(todo.getDescription(), response.description());
        assertEquals(todo.getStatus().toString(), response.status());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void testCreateTodo_shouldThrowException_whenDescriptionIsEmpty() {
        CreateTodoRequest invalidRequest = new CreateTodoRequest(
                "",  futureDate);

        assertThrows(IllegalArgumentException.class, () -> {
            todoService.createTodoItem(invalidRequest);
        });
    }

    @Test
    void testCreateTodo_shouldThrowException_whenDueDateIsPast() {
        LocalDateTime pastDate = now.minusDays(1);
        CreateTodoRequest invalidRequest = new CreateTodoRequest(
                "Valid description", pastDate);

        assertThrows(IllegalArgumentException.class, () -> {
            todoService.createTodoItem(invalidRequest);
        });
    }
}
