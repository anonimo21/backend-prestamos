package com.prestamos.prestamos.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
/**
 * Devuelve una respuesta HTTP 401 cuando una solicitud no autenticada
 * intenta acceder a un recurso protegido.
 */
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    /**
     * Envía el error de autenticación al cliente.
     *
     * @param request solicitud que no pudo ser autenticada.
     * @param response respuesta donde se informa el error 401.
     * @param authException causa de la falla de autenticación.
     * @throws IOException si no se puede escribir la respuesta.
     * @throws ServletException si ocurre un error de servlet.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
