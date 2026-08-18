package com.prestamos.prestamos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
     *
     * @param ex excepción lanzada cuando un recurso no existe.
     * @return detalle del problema con el mensaje original.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

    /**
     * Maneja {@link DuplicateResourceException} y devuelve HTTP 409.
     *
     * <p>Se usa, por ejemplo, cuando se intenta crear un cliente con
     * una identificación que ya existe.</p>
     *
     * @param ex excepción lanzada por un conflicto de unicidad.
     * @return detalle del problema con el mensaje original.
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicate(DuplicateResourceException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

    /**
     * Maneja {@link BusinessRuleException} y devuelve HTTP 422.
     *
     * @param ex excepción lanzada por una regla de negocio violada.
     * @return detalle del problema con el mensaje original.
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

    /**
     * Maneja errores de validación de Bean Validation en {@code @RequestBody}.
     *
     * <p>Recorre los errores de campo y los devuelve como un mapa
     * {@code campo → mensaje} bajo la propiedad {@code errors}.</p>
     *
     * @param ex excepción generada por Spring cuando alguna restricción
     *           {@code @NotBlank}, {@code @Email}, etc. falla.
     * @return detalle del problema con el detalle y los errores por campo.
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
     * Manejador comodín para cualquier excepción no capturada por los
     * handlers anteriores.
     *
     * <p>Devuelve HTTP 500 con un mensaje genérico que incluye el detalle
     * de la excepción para no perder información útil en el log.</p>
     *
     * @param ex excepción inesperada.
     * @return detalle del problema con código 500.
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
