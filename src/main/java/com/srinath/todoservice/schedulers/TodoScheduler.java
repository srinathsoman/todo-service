package com.srinath.todoservice.schedulers;

import com.srinath.todoservice.services.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TodoScheduler {

    private final TodoService todoService;

    @Scheduled(fixedRate = 60000) // Run every 60 seconds
    public void updatePastDueTodos() {
        try {
            log.debug("Checking for past due todos...");
            todoService.updatePastDueTodos();
            log.debug("Past due todos check completed");
        } catch (Exception e) {
            log.error("Error occurred while updating past due todos", e);
        }
    }
}
