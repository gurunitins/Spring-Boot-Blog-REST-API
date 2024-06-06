package com.sopromadze.blogapi.service.impl;

import com.sopromadze.blogapi.exception.BadRequestException;
import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.exception.UnauthorizedException;
import com.sopromadze.blogapi.infrastructure.persistence.postgresql.entity.TodoEntity;
import com.sopromadze.blogapi.infrastructure.persistence.postgresql.repository.TodoRepository;
import com.sopromadze.blogapi.model.user.UserEntity;
import com.sopromadze.blogapi.payload.ApiResponse;
import com.sopromadze.blogapi.payload.PagedResponse;
import com.sopromadze.blogapi.repository.UserRepository;
import com.sopromadze.blogapi.security.UserPrincipal;
import com.sopromadze.blogapi.service.TodoService;
import com.sopromadze.blogapi.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.sopromadze.blogapi.utils.AppConstants.*;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TodoEntity completeTodo(Long id, UserPrincipal currentUser) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));

        UserEntity userEntity = userRepository.getUser(currentUser);

        if (todoEntity.getUser().getId().equals(userEntity.getId())) {
            todoEntity.setCompleted(Boolean.TRUE);
            return todoRepository.save(todoEntity);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public TodoEntity unCompleteTodo(Long id, UserPrincipal currentUser) {
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));
        UserEntity userEntity = userRepository.getUser(currentUser);
        if (todoEntity.getUser().getId().equals(userEntity.getId())) {
            todoEntity.setCompleted(Boolean.FALSE);
            return todoRepository.save(todoEntity);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public PagedResponse<TodoEntity> getAllTodos(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<TodoEntity> todos = todoRepository.findByCreatedBy(currentUser.getId(), pageable);

        List<TodoEntity> content = todos.getNumberOfElements() == 0 ? Collections.emptyList() : todos.getContent();

        return new PagedResponse<>(content, todos.getNumber(), todos.getSize(), todos.getTotalElements(),
                todos.getTotalPages(), todos.isLast());
    }

    @Override
    public TodoEntity addTodo(TodoEntity todoEntity, String username) {
        userRepository.findByUsername(username)
                .ifPresent(todoEntity::setUser);
        return todoRepository.save(todoEntity);
    }

    @Override
    public TodoEntity getTodo(Long id, UserPrincipal currentUser) {
        UserEntity userEntity = userRepository.getUser(currentUser);
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));

        if (todoEntity.getUser().getId().equals(userEntity.getId())) {
            return todoEntity;
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public TodoEntity updateTodo(Long id, TodoEntity newTodoEntity, UserPrincipal currentUser) {
        UserEntity userEntity = userRepository.getUser(currentUser);
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));
        if (todoEntity.getUser().getId().equals(userEntity.getId())) {
            todoEntity.setTitle(newTodoEntity.getTitle());
            todoEntity.setCompleted(newTodoEntity.isCompleted());
            return todoRepository.save(todoEntity);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse deleteTodo(Long id, UserPrincipal currentUser) {
        UserEntity userEntity = userRepository.getUser(currentUser);
        TodoEntity todoEntity = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(TODO, ID, id));

        if (todoEntity.getUser().getId().equals(userEntity.getId())) {
            todoRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "You successfully deleted todo");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, YOU_DON_T_HAVE_PERMISSION_TO_MAKE_THIS_OPERATION);

        throw new UnauthorizedException(apiResponse);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size < 0) {
            throw new BadRequestException("Size number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

}
