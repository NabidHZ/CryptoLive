// src/main/java/com/cryptolive/controller/AuthController.java
package com.cryptolive.controller;

import com.cryptolive.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar las operaciones de autenticación.
 * Proporciona endpoints para el inicio de sesión y el registro de usuarios.
 */
@RestController
public class AuthController {

    @Autowired
    private AuthService authService; // Servicio para manejar la lógica de autenticación.

    /**
     * Endpoint para iniciar sesión.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return Una respuesta HTTP con un token JWT si la autenticación es exitosa,
     *         o un error si las credenciales son inválidas.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            String jwt = authService.login(email, password);
            return ResponseEntity.ok().body(new JwtResponse(jwt));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return Una respuesta HTTP indicando el éxito del registro,
     *         o un error si el usuario ya existe.
     */
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

    /**
     * Clase para encapsular la respuesta con el token JWT.
     */
    static class JwtResponse {
        public String token;

        /**
         * Constructor para inicializar el token.
         *
         * @param token El token JWT generado.
         */
        public JwtResponse(String token) { this.token = token; }
    }

    /**
     * Clase para encapsular mensajes de respuesta exitosos.
     */
    static class MessageResponse {
        public String message;

        /**
         * Constructor para inicializar el mensaje.
         *
         * @param message El mensaje de respuesta.
         */
        public MessageResponse(String message) { this.message = message; }
    }

    /**
     * Clase para encapsular mensajes de error.
     */
    static class ErrorResponse {
        public String error;

        /**
         * Constructor para inicializar el mensaje de error.
         *
         * @param error El mensaje de error.
         */
        public ErrorResponse(String error) { this.error = error; }
    }
}