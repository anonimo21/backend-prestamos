package com.prestamos.prestamos.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enumeración que define los posibles estados de una cuota.
 *
 * <p>Se almacena como cadena ({@link EnumType#STRING}) en la base de
 * datos para mantener legibilidad y facilitar la evolución del esquema
 * sin renumerar los valores.</p>
 */
@Schema(name = "EstadoCuota", description = "Estados posibles de una cuota individual.")
@Getter
public enum EstadoCuota {

    /** La cuota aún no ha sido pagada. */
    PENDIENTE("Pendiente de pago"),

    /** La cuota fue pagada completamente. */
    PAGADO("Pagada"),

    /** La cuota superó su fecha de vencimiento sin pago. */
    VENCIDO("Vencida");

    private final String descripcion;

    EstadoCuota(String descripcion) {
        this.descripcion = descripcion;
    }
}
