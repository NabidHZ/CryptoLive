// src/main/java/com/cryptolive/service/AuthService.java
package com.cryptolive.service;

import com.cryptolive.model.User;
import com.cryptolive.repository.UserRepository;
import com.cryptolive.security.JwtUtil;
import com.cryptolive.execption.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("No existe ningún usuario con ese correo electrónico");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("La contraseña no es correcta");
        }
        return jwtUtil.generateToken(user.getEmail());
    }

    public void register(String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new UserAlreadyExistsException("El usuario ya existe");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new InvalidEmailException("El email no tiene un formato válido");
        }
        if (password.length() < 8) {
            throw new WeakPasswordException("La contraseña es demasiado débil");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setProvider("LOCAL");
        userRepository.save(user);
    }
}