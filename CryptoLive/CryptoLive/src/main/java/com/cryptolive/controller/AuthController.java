package com.cryptolive.controller;

import com.cryptolive.DTO.requests.LoginRequest;
import com.cryptolive.DTO.requests.RegisterRequest;
import com.cryptolive.service.AuthService;
import com.cryptolive.execption.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String jwt = authService.login(request.email, request.password);
            return ResponseEntity.ok().body(new JwtResponse(jwt));
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ErrorResponse("Error de autenticación"));
        }
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("registerRequest") RegisterRequest request, Model model) {
        try {
            authService.register(request.email, request.password);
            model.addAttribute("message", "Usuario registrado correctamente");
            return "redirect:/login";
        } catch (UserAlreadyExistsException | InvalidEmailException | WeakPasswordException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<?> registerApi(@RequestBody RegisterRequest request) {
        try {
            authService.register(request.email, request.password);
            return ResponseEntity.ok().body(new MessageResponse("Usuario registrado correctamente"));
        } catch (UserAlreadyExistsException | InvalidEmailException | WeakPasswordException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}

class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}

class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}