package com.sopromadze.blogapi.application;

import com.sopromadze.blogapi.domain.model.Todo;
import com.sopromadze.blogapi.domain.service.TodoPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TodoUseCase {

    private final TodoPersistenceService todoPersistenceService;

    public Todo createTodo(Todo todo, String creatorUsername) {
        log.debug("[START insert] todo: {} creatorUsername: {}", todo, creatorUsername);
        Todo response = todoPersistenceService.createTodo(todo, creatorUsername);
        log.debug("[STOP insert] inserted: {}", response);
        return response;
    }

}
