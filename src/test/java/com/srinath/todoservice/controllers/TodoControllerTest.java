package com.srinath.todoservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srinath.todoservice.entities.Todo;
import com.srinath.todoservice.enums.TodoStatus;
import com.srinath.todoservice.repositories.TodoRepository;
import com.srinath.todoservice.requests.CreateTodoRequest;
import com.srinath.todoservice.requests.UpdateTodoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private LocalDateTime futureDate;
    private LocalDateTime pastDate;
    Todo testTodo;

    @BeforeEach
    void setUp() {

        // Clear database
        todoRepository.deleteAll();
        // Setup test data
        LocalDateTime now = LocalDateTime.now();
        futureDate = now.plusDays(1);
        pastDate = now.minusDays(1);

        testTodo = Todo.builder()
                .description("Test Description")
                .status(TodoStatus.NOT_DONE)
                .createdAt(now)
                .dueDate(futureDate)
                .build();
        testTodo = todoRepository.save(testTodo);

    }

    @Test
    void testCreateTodo_Success() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest("Test Description", futureDate);

        mockMvc.perform(post("/api/v1/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.status", is("not done")))
                .andExpect(jsonPath("$.dueDate", notNullValue()))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.completedAt", nullValue()));
    }

    @Test
    void testCreateTodo_InvalidRequest() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest("", pastDate); // Empty description and past date

        mockMvc.perform(post("/api/v1/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTodoDescription_Success() throws Exception {
        UpdateTodoRequest request = new UpdateTodoRequest("Updated description");

        mockMvc.perform(put("/api/v1/todo/{id}", testTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Updated description")));
    }

    @Test
    void testUpdateTodoDescription_PastDue() throws Exception {
        Todo pastDueTodo = Todo.builder()
                .description("Test Description")
                .status(TodoStatus.PAST_DUE)
                .createdAt(LocalDateTime.now().minusDays(2))
                .dueDate(LocalDateTime.now().minusDays(1))
                .build();
        pastDueTodo = todoRepository.save(pastDueTodo);

        UpdateTodoRequest request = new UpdateTodoRequest("Updated description");

        mockMvc.perform(put("/api/v1/todo/{id}", pastDueTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Past due")));
    }

    @Test
    void testUpdateTodoDescription_NotFound() throws Exception {

        UpdateTodoRequest request = new UpdateTodoRequest("Updated description");

        mockMvc.perform(put("/api/v1/todo/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testMarkTodoAsDone_Success() throws Exception {
        mockMvc.perform(patch("/api/v1/todo/{id}/done", testTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("done")))
                .andExpect(jsonPath("$.completedAt", notNullValue()));
    }

    @Test
    void testMarkTodoAsDone_PastDue() throws Exception {

        Todo pastDueTodo = Todo.builder()
                .description("Test Description")
                .status(TodoStatus.PAST_DUE)
                .createdAt(LocalDateTime.now().minusDays(1))
                .dueDate(futureDate)
                .build();
        pastDueTodo = todoRepository.save(pastDueTodo);

        mockMvc.perform(patch("/api/v1/todo/{id}/done", pastDueTodo.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Past due")));
    }

    @Test
    void testMarkTodoAsDone_NotFound() throws Exception {
        mockMvc.perform(patch("/api/v1/todo/{id}/done", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testMarkTodoAsNotDone_Success() throws Exception {

        testTodo.setStatus(TodoStatus.DONE);
        todoRepository.save(testTodo);

        mockMvc.perform(patch("/api/v1/todo/{id}/not-done", testTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("not done")))
                .andExpect(jsonPath("$.completedAt", nullValue()));
    }

    @Test
    void testMarkTodoAsNotDone_NotFound() throws Exception {
        mockMvc.perform(patch("/api/v1/todo/{id}/not-done", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testMarkTodoAsNotDone_PastDue() throws Exception {

        Todo pastDueTodo = Todo.builder()
                .description("Test Description")
                .status(TodoStatus.PAST_DUE)
                .createdAt(LocalDateTime.now().minusDays(1))
                .dueDate(futureDate)
                .build();
        pastDueTodo = todoRepository.save(pastDueTodo);

        mockMvc.perform(patch("/api/v1/todo/{id}/not-done", pastDueTodo.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Past due")));
    }

    @Test
    void testGetAllTodos_NotDoneOnly() throws Exception {

        Todo doneTodo = Todo.builder()
                .description("Test Description")
                .status(TodoStatus.DONE)
                .createdAt(LocalDateTime.now())
                .dueDate(futureDate)
                .build();
        todoRepository.save(doneTodo);
        testTodo = todoRepository.save(testTodo);

        mockMvc.perform(get("/api/v1/todo?includeAll=FALSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testTodo.getId().toString())))
                .andExpect(jsonPath("$[0].status", is("not done")));
    }

    @Test
    void testGetAllTodos_IncludeAll() throws Exception {

        Todo doneTodo = Todo.builder()
                .description("Test Description")
                .status(TodoStatus.DONE)
                .createdAt(LocalDateTime.now())
                .dueDate(futureDate)
                .build();
        doneTodo = todoRepository.save(doneTodo);
        testTodo = todoRepository.save(testTodo);

        mockMvc.perform(get("/api/v1/todo?includeAll=TRUE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetAllTodos_EmptyIncludeAll() throws Exception {

        Todo doneTodo = Todo.builder()
                .description("Test Description")
                .status(TodoStatus.DONE)
                .createdAt(LocalDateTime.now())
                .dueDate(futureDate)
                .build();
        todoRepository.save(doneTodo);
        testTodo = todoRepository.save(testTodo);

        mockMvc.perform(get("/api/v1/todo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testTodo.getId().toString())))
                .andExpect(jsonPath("$[0].status", is("not done")));
    }

}
