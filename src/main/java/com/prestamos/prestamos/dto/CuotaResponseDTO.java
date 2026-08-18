package com.prestamos.prestamos.dto;

import com.prestamos.prestamos.domain.EstadoCuota;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO que representa una cuota individual de un préstamo.
 *
 * <p>Es el objeto que se serializa dentro de
 * {@link PrestamoResponseDTO#getCuotas()} para que el cliente de la
 * API vea el plan de pagos.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "CuotaResponse", description = "Información de una cuota individual del plan de pagos.")
public class CuotaResponseDTO {

    /** Identificador único de la cuota. */
    @Schema(description = "Identificador único de la cuota.", example = "101")
    private Long id;

    /** Número de cuota dentro del préstamo (1, 2, 3, …). */
    @Schema(description = "Número de cuota dentro del préstamo.", example = "1")
    private Integer numeroCuota;

    /** Monto a pagar en esta cuota. */
    @Schema(description = "Monto a pagar en esta cuota.", example = "962.50")
    private Double monto;

    /** Fecha límite en la que la cuota debe ser pagada. */
    @Schema(description = "Fecha de vencimiento de la cuota.", example = "2026-02-15")
    private LocalDate fechaVencimiento;

    /** Estado actual de la cuota (PENDIENTE, PAGADO, VENCIDO). */
    @Schema(description = "Estado de la cuota.",
            example = "PENDIENTE", allowableValues = {"PENDIENTE", "PAGADO", "VENCIDO"})
    private EstadoCuota estado;

    /** Descripción legible del estado, útil para mostrar al usuario final. */
    @Schema(description = "Descripción legible del estado.", example = "Pendiente de pago")
    private String estadoDescripcion;

    /** Fecha real en la que la cuota fue pagada (puede ser {@code null}). */
    @Schema(description = "Fecha real de pago (null si aún no fue pagada).",
            example = "2026-02-14", nullable = true)
    private LocalDate fechaPago;
}
