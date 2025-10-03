package com.srinath.todoservice.services.impl;

import com.srinath.todoservice.dtos.TodoDetails;
import com.srinath.todoservice.entities.Todo;
import com.srinath.todoservice.enums.TodoStatus;
import com.srinath.todoservice.exceptions.InvalidParameterException;
import com.srinath.todoservice.exceptions.TodoCannotBeModifiedException;
import com.srinath.todoservice.exceptions.TodoNotFoundException;
import com.srinath.todoservice.repositories.TodoRepository;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.requests.UpdateTodoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoServiceImpl todoService;

    private Todo todo;
    private CreateTodoRequest createRequest;
    private UpdateTodoRequest updateTodoRequest;
    private LocalDateTime now;
    private LocalDateTime futureDate;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        futureDate = now.plusDays(1);
        todo = Todo.builder()
                .id(UUID.randomUUID())
                .description("Test Description")
                .status(TodoStatus.NOT_DONE)
                .createdAt(now)
                .dueDate(futureDate)
                .build();

        createRequest = new CreateTodoRequest("Test Description", futureDate);
        updateTodoRequest =new UpdateTodoRequest("Updated Description");
    }

    @Test
    void testCreateTodo_withValidRequest_returnsTodoDetails() {
        //Arrange
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        //Act
        TodoDetails response = todoService.createTodoItem(createRequest);

        //Assert
        assertNotNull(response);
        assertEquals(todo.getId(), response.id());
        assertEquals(todo.getDescription(), response.description());
        assertEquals(todo.getStatus().toString(), response.status());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void testCreateTodo_shouldThrowException_whenDescriptionIsEmpty() {
        //Arrange
        CreateTodoRequest invalidRequest = new CreateTodoRequest(
                "",  futureDate);
        //Act and Assert
        assertThrows(InvalidParameterException.class, () -> {
            todoService.createTodoItem(invalidRequest);
        });
    }

    @Test
    void testCreateTodo_shouldThrowException_whenDueDateIsPast() {
        //Arrange
        LocalDateTime pastDate = now.minusDays(1);
        CreateTodoRequest invalidRequest = new CreateTodoRequest(
                "Valid description", pastDate);

        //Act and Assert
        assertThrows(InvalidParameterException.class, () -> {
            todoService.createTodoItem(invalidRequest);
        });
    }

    @Test
    void testUpdateTodoDescription_Success() {

        //Arrange
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        //Act
        TodoDetails response = todoService.updateTodoDescription(todo.getId(), updateTodoRequest);

        //Assert
        assertNotNull(response);
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository).save(todo);
    }

    @Test
    void testUpdateTodoDescription_CannotBeModified() {
        //Arrange
        todo.setStatus(TodoStatus.PAST_DUE);
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));

        //Act and Assert
        assertThrows(TodoCannotBeModifiedException.class,
                () -> todoService.updateTodoDescription(todo.getId(), updateTodoRequest));
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void testUpdateTodoDescription_TodoNotFound() {
        //Arrange
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(TodoNotFoundException.class,
                () -> todoService.updateTodoDescription(todo.getId(), updateTodoRequest));
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void testMarkTodoAsDone_Success() {
        //Arrange
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        //Act
        TodoDetails response = todoService.markTodoAsDone(todo.getId());
        //Assert
        assertNotNull(response);
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository).save(todo);
    }

    @Test
    void testMarkTodoAsDone_CannotBeModified() {
        todo.setStatus(TodoStatus.PAST_DUE);
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));

        assertThrows(TodoCannotBeModifiedException.class,
                () -> todoService.markTodoAsDone(todo.getId()));
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void testMarkTodoAsNotDone_Success() {
        todo.setStatus(TodoStatus.DONE);
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        TodoDetails response = todoService.markTodoAsNotDone(todo.getId());

        assertNotNull(response);
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository).save(todo);
    }

    @Test
    void testMarkTodoAsNotDone_CannotBeModified() {
        todo.setStatus(TodoStatus.PAST_DUE);
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));

        assertThrows(TodoCannotBeModifiedException.class,
                () -> todoService.markTodoAsNotDone(todo.getId()));
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository, never()).save(any(Todo.class));
    }

}
