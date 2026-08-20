package com.prestamos.prestamos.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
/**
 * Genera, lee y valida los tokens JWT usados para autenticar solicitudes.
 */
public class JwtGenerator {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Construye la clave simétrica empleada para firmar y verificar los JWT.
     *
     * @return clave HMAC derivada del secreto configurado.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token firmado con el usuario autenticado y la fecha de
     * expiración configurada.
     *
     * @param authentication autenticación válida de Spring Security.
     * @return representación compacta del JWT firmado.
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpiration);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();

        return token;
    }

    /**
     * Extrae el nombre de usuario almacenado como sujeto del token.
     *
     * @param token JWT firmado y válido.
     * @return nombre de usuario asociado al token.
     */
    public String getUsernameFromJwt(String token) {
        // La forma moderna: parser().verifyWith(...).parseSignedClaims(...).getPayload()
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSigningKey()) // Asegurarse de que el casting es correcto
                .build()
                .parseSignedClaims(token) // parseSignedClaims en lugar de parseClaimsJws
                .getPayload(); // getPayload en lugar de getBody
        return claims.getSubject();
    }

    /**
     * Comprueba que el token tenga una firma válida y no esté malformado,
     * vencido o vacío.
     *
     * @param token JWT que se desea validar.
     * @return {@code true} si el token puede ser usado; de lo contrario, {@code false}.
     */
    public boolean validateToken(String token) {
        try {
            // La forma moderna para validación también
            Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(token); // Aquí solo nos interesa que no lance excepción
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        } catch (SignatureException e) { // Importante para HS512
            System.out.println("Signature validation failed: " + e.getMessage());
        }
        return false;
    }

}
