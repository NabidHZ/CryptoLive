package com.cryptolive.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase de utilidad para manejar operaciones relacionadas con JWT (JSON Web Tokens).
 * Proporciona métodos para generar, validar y extraer información de los tokens.
 */
@Component
public class JwtUtil {

    /**
     * Secreto utilizado para firmar los tokens JWT.
     * Se obtiene del archivo de configuración `application.properties`.
     */
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    /**
     * Tiempo de expiración del token JWT en milisegundos (1 día).
     */
    private final long jwtExpirationMs = 86400000;

    /**
     * Obtiene la clave de firma HMAC a partir del secreto JWT.
     *
     * @return La clave de firma HMAC.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Genera un token JWT para un usuario dado.
     *
     * @param email El email del usuario que será el subject del token.
     * @return El token JWT generado.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // El email del usuario como subject.
                .setIssuedAt(new Date()) // Fecha de emisión.
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Expiración (1 día).
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma con clave secreta.
                .compact(); // Genera el token.
    }

    /**
     * Extrae el email (subject) de un token JWT.
     *
     * @param token El token JWT del cual se extraerá el email.
     * @return El email contenido en el token.
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Usa la clave secreta.
                .build()
                .parseClaimsJws(token) // Parsear el token.
                .getBody()
                .getSubject(); // Extraer el subject (email).
    }

    /**
     * Valida un token JWT verificando su firma y su validez.
     *
     * @param token El token JWT a validar.
     * @return `true` si el token es válido, `false` en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Usa la clave secreta para validar.
                    .build()
                    .parseClaimsJws(token); // Intenta parsear el token.
            return true; // Si no hay excepciones, el token es válido.
        } catch (JwtException e) {
            return false; // Si hay una excepción, el token no es válido.
        }
    }
}