package com.safesphere.backend.service;

import com.safesphere.backend.dto.ApiResponse;
import com.safesphere.backend.dto.LoginRequest;
import com.safesphere.backend.dto.LoginResponse;
import com.safesphere.backend.dto.RegisterRequest;
import com.safesphere.backend.entity.User;
import com.safesphere.backend.exception.EmailAlreadyExistsException;
import com.safesphere.backend.repository.UserRepository;
import com.safesphere.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public UserService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService){

        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ApiResponse<String> registerUser(RegisterRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user=User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return new ApiResponse<>(
                true,
                "User Registered Successfully",
                null
        );

    }
    public ApiResponse<LoginResponse> login(LoginRequest request){

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),
                        request.getPassword()

                )

        );

        String jwt = jwtService.generateToken(request.getEmail());

        return new ApiResponse<>(

                true,

                "Login Successful",

                new LoginResponse(jwt)

        );

    }
}