package com.cryptolive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() { //Esta clase configura CORS para permitir solicitudes desde el frontend.
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permite todas las rutas.
                        .allowedOrigins("http://localhost:5173") // Origen permitido (frontend).
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos.
                        .allowedHeaders("*") // Permite todos los encabezados.
                        .allowCredentials(true); // Permite el envío de cookies o credenciales.
            }
        };
    }
}