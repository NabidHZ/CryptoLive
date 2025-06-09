package com.cryptolive.service;

import com.cryptolive.model.User;
import com.cryptolive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

/**
 * Servicio personalizado para manejar la carga de usuarios de OAuth2.
 * Extiende la funcionalidad predeterminada de OAuth2 para integrar usuarios con la base de datos.
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository; // Repositorio para interactuar con la base de datos de usuarios.

    /**
     * Carga un usuario de OAuth2 y lo sincroniza con la base de datos.
     * Si el usuario no existe en la base de datos, se crea uno nuevo.
     *
     * @param userRequest La solicitud de usuario de OAuth2 que contiene los detalles de autenticación.
     * @return El usuario de OAuth2 cargado.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Carga el usuario de OAuth2 utilizando el servicio predeterminado.
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        // Obtiene el email del usuario desde los atributos de OAuth2.
        String email = oAuth2User.getAttribute("email");

        // Busca al usuario en la base de datos por su email.
        User user = userRepository.findByEmail(email);

        // Si el usuario no existe, lo crea y lo guarda en la base de datos.
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setProvider("GOOGLE"); // Establece el proveedor como "GOOGLE".
            userRepository.save(user);
        }

        // Retorna el usuario de OAuth2 cargado.
        return oAuth2User;
    }
}