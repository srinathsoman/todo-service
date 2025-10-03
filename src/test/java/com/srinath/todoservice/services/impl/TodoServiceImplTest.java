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
import java.util.*;

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
        //Arange
        todo.setStatus(TodoStatus.PAST_DUE);
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));

        //Act and Assert
        assertThrows(TodoCannotBeModifiedException.class,
                () -> todoService.markTodoAsDone(todo.getId()));
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void testMarkTodoAsNotDone_Success() {
        //Arrange
        todo.setStatus(TodoStatus.DONE);
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        //Act
        TodoDetails response = todoService.markTodoAsNotDone(todo.getId());
        //Assert
        assertNotNull(response);
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository).save(todo);
    }

    @Test
    void testMarkTodoAsNotDone_CannotBeModified() {
        //Arrange
        todo.setStatus(TodoStatus.PAST_DUE);
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.of(todo));
        //Act and Assert
        assertThrows(TodoCannotBeModifiedException.class,
                () -> todoService.markTodoAsNotDone(todo.getId()));
        verify(todoRepository).findById(todo.getId());
        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void testGetAllTodos_IncludeAllFalse() {
        //Arrange
        List<Todo> todos = Collections.singletonList(todo);
        when(todoRepository.findAllByStatusOrderByDueDateAsc(TodoStatus.NOT_DONE)).thenReturn(todos);
        //Act
        List<TodoDetails> response = todoService.getAllTodos(false);
        //Assert
        assertEquals(1, response.size());
        assertEquals(todo.getId(), response.get(0).id());
        verify(todoRepository).findAllByStatusOrderByDueDateAsc(TodoStatus.NOT_DONE);
    }

    @Test
    void testGetAllTodos_IncludeAllTrue() {
        //Arrange
        List<Todo> todos = Collections.singletonList(todo);
        when(todoRepository.findAllByOrderByDueDateAsc()).thenReturn(todos);
        //Act
        List<TodoDetails> response = todoService.getAllTodos(true);
        //Assert
        assertEquals(1, response.size());
        assertEquals(todo.getId(), response.get(0).id());
        verify(todoRepository).findAllByOrderByDueDateAsc();
    }

    @Test
    void testGetTodoById_Success() {
        //Arrange
        when(todoRepository.findById(todo.getId())).thenReturn(Optional.of(todo));
        //Act
        TodoDetails response = todoService.getTodoById(todo.getId());
        //Assert
        assertNotNull(response);
        assertEquals(todo.getId(), response.id());
        verify(todoRepository).findById(todo.getId());
    }

    @Test
    void testGetTodoById_NotFound() {
        //Arrange
        when(todoRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        //Act and Assert
        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(UUID.randomUUID()));
        verify(todoRepository).findById(any(UUID.class));
    }

    @Test
    void testUpdatePastDueTodos_WithTodosToUpdate() {

        todo.setDueDate(LocalDateTime.now().minusDays(1));
        List<Todo> pastDueTodos = Collections.singletonList(todo);
        Todo updatedPastDueToDo=Todo.builder()
                .id(UUID.randomUUID())
                .description("Test Description")
                .status(TodoStatus.PAST_DUE)
                .createdAt(now)
                .dueDate(LocalDateTime.now().minusDays(1))
                .build();
        List<Todo> updatedPastDueTodos = Collections.singletonList(updatedPastDueToDo);
        when(todoRepository.findAllByDueDateBeforeAndStatusNot(any(LocalDateTime.class),
                any(TodoStatus.class))).thenReturn(pastDueTodos);
        when(todoRepository.saveAll(anyList())).thenReturn(updatedPastDueTodos);

        todoService.updatePastDueTodos();

        assertEquals(TodoStatus.PAST_DUE, updatedPastDueToDo.getStatus());
        verify(todoRepository).findAllByDueDateBeforeAndStatusNot(any(LocalDateTime.class),
                any(TodoStatus.class));
        verify(todoRepository).saveAll(pastDueTodos);
    }

    @Test
    void testUpdatePastDueTodos_WithNoTodosToUpdateShouldRunWithoutError() {

        List<Todo> pastDueTodos = List.of();

        when(todoRepository.findAllByDueDateBeforeAndStatusNot(any(LocalDateTime.class),
                any(TodoStatus.class))).thenReturn(pastDueTodos);
        when(todoRepository.saveAll(anyList())).thenReturn(pastDueTodos);

        todoService.updatePastDueTodos();

        verify(todoRepository).findAllByDueDateBeforeAndStatusNot(any(LocalDateTime.class),
                any(TodoStatus.class));
        verify(todoRepository).saveAll(pastDueTodos);
    }

}
