package com.sopromadze.blogapi.controller;

import com.sopromadze.blogapi.model.user.UserEntity;
import com.sopromadze.blogapi.payload.LoginResponse;
import com.sopromadze.blogapi.payload.LoginUserDto;
import com.sopromadze.blogapi.payload.RegisterUserDto;
import com.sopromadze.blogapi.service.AuthenticationService;
import com.sopromadze.blogapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterUserDto registerUserDto) {
        UserEntity registeredUserEntity = authenticationService.signup(registerUserDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registeredUserEntity);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        UserEntity authenticatedUserEntity = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUserEntity.getUsername());

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(loginResponse);
    }

}
