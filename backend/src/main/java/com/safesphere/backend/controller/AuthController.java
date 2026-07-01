package com.safesphere.backend.controller;

import com.safesphere.backend.dto.RegisterRequest;
import com.safesphere.backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService){
        this.userService=userService;
    }
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        return userService.registerUser(request);
    }

}
