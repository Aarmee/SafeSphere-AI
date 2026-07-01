package com.safesphere.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message="Full Name is required")
    private String fullName;

    @Email(message="Invalid Email")
    @NotBlank(message="Email is required")
    private String email;

    @Size(min=6,message="Password must contain at least 6 characters")
    private String password;

}