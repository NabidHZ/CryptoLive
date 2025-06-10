package com.cryptolive.execption;

/**
 * Excepción lanzada cuando se intenta registrar un usuario que ya existe en la base de datos.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}