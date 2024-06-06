package com.sopromadze.blogapi.repository;

import com.sopromadze.blogapi.exception.ResourceNotFoundException;
import com.sopromadze.blogapi.model.user.UserEntity;
import com.sopromadze.blogapi.security.UserPrincipal;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(@NotBlank String username);

    Optional<UserEntity> findByEmail(@NotBlank String email);

    Boolean existsByUsername(@NotBlank String username);

    Boolean existsByEmail(@NotBlank String email);

    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    default UserEntity getUser(UserPrincipal currentUser) {
        return getUserByName(currentUser.getUsername());
    }

    default UserEntity getUserByName(String username) {
        return findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

}
