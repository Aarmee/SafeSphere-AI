package com.safesphere.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> dashboard() {

        return Map.of(
                "message", "Welcome Admin",
                "status", "Authorized"
        );

    }
}