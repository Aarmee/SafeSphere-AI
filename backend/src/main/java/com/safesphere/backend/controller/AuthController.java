package com.safesphere.backend.controller;

import com.safesphere.backend.dto.*;
import com.safesphere.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.safesphere.backend.dto.RefreshTokenRequest;
import com.safesphere.backend.dto.RefreshTokenResponse;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){

        this.userService = userService;

    }

    @PostMapping("/register")
    public ApiResponse<String> register(

            @Valid
            @RequestBody
            RegisterRequest request){

        return userService.registerUser(request);

    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(

            @Valid
            @RequestBody
            LoginRequest request){

        return userService.login(request);

    }

    @PostMapping("/refresh-token")
    public ApiResponse<RefreshTokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        return userService.refreshToken(request);

    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(
            Authentication authentication) {

        return userService.logout(authentication);

    }

}