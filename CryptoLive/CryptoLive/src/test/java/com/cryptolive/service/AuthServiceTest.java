package com.cryptolive.service;

import com.cryptolive.execption.IncorrectPasswordException;
import com.cryptolive.execption.UserNotFoundException;
import com.cryptolive.model.User;
import com.cryptolive.repository.UserRepository;
import com.cryptolive.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ConCredencialesCorrectas_RetornaToken() {
        // Arrange
        String email = "test@test.com";
        String password = "password123";
        String hashedPassword = "hashedPassword";
        String token = "jwt-token";

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);
        when(jwtUtil.generateToken(email)).thenReturn(token);

        // Act
        String resultado = authService.login(email, password);

        // Assert
        assertEquals(token, resultado);
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, hashedPassword);
        verify(jwtUtil).generateToken(email);
    }

    @Test
    void login_ConUsuarioInexistente_LanzaExcepcion() {
        // Arrange
        String email = "noexiste@test.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            authService.login(email, password);
        });

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void login_ConPasswordIncorrecta_LanzaExcepcion() {
        // Arrange
        String email = "test@test.com";
        String password = "wrongpassword";
        String hashedPassword = "hashedPassword";

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(false);

        // Act & Assert
        assertThrows(IncorrectPasswordException.class, () -> {
            authService.login(email, password);
        });

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, hashedPassword);
        verifyNoInteractions(jwtUtil);
    }
}