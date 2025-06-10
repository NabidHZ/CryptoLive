package com.cryptolive.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

/**
 * Clase de configuración para cargar variables de entorno desde un archivo `.env`.
 * Utiliza la biblioteca Dotenv para leer las variables y establecerlas como propiedades del sistema.
 */
@Configuration
public class DotenvConfig {

    /**
     * Método que se ejecuta después de la construcción del bean.
     * Cargaaas las variables de entorno desde el archivo `.env` y las establece como propiedades del sistema.
     */
    @PostConstruct
    public void loadEnv() {
        Dotenv dotenv = Dotenv.load(); // Carga las variables del archivo `.env`.
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue()) // Establece cada variable como propiedad del sistema.
        );
    }
}