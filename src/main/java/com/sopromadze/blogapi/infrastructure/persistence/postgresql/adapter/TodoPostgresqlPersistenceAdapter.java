package com.sopromadze.blogapi.infrastructure.persistence.postgresql.adapter;

import com.sopromadze.blogapi.domain.model.Todo;
import com.sopromadze.blogapi.domain.port.TodoPersistencePort;
import com.sopromadze.blogapi.infrastructure.persistence.postgresql.entity.TodoEntity;
import com.sopromadze.blogapi.infrastructure.persistence.postgresql.mapper.TodoEntityMapper;
import com.sopromadze.blogapi.infrastructure.persistence.postgresql.repository.TodoRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodoPostgresqlPersistenceAdapter implements TodoPersistencePort {

    private final TodoEntityMapper todoEntityMapper;

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    @Override
    public Todo insert(Todo todo, String creatorUsername) {
        TodoEntity todoEntity = todoEntityMapper.toEntity(todo);
        userRepository.findByUsername(creatorUsername)
                .ifPresent(todoEntity::setUser);
        TodoEntity savedTodo = todoRepository.save(todoEntity);
        return todoEntityMapper.toDomain(savedTodo);
    }

}

