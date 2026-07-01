package com.safesphere.backend.service;

import com.safesphere.backend.dto.ApiResponse;
import com.safesphere.backend.dto.LoginRequest;
import com.safesphere.backend.dto.LoginResponse;
import com.safesphere.backend.dto.RegisterRequest;
import com.safesphere.backend.entity.Role;
import com.safesphere.backend.entity.User;
import com.safesphere.backend.exception.EmailAlreadyExistsException;
import com.safesphere.backend.repository.UserRepository;
import com.safesphere.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.time.LocalDateTime;
import com.safesphere.backend.dto.RefreshTokenRequest;
import com.safesphere.backend.dto.RefreshTokenResponse;
import com.safesphere.backend.entity.RefreshToken;
import org.springframework.security.core.Authentication;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    public UserService(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            RefreshTokenService refreshTokenService,
            JwtService jwtService) {

        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
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
                .role(Role.USER)
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
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String accessToken = jwtService.generateToken(user.getEmail());

        String refreshToken = refreshTokenService
                .createRefreshToken(user)
                .getToken();

        return new ApiResponse<>(
                true,
                "Login Successful",
                new LoginResponse(
                        accessToken,
                        refreshToken
                )
        );

    }
    public ApiResponse<RefreshTokenResponse> refreshToken(
            RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService
                .verifyRefreshToken(request.getRefreshToken());

        String accessToken = jwtService.generateToken(
                refreshToken.getUser().getEmail());

        return new ApiResponse<>(

                true,

                "Access Token Generated",

                new RefreshTokenResponse(accessToken)

        );

    }
    public ApiResponse<String> logout(
            Authentication authentication) {

        User user = userRepository

                .findByEmail(authentication.getName())

                .orElseThrow();

        refreshTokenService.deleteRefreshToken(user);

        return new ApiResponse<>(

                true,

                "Logout Successful",

                null

        );

    }
}