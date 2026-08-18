package com.prestamos.prestamos.dto;

import com.prestamos.prestamos.domain.EstadoPrestamo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO que representa la información completa de un préstamo expuesta
 * por la API.
 *
 * <p>Incluye tanto los datos financieros (monto, tasa, cuota mensual)
 * como el estado del préstamo, fechas relevantes, los datos del cliente
 * y el plan de cuotas asociado.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "PrestamoResponse", description = "Información completa de un préstamo, incluyendo cliente y plan de cuotas.")
public class PrestamoResponseDTO {

    /** Identificador único del préstamo. */
    @Schema(description = "Identificador único del préstamo.", example = "10")
    private Long id;

    /** Datos del cliente asociado al préstamo. */
    @Schema(description = "Datos del cliente asociado al préstamo.")
    private ClienteResponseDTO cliente;

    /** Capital originalmente solicitado. */
    @Schema(description = "Capital originalmente solicitado.", example = "10000.00")
    private Double monto;

    /** Tasa de interés aplicada (en porcentaje). */
    @Schema(description = "Tasa de interés aplicada (porcentaje).", example = "15.5")
    private Double tasaInteres;

    /** Plazo del préstamo en meses. */
    @Schema(description = "Plazo del préstamo en meses.", example = "12")
    private Integer plazoMeses;

    /** Capital + intereses a devolver a lo largo del préstamo. */
    @Schema(description = "Monto total a devolver (capital + intereses).", example = "11550.00")
    private Double montoTotal;

    /** Valor de la cuota mensual calculada. */
    @Schema(description = "Valor de la cuota mensual.", example = "962.50")
    private Double cuotaMensual;

    /** Estado actual del préstamo (PENDIENTE, APROBADO, etc.). */
    @Schema(description = "Estado actual del préstamo.",
            example = "PENDIENTE", allowableValues = {"PENDIENTE", "APROBADO", "RECHAZADO", "PAGADO"})
    private EstadoPrestamo estado;

    /** Descripción legible del estado, útil para mostrar al usuario final. */
    @Schema(description = "Descripción legible del estado.", example = "Pendiente de aprobación")
    private String estadoDescripcion;

    /** Fecha y hora en la que se solicitó el préstamo. */
    @Schema(description = "Fecha y hora de la solicitud.", example = "2026-01-15T10:30:00")
    private LocalDateTime fechaSolicitud;

    /** Fecha y hora en la que fue aprobado (puede ser {@code null}). */
    @Schema(description = "Fecha y hora de aprobación (null si aún no fue aprobado).",
            example = "2026-01-16T09:00:00", nullable = true)
    private LocalDateTime fechaAprobacion;

    /** Lista de cuotas generadas para este préstamo. */
    @Schema(description = "Plan de cuotas generado para el préstamo.")
    private List<CuotaResponseDTO> cuotas;
}
