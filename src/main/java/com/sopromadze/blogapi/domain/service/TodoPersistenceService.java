package com.sopromadze.blogapi.domain.service;

import com.sopromadze.blogapi.domain.model.Todo;
import com.sopromadze.blogapi.domain.port.TodoPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoPersistenceService {

    private final TodoPersistencePort todoPersistencePort;

    public Todo createTodo(Todo todo, String creatorUsername) {
        log.debug("[START insert] todo: {} creatorUsername: {}", todo, creatorUsername);
        Todo response = todoPersistencePort.insert(todo, creatorUsername);
        log.debug("[STOP insert] inserted: {}", response);
        return response;
    }

}
