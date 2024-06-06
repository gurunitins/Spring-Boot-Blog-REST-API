package com.sopromadze.blogapi.infrastructure.persistence.postgresql.repository;

import com.sopromadze.blogapi.infrastructure.persistence.postgresql.entity.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    Page<TodoEntity> findByCreatedBy(Long userId, Pageable pageable);

}
