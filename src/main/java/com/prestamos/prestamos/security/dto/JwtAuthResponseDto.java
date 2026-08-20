package com.prestamos.prestamos.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponseDto {

    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer ";

    /**
     * Crea una respuesta de autenticación usando el token recibido y el
     * tipo estándar {@code Bearer}.
     *
     * @param accessToken token JWT generado tras un inicio de sesión válido.
     */
    public JwtAuthResponseDto(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer ";
    }
}
