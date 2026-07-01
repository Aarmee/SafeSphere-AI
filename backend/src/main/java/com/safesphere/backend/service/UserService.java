package com.safesphere.backend.service;

import com.safesphere.backend.dto.RegisterRequest;
import com.safesphere.backend.entity.User;
import com.safesphere.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public String registerUser(RegisterRequest request){
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return "User Registered Successfully";
    }

}
