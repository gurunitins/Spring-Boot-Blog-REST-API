package com.sopromadze.blogapi.service;

import com.sopromadze.blogapi.infrastructure.persistence.postgresql.entity.TodoEntity;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.security.UserPrincipal;

public interface TodoService {

    TodoEntity completeTodo(Long id, UserPrincipal currentUser);

    TodoEntity unCompleteTodo(Long id, UserPrincipal currentUser);

    PagedResponse<TodoEntity> getAllTodos(UserPrincipal currentUser, int page, int size);

    TodoEntity addTodo(TodoEntity todoEntity, String username);

    TodoEntity getTodo(Long id, UserPrincipal currentUser);

    TodoEntity updateTodo(Long id, TodoEntity newTodoEntity, UserPrincipal currentUser);

    ApiResponse deleteTodo(Long id, UserPrincipal currentUser);

}
