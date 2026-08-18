package com.prestamos.prestamos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO que se devuelve desde la API al consultar clientes.
 *
 * <p>A diferencia de {@link ClienteRequestDTO}, este objeto es de solo
 * lectura e incluye campos derivados como {@code nombreCompleto} o
 * metadatos como {@code fechaRegistro} que no son ingresados por el
 * cliente sino asignados por el servidor.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ClienteResponse", description = "Información completa de un cliente expuesta por la API.")
public class ClienteResponseDTO {

    /** Identificador único del cliente en la base de datos. */
    @Schema(description = "Identificador único del cliente.", example = "1")
    private Long id;

    /** Número de identificación (DNI/documento) del cliente. */
    @Schema(description = "Número de identificación del cliente.", example = "12345678A")
    private String identificacion;

    /** Nombre del cliente. */
    @Schema(description = "Nombre del cliente.", example = "Juan")
    private String nombre;

    /** Apellido del cliente. */
    @Schema(description = "Apellido del cliente.", example = "Pérez")
    private String apellido;

    /** Nombre y apellido concatenados, listo para mostrar. */
    @Schema(description = "Nombre y apellido concatenados.", example = "Juan Pérez")
    private String nombreCompleto;

    /** Email registrado del cliente. */
    @Schema(description = "Email registrado.", example = "juan.perez@example.com")
    private String email;

    /** Teléfono de contacto (puede ser {@code null}). */
    @Schema(description = "Teléfono de contacto.", example = "+5491122334455", nullable = true)
    private String telefono;

    /** Dirección física del cliente (puede ser {@code null}). */
    @Schema(description = "Dirección física.", example = "Av. Siempre Viva 742, Springfield", nullable = true)
    private String direccion;

    /** Fecha y hora en la que el cliente fue registrado en el sistema. */
    @Schema(description = "Fecha y hora en que el cliente fue registrado.",
            example = "2026-01-15T10:30:00")
    private LocalDateTime fechaRegistro;
}
