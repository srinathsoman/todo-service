package com.srinath.todoservice.repositories;

import com.srinath.todoservice.entities.Todo;
import com.srinath.todoservice.enums.TodoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TodoRepository  extends JpaRepository<Todo, UUID> {

    List<Todo> findAllByStatusOrderByDueDateAsc(TodoStatus status);
    List<Todo> findAllByOrderByDueDateAsc();
}
