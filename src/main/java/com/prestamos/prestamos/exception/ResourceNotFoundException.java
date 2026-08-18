package com.prestamos.prestamos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción que se lanza cuando un recurso solicitado no existe.
 *
 * <p>Mapea al código HTTP 404 vía {@link ResponseStatus} y es
 * traducida por el {@link GlobalExceptionHandler} en una respuesta
 * con detalle del problema.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param message mensaje que indica qué recurso no fue encontrado.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
