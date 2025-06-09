package com.cryptolive.controller;

import com.cryptolive.model.User;
import com.cryptolive.repository.UserRepository;
import com.cryptolive.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la autenticación de usuarios.
 * Proporciona endpoints para el inicio de sesión y el registro de usuarios.
 */
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository; // Repositorio para acceder a los datos de los usuarios.
    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador de contraseñas (BCrypt).
    @Autowired
    private JwtUtil jwtUtil; // Utilidad para generar y validar tokens JWT.

    /**
     * Endpoint para iniciar sesión.
     *
     * @param email    El email del usuario.
     * @param password La contraseña del usuario.
     * @return Un token JWT si las credenciales son válidas.
     * @throws RuntimeException Si las credenciales son inválidas.
     */
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        User user = userRepository.findByEmail(email); // Busca el usuario por email.
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Genera y devuelve un token JWT si la contraseña coincide.
            return jwtUtil.generateToken(user.getEmail());
        }
        // Lanza una excepción si las credenciales no son válidas.
        throw new RuntimeException("Credenciales inválidas");
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param email    El email del usuario.
     * @param password La contraseña del usuario.
     * @return Un mensaje indicando que el usuario fue registrado correctamente.
     * @throws RuntimeException Si el usuario ya existe.
     */
    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password) {
        if (userRepository.findByEmail(email) != null) {
            // Lanza una excepción si el usuario ya existe.
            throw new RuntimeException("El usuario ya existe");
        }
        // Crea un nuevo usuario con la contraseña cifrada.
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setProvider("LOCAL");
        userRepository.save(user); // Guarda el usuario en la base de datos.
        return "Usuario registrado correctamente";
    }
}