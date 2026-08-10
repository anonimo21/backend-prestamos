package com.prestamos.prestamos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser positivo")
    private Long clienteId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    @NotNull(message = "La tasa de interés es obligatoria")
    @PositiveOrZero(message = "La tasa de interés no puede ser negativa")
    private Double tasaInteres;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Positive(message = "El plazo debe ser mayor a cero")
    private Integer plazoMeses;
}
