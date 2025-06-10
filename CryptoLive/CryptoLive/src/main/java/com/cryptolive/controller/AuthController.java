// src/main/java/com/cryptolive/controller/AuthController.java
package com.cryptolive.controller;

import com.cryptolive.service.AuthService;
import com.cryptolive.execption.*;
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
        } catch (UserNotFoundException | IncorrectPasswordException e) {
            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ErrorResponse("Error de autenticación"));
        }/*Un RespopnseEntity es una clse de spring que representa una repuesta HTTP completa
        el código de estado, los encabezados y el cuerpo de la respuesta. Permite controlar exactamente qué se
        devuelve al cliente desde un controlador REST,
        como el estado (por ejemplo, 200, 401, 400), los datos (en formato JSON, texto, etc.) */
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String email, @RequestParam String password) {
        try {
            authService.register(email, password);
            return ResponseEntity.ok().body(new MessageResponse("Usuario registrado correctamente"));
        } catch (UserAlreadyExistsException | InvalidEmailException | WeakPasswordException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Error en el registro"));
        }
    }

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