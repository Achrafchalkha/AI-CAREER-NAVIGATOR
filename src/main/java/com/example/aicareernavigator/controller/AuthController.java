package com.example.aicareernavigator.controller;

import com.example.aicareernavigator.dto.AuthResponse;
import com.example.aicareernavigator.dto.LoginRequest;
import com.example.aicareernavigator.dto.RegisterRequest;
import com.example.aicareernavigator.dto.MessageResponse;
import com.example.aicareernavigator.model.User;
import com.example.aicareernavigator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = authService.register(request);
            String token = authService.generateToken(user);
            
            return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getFirstName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = authService.authenticate(request.getEmail(), request.getPassword());
            String token = authService.generateToken(user);
            
            return ResponseEntity.ok(new AuthResponse(token, user.getEmail(), user.getFirstName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Authentication failed: " + e.getMessage()));
        }
    }
} 