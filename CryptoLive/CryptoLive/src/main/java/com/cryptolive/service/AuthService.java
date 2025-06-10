// src/main/java/com/cryptolive/service/AuthService.java
package com.cryptolive.service;

import com.cryptolive.model.User;
import com.cryptolive.repository.UserRepository;
import com.cryptolive.security.JwtUtil;
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
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getEmail());
        }
        throw new RuntimeException("Credenciales inválidas");
    }

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