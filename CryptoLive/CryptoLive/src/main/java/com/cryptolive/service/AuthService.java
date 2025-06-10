// src/main/java/com/cryptolive/service/AuthService.java
package com.cryptolive.service;

import com.cryptolive.model.User;
import com.cryptolive.repository.UserRepository;
import com.cryptolive.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Clase de servicio para gestionar las operaciones relacionadas con la autenticación.
 * Proporciona métodos para el inicio de sesión y el registro de usuarios.
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository; // Repositorio para acceder a los datos de los usuarios.
    @Autowired
    private PasswordEncoder passwordEncoder; // Codificador para encriptar contraseñas.
    @Autowired
    private JwtUtil jwtUtil; // Utilidad para generar tokens JWT.

    /**
     * Autentica a un usuario basado en su correo electrónico y contraseña.
     *
     * @param email El correo electrónico del usuario que intenta iniciar sesión.
     * @param password La contraseña proporcionada por el usuario.
     * @return Un token JWT si la autenticación es exitosa.
     * @throws RuntimeException Si las credenciales son inválidas.
     */
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getEmail());
        }
        throw new RuntimeException("Credenciales inválidas");
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param email El correo electrónico del usuario que se desea registrar.
     * @param password La contraseña para el nuevo usuario.
     * @throws RuntimeException Si el usuario ya existe.
     */
    public void register(String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("El usuario ya existe");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setProvider("LOCAL");
        userRepository.save(user);
    }
}