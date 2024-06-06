package com.sopromadze.blogapi.infrastructure.persistence.postgresql.mapper;

import com.sopromadze.blogapi.domain.model.Todo;
import com.sopromadze.blogapi.infrastructure.persistence.postgresql.entity.TodoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoEntityMapper {

    TodoEntity toEntity(Todo todo);

    Todo toDomain(TodoEntity todoEntity);

}
