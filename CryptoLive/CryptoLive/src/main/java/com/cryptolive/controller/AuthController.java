package com.cryptolive.controller;

import com.cryptolive.model.User;
import com.cryptolive.repository.UserRepository;
import com.cryptolive.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getEmail());
        }
        throw new RuntimeException("Credenciales inválidas");
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("El usuario ya existe");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setProvider("LOCAL");
        userRepository.save(user);
        return "Usuario registrado correctamente";
    }
}