package com.cryptolive.execption;

/**
 * Excepción lanzada cuando la contraseña proporcionada no cumple los requisitos mínimos de seguridad.
 */
public class WeakPasswordException extends RuntimeException {
    public WeakPasswordException(String message) {
        super(message);
    }
}