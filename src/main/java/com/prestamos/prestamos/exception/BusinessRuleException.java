package com.prestamos.prestamos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando se viola una regla de negocio.
 *
 * <p>La anotación {@link ResponseStatus} marca el código HTTP por
 * defecto como {@link HttpStatus#UNPROCESSABLE_ENTITY} (422), aunque
 * el {@link GlobalExceptionHandler} es quien realmente construye la
 * respuesta {@code ProblemDetail}.</p>
 */
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class BusinessRuleException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param message mensaje legible que explica la regla incumplida.
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}
