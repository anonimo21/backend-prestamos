package com.prestamos.prestamos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando se intenta crear o actualizar un
 * recurso con un valor que viola una restricción de unicidad.
 *
 * <p>Mapea al código HTTP 409 (Conflict) y es traducida por el
 * {@link GlobalExceptionHandler} en una respuesta con detalle del
 * problema.</p>
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param message mensaje que indica qué valor duplicado se intentó usar.
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
