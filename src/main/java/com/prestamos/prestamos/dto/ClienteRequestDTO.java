package com.prestamos.prestamos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO recibido por la API para crear o actualizar un cliente.
 *
 * <p>Incluye restricciones Bean Validation ({@code @NotBlank},
 * {@code @Email}, {@code @Size}) que son ejecutadas por Spring antes
 * de invocar el servicio, devolviéndose un 400 con los mensajes
 * correspondientes cuando alguna regla no se cumple.</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ClienteRequest", description = "Datos necesarios para registrar o modificar un cliente en el sistema.")
public class ClienteRequestDTO {

    /**
     * Número de identificación (DNI/documento) del cliente.
     * <p>Es único en el sistema y se usa para detectar duplicados.</p>
     */
    @NotBlank(message = "La identificación es obligatoria")
    @Size(min = 6, max = 20, message = "La identificación debe tener entre 6 y 20 caracteres")
    @Schema(description = "Número de identificación único del cliente (DNI/documento).",
            example = "12345678A", requiredMode = Schema.RequiredMode.REQUIRED, minLength = 6, maxLength = 20)
    private String identificacion;

    /** Nombre del cliente (no nulo, máximo 100 caracteres). */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Schema(description = "Nombre del cliente.", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100)
    private String nombre;

    /** Apellido del cliente (no nulo, máximo 100 caracteres). */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    @Schema(description = "Apellido del cliente.", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100)
    private String apellido;

    /** Email del cliente; debe tener un formato válido. */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 150, message = "El email no puede tener más de 150 caracteres")
    @Schema(description = "Correo electrónico de contacto.",
            example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED, maxLength = 150)
    private String email;

    /** Teléfono de contacto (opcional). */
    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    @Schema(description = "Teléfono de contacto (opcional).", example = "+5491122334455", maxLength = 20)
    private String telefono;

    /** Dirección física del cliente (opcional). */
    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    @Schema(description = "Dirección física del cliente (opcional).",
            example = "Av. Siempre Viva 742, Springfield", maxLength = 255)
    private String direccion;
}
