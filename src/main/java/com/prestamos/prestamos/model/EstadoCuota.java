package com.prestamos.prestamos.model;

import lombok.Getter;

@Getter
public enum EstadoCuota {

    PENDIENTE("Pendiente de pago"),
    PAGADO("Pagada"),
    VENCIDO("Vencida");

    private final String descripcion;

    EstadoCuota(String descripcion) {
        this.descripcion = descripcion;
    }
}
