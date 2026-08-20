package com.prestamos.prestamos.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
/**
 * Intercepta cada solicitud para identificar un JWT Bearer válido y
 * registrar al usuario autenticado en el contexto de seguridad.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtGenerator jwtGenerator;
    private final UserDetailsService userDetailsService;

    @Override
    /**
     * Extrae y valida el JWT de la solicitud; si corresponde a un usuario
     * válido, construye su autenticación antes de continuar la cadena.
     *
     * @param request solicitud HTTP entrante.
     * @param response respuesta HTTP en curso.
     * @param filterChain cadena de filtros que continuará el procesamiento.
     * @throws ServletException si ocurre un error de servlet.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = getJwtFromRequest(request);

        if(StringUtils.hasText(token) && jwtGenerator.validateToken(token)
                && SecurityContextHolder.getContext().getAuthentication()==null
        ){
            String username = jwtGenerator.getUsernameFromJwt(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }

        filterChain.doFilter(request, response);

    }

    /**
     * Obtiene el token sin el prefijo {@code Bearer } de la cabecera
     * {@code Authorization} de una solicitud.
     *
     * @param request solicitud que puede incluir las credenciales.
     * @return JWT extraído o {@code null} si no hay una cabecera válida.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
