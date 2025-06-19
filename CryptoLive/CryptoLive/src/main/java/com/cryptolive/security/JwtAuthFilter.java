package com.cryptolive.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que se ejecuta una vez por solicitud.
 * Valida el token JWT presente en el encabezado de autorización y establece
 * el contexto de seguridad si el token es válido.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para manejar operaciones relacionadas con JWT.

    /**
     * Determina si el filtro no debe aplicarse a ciertas rutas.
     *
     * @param request La solicitud HTTP.
     * @return true si la ruta es "/login", "/register" o comienza con "/oauth2/", false en caso contrario.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/login") || path.equals("/register") || path.startsWith("/oauth2/");
    }

    /**
     * Lógica principal del filtro para validar el token JWT y establecer el contexto de seguridad.
     *
     * @param request     La solicitud HTTP entrante.
     * @param response    La respuesta HTTP saliente.
     * @param filterChain La cadena de filtros.
     * @throws ServletException Si ocurre un error en el procesamiento del filtro.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Obtiene el encabezado de autorización de la solicitud.
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // Verifica si el encabezado contiene un token Bearer.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extrae el token eliminando el prefijo "Bearer ".
            if (jwtUtil.validateToken(token)) {
                email = jwtUtil.getEmailFromToken(token); // Extrae el email del token si es válido.
            }
        }

        // Si el email es válido y no hay autenticación en el contexto, la establece.
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, null, null); // Crea un token de autenticación.
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken); // Establece el contexto de seguridad.
        }

        // Continúa con el siguiente filtro en la cadena.
        filterChain.doFilter(request, response); // Continúa con el siguiente filtro.
    }
}