package com.sopromadze.blogapi.infrastructure.rest.controller;

import com.sopromadze.blogapi.application.TodoUseCase;
import com.sopromadze.blogapi.domain.model.Todo;
import com.sopromadze.blogapi.infrastructure.persistence.postgresql.entity.TodoEntity;
import com.sopromadze.blogapi.infrastructure.rest.mapper.TodoMapper;
import com.sopromadze.blogapi.infrastructure.rest.payload.request.TodoPostRequestDto;
import com.sopromadze.blogapi.infrastructure.rest.payload.response.TodoResponseDto;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.security.CurrentUser;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.TodoService;
import com.sopromadze.blogapi.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    private final TodoMapper todoMapper;

    private final TodoUseCase todoUseCase;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PagedResponse<TodoEntity>> getAllTodos(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam(value = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) {

        PagedResponse<TodoEntity> response = todoService.getAllTodos(currentUser, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TodoResponseDto> addTodo(@Valid @RequestBody TodoPostRequestDto requestDto,
                                                   @AuthenticationPrincipal(expression = "username") String username) {
        Todo todo = todoMapper.toDomain(requestDto);
        Todo createdTodo = todoUseCase.createTodo(todo, username);
        TodoResponseDto responseDto = todoMapper.toResponseDto(createdTodo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TodoEntity> getTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {
        TodoEntity todoEntity = todoService.getTodo(id, currentUser);

        return new ResponseEntity<>(todoEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TodoEntity> updateTodo(@PathVariable(value = "id") Long id, @Valid @RequestBody TodoEntity newTodoEntity,
                                                 @CurrentUser UserPrincipal currentUser) {
        TodoEntity updatedTodoEntity = todoService.updateTodo(id, newTodoEntity, currentUser);

        return new ResponseEntity<>(updatedTodoEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse> deleteTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = todoService.deleteTodo(id, currentUser);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TodoEntity> completeTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {

        TodoEntity todoEntity = todoService.completeTodo(id, currentUser);

        return new ResponseEntity<>(todoEntity, HttpStatus.OK);
    }

    @PutMapping("/{id}/unComplete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TodoEntity> unCompleteTodo(@PathVariable(value = "id") Long id, @CurrentUser UserPrincipal currentUser) {

        TodoEntity todoEntity = todoService.unCompleteTodo(id, currentUser);

        return new ResponseEntity<>(todoEntity, HttpStatus.OK);
    }

}
