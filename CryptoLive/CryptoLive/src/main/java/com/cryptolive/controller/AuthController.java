// src/main/java/com/cryptolive/controller/AuthController.java
package com.cryptolive.controller;

import com.cryptolive.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            String jwt = authService.login(email, password);
            return ResponseEntity.ok().body(new JwtResponse(jwt));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String email, @RequestParam String password) {
        try {
            authService.register(email, password);
            return ResponseEntity.ok().body(new MessageResponse("Usuario registrado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    // Clases DTO para las respuestas
    static class JwtResponse {
        public String token;
        public JwtResponse(String token) { this.token = token; }
    }
    static class MessageResponse {
        public String message;
        public MessageResponse(String message) { this.message = message; }
    }
    static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}