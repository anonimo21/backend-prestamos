package com.prestamos.prestamos.security.config;

import com.prestamos.prestamos.security.jwt.JwtAuthEntryPoint;
import com.prestamos.prestamos.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
/**
 * Define la seguridad stateless de la API, la validación de JWT y los
 * componentes de autenticación usados por Spring Security.
 */
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    /**
     * Configura las rutas públicas, exige autenticación para el resto y
     * registra el filtro JWT antes del filtro de usuario y contraseña.
     *
     * @param http configurador de seguridad HTTP de Spring.
     * @return cadena de filtros de seguridad construida.
     * @throws Exception si Spring no puede construir la configuración.
     */
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling( exception ->
                    exception.authenticationEntryPoint( jwtAuthEntryPoint )
                )
                .sessionManagement( session ->
                    session.sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/auth/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").hasRole("ADMIN")
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/h2-console/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .headers(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    /**
     * Proporciona el codificador BCrypt empleado para almacenar y
     * comparar contraseñas de forma segura.
     *
     * @return codificador de contraseñas BCrypt.
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    /**
     * Expone el administrador que delega la validación de credenciales
     * en los proveedores configurados por Spring Security.
     *
     * @param authenticationConfiguration configuración de autenticación de Spring.
     * @return administrador de autenticación de la aplicación.
     * @throws Exception si no se puede recuperar el administrador.
     */
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
