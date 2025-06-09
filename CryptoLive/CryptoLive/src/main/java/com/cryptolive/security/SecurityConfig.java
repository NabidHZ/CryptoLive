package com.cryptolive.security;

import com.cryptolive.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración de seguridad para la aplicación.
 * Configura la autenticación basada en JWT, OAuth2 y las políticas de acceso a rutas.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter; // Filtro personalizado para manejar la autenticación JWT.

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService; // Servicio personalizado para manejar usuarios de OAuth2.

    /**
     * Configura la cadena de seguridad de Spring Security.
     *
     * @param http Objeto HttpSecurity para configurar la seguridad de la aplicación.
     * @return La cadena de seguridad configurada.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desactiva la protección CSRF, ya que se utiliza autenticación basada en tokens.
                .csrf(csrf -> csrf.disable())
                // Configura las reglas de autorización para las rutas.
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/api/login", "/register", "/oauth2/**").permitAll() // Permite el acceso sin autenticación a estas rutas.
                        .anyRequest().authenticated() // Requiere autenticación para todas las demás rutas.
                )
                // Configura la política de creación de sesiones.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                // Agrega el filtro de autenticación JWT antes del filtro de autenticación predeterminado.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Configura el inicio de sesión con OAuth2.
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // Define el servicio personalizado para manejar usuarios de OAuth2.
                        )
                        .defaultSuccessUrl("/index.html", true) // Define la URL de redirección tras un inicio de sesión exitoso.
                );
        return http.build();
    }

    /**
     * Define un bean para el codificador de contraseñas.
     * Utiliza BCrypt para cifrar y verificar contraseñas de forma segura.
     *
     * @return Una instancia de PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}