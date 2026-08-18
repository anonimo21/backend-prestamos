package com.prestamos.prestamos.controller;

import com.prestamos.prestamos.domain.EstadoPrestamo;
import com.prestamos.prestamos.dto.PrestamoRequestDTO;
import com.prestamos.prestamos.dto.PrestamoResponseDTO;
import com.prestamos.prestamos.service.IPrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de préstamos.
 *
 * <p>Expone los endpoints bajo {@code /api/v1/prestamos} y permite
 * crear, consultar, actualizar y eliminar préstamos, así como filtrar
 * por estado.</p>
 */
@RestController
@RequestMapping("/api/v1/prestamos")
@Tag(name = "Préstamos", description = "Operaciones para gestionar préstamos: alta, consulta, actualización y eliminación.")
public class PrestamoController { 

    private final IPrestamoService prestamoService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param prestamoService servicio de préstamos.
     */
    @Autowired
    public PrestamoController(IPrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    /**
     * Lista los préstamos con filtro opcional por estado.
     */
    @Operation(
            summary = "Listar préstamos",
            description = "Devuelve todos los préstamos del sistema, o solo los que coincidan con el estado enviado.")
    @ApiResponse(responseCode = "200", description = "Lista de préstamos recuperada con éxito.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PrestamoResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<PrestamoResponseDTO>> listar(
            @Parameter(description = "Estado por el que filtrar (opcional).",
                    example = "PENDIENTE",
                    schema = @Schema(implementation = EstadoPrestamo.class))
            @RequestParam(name = "estado", required = false) EstadoPrestamo estado) {
        List<PrestamoResponseDTO> prestamos;
        if (estado != null) {
            prestamos = prestamoService.listarPorEstado(estado);
        } else {
            prestamos = prestamoService.listarTodos();
        }
        return ResponseEntity.ok(prestamos);
    }

    /**
     * Obtiene un préstamo por su identificador.
     */
    @Operation(
            summary = "Obtener préstamo por ID",
            description = "Recupera un préstamo junto con su cliente asociado y el plan de cuotas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Préstamo encontrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrestamoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un préstamo con ese ID.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> obtenerPorId(
            @Parameter(description = "Identificador del préstamo.", example = "10")
            @PathVariable("id") Long id) {
        PrestamoResponseDTO prestamo = prestamoService.obtenerPorId(id);
        return ResponseEntity.ok(prestamo);
    }

    /**
     * Crea un préstamo y genera su plan de cuotas.
     */
    @Operation(
            summary = "Crear un préstamo",
            description = "Crea un préstamo para el cliente indicado, calcula automáticamente el monto total, "
                    + "la cuota mensual y genera el plan de cuotas. La última cuota absorbe los "
                    + "redondeos para que la suma coincida con el monto total.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Préstamo creado con su plan de cuotas.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrestamoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "El cliente indicado no existe.",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del préstamo a crear.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PrestamoRequestDTO.class)))
            @Valid @RequestBody PrestamoRequestDTO dto) {
        PrestamoResponseDTO prestamo = prestamoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
    }

    /**
     * Actualiza los datos de un préstamo existente.
     */
    @Operation(
            summary = "Actualizar préstamo",
            description = "Reemplaza los datos financieros de un préstamo existente y recalcula sus montos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Préstamo actualizado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PrestamoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Préstamo o cliente inexistente.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> actualizar(
            @Parameter(description = "Identificador del préstamo a actualizar.", example = "10")
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del préstamo.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PrestamoRequestDTO.class)))
            @Valid @RequestBody PrestamoRequestDTO dto) {
        PrestamoResponseDTO prestamo = prestamoService.actualizar(id, dto);
        return ResponseEntity.ok(prestamo);
    }

    /**
     * Elimina un préstamo del sistema.
     */
    @Operation(
            summary = "Eliminar préstamo",
            description = "Elimina un préstamo del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Préstamo eliminado."),
            @ApiResponse(responseCode = "404", description = "No existe un préstamo con ese ID.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del préstamo a eliminar.", example = "10")
            @PathVariable("id") Long id) {
        prestamoService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
