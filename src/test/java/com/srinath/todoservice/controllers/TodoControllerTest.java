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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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


}
