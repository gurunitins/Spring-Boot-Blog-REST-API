package com.sopromadze.blogapi.infrastructure.rest.mapper;

import com.sopromadze.blogapi.domain.model.Todo;
import com.sopromadze.blogapi.infrastructure.rest.payload.request.TodoPostRequestDto;
import com.sopromadze.blogapi.infrastructure.rest.payload.response.TodoResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    Todo toDomain(TodoPostRequestDto todoPostRequestDto);
    
    TodoResponseDto toResponseDto(Todo todo);

}
