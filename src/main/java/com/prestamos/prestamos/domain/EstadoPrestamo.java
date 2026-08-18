package com.prestamos.prestamos.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enumeración que define los posibles estados de un préstamo.
 *
 * <p>El ciclo de vida habitual es {@link #PENDIENTE} → {@link #APROBADO}
 * → {@link #PAGADO}, aunque también puede terminar en {@link #RECHAZADO}
 * si la solicitud no es aprobada.</p>
 */
@Schema(name = "EstadoPrestamo", description = "Estados posibles de un préstamo dentro de su ciclo de vida.")
@Getter
public enum EstadoPrestamo {

    /** Solicitud creada pero aún no evaluada. */
    PENDIENTE("Pendiente de aprobación"),

    /** Préstamo aprobado, con cuotas en curso de pago. */
    APROBADO("Aprobado, en curso de pago"),

    /** Solicitud rechazada; no se desembolsó dinero. */
    RECHAZADO("Rechazado"),

    /** Todas las cuotas fueron pagadas y el préstamo quedó saldado. */
    PAGADO("Totalmente pagado");

    private final String descripcion;

    EstadoPrestamo(String descripcion) {
        this.descripcion = descripcion;
    }
}
