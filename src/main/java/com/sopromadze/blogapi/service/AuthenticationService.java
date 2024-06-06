package com.sopromadze.blogapi.service;

import com.sopromadze.blogapi.model.role.Role;
import com.sopromadze.blogapi.model.role.RoleName;
import com.sopromadze.blogapi.model.user.UserEntity;
import com.sopromadze.blogapi.payload.LoginUserDto;
import com.sopromadze.blogapi.payload.RegisterUserDto;
import com.sopromadze.blogapi.repository.RoleRepository;
import com.sopromadze.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    public UserEntity signup(RegisterUserDto input) {

        List<RoleName> roleNames = new ArrayList<>(List.of(RoleName.ROLE_USER));

        if (userRepository.count() == 0) {
            roleNames.add(RoleName.ROLE_ADMIN);
        }

        List<Role> roles = roleNames.stream()
                .map(roleRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        UserEntity userEntity = UserEntity.builder()
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .username(input.getEmail())
                .roles(roles)
                .password(passwordEncoder.encode(input.getPassword()))
                .build();
        return userRepository.save(userEntity);

    }

    public UserEntity authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

}
