package com.srinath.todoservice.repositories;

import com.srinath.todoservice.entities.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TodoRepository  extends JpaRepository<Todo, UUID> {
}
