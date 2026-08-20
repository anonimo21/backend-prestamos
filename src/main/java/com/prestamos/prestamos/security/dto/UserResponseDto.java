package com.prestamos.prestamos.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * Datos seguros de una cuenta para la administración de usuarios.
 * La contraseña nunca se expone en las respuestas de la API.
 */
@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Set<String> roles;
}
