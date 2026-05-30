package com.swissroute.constant;

public final class ExceptionMessagesConstants {

    private ExceptionMessagesConstants() {
        throw new UnsupportedOperationException("Clase de constantes");
    }

    public static final String USER_EMAIL_NO_EXISTE = "El usuario con email %s no existe";
    public static final String EMAIL_YA_REGISTRADO = "El email ya se encuentra registrado";
    public static final String ROL_NO_ENCONTRADO = "El rol indicado no existe";
    public static final String CREDENCIALES_INVALIDAS = "Email o contraseña inválidos";
    public static final String RUTA_FAVORITA_NOMBRE_DUPLICADO = "Ya existe una ruta favorita con ese nombre para el usuario autenticado";
}