package com.prestamos.prestamos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO recibido por la API para crear un préstamo.
 *
 * <p>El servicio toma estos datos, valida reglas de negocio (cliente
 * existente, monto dentro de un rango, etc.) y calcula los valores
 * derivados (monto total, cuota mensual, plan de cuotas) que se
 * persisten junto con el préstamo.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PrestamoRequest", description = "Datos necesarios para crear un préstamo para un cliente existente.")
public class PrestamoRequestDTO {

    /** ID del cliente que solicita el préstamo (obligatorio y positivo). */
    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser positivo")
    @Schema(description = "Identificador del cliente que solicita el préstamo.",
            example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clienteId;

    /** Capital solicitado; debe ser mayor a cero. */
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Schema(description = "Monto del capital solicitado (sin intereses).",
            example = "10000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double monto;

    /** Tasa de interés aplicada (en porcentaje); no puede ser negativa. */
    @NotNull(message = "La tasa de interés es obligatoria")
    @PositiveOrZero(message = "La tasa de interés no puede ser negativa")
    @Schema(description = "Tasa de interés aplicada, expresada en porcentaje.",
            example = "15.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double tasaInteres;

    /** Plazo del préstamo en meses; debe ser mayor a cero. */
    @NotNull(message = "El plazo en meses es obligatorio")
    @Positive(message = "El plazo debe ser mayor a cero")
    @Schema(description = "Plazo del préstamo expresado en meses.",
            example = "12", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer plazoMeses;
}
