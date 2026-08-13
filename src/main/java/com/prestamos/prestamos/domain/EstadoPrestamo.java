package com.prestamos.prestamos.domain;

import lombok.Getter;

@Getter
public enum EstadoPrestamo {

    PENDIENTE("Pendiente de aprobación"),
    APROBADO("Aprobado, en curso de pago"),
    RECHAZADO("Rechazado"),
    PAGADO("Totalmente pagado");

    private final String descripcion;

    EstadoPrestamo(String descripcion) {
        this.descripcion = descripcion;
    }
}
