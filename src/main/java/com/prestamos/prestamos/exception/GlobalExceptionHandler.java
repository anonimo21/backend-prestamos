package com.prestamos.prestamos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manejador global de excepciones para todos los controladores REST.
 *
 * <p>Centraliza la traducción de excepciones a respuestas HTTP siguiendo
 * el estándar RFC 7807 ({@link ProblemDetail}), devolviendo códigos
 * coherentes según el tipo de error y un timestamp para facilitar la
 * trazabilidad en logs y del lado del cliente.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja {@link ResourceNotFoundException} y devuelve HTTP 404.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

    /**
     * Maneja {@link DuplicateResourceException} y devuelve HTTP 409.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicate(DuplicateResourceException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

    /**
     * Maneja {@link BusinessRuleException} y devuelve HTTP 422.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

    /**
     * Maneja errores de validación de Bean Validation en {@code @RequestBody}.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Error de validación en los datos enviados");
        problem.setProperty("timestamp", LocalDateTime.now());
        problem.setProperty("errors", errors);
        return problem;
    }

    /**
     * Maneja intentos autenticados que no cuentan con el rol requerido.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta acción");
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

    /**
     * Manejador comodín para cualquier excepción no capturada por los handlers anteriores.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor: " + ex.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }
}
