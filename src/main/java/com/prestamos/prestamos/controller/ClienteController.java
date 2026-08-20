package com.prestamos.prestamos.controller;

import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;
import com.prestamos.prestamos.service.IClienteService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de clientes.
 *
 * <p>Expone los endpoints bajo {@code /api/v1/clientes} y habilita
 * CORS para el frontend local en {@code http://localhost:4200}.</p>
 */
@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Clientes", description = "Operaciones CRUD para la gestión de clientes.")
public class ClienteController {

    private final IClienteService clienteService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param clienteService servicio de clientes.
     */
    @Autowired
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Lista los clientes con filtro opcional.
     */
    @Operation(
            summary = "Listar clientes",
            description = "Devuelve todos los clientes registrados. Si se envía el parámetro `buscar`, "
                    + "filtra por coincidencia parcial en nombre, apellido o identificación.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes recuperada con éxito.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClienteResponseDTO.class)))
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<ClienteResponseDTO>> listar(
            @Parameter(description = "Texto opcional para filtrar por nombre, apellido o identificación.",
                    example = "juan")
            @RequestParam(name = "buscar", required = false) String buscar) {
        List<ClienteResponseDTO> clientes = clienteService.buscarPorFiltro(buscar);
        return ResponseEntity.ok(clientes);
    }

    /**
     * Obtiene un cliente por su identificador.
     */
    @Operation(
            summary = "Obtener cliente por ID",
            description = "Recupera los datos de un cliente específico a partir de su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un cliente con ese ID.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(
            @Parameter(description = "Identificador del cliente.", example = "1")
            @PathVariable("id") Long id) {
        ClienteResponseDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    /**
     * Crea un cliente nuevo.
     */
    @Operation(
            summary = "Crear un nuevo cliente",
            description = "Registra un cliente en el sistema. La identificación debe ser única. "
                    + "Si ya existe, se devuelve 409.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados.",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Ya existe un cliente con esa identificación.",
                    content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> crear(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del cliente a crear.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequestDTO.class)))
            @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO nuevoCliente = clienteService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    /**
     * Actualiza los datos de un cliente existente.
     */
    @Operation(
            summary = "Actualizar cliente existente",
            description = "Reemplaza los datos de un cliente identificado por `id`. "
                    + "Si la identificación cambia, se valida que no esté duplicada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No existe un cliente con ese ID.",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "La nueva identificación ya está usada.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @Parameter(description = "Identificador del cliente a actualizar.", example = "1")
            @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del cliente.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequestDTO.class)))
            @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO actualizado = clienteService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina un cliente del sistema.
     */
    @Operation(
            summary = "Eliminar cliente",
            description = "Elimina un cliente del sistema. Si tiene préstamos asociados, "
                    + "se devuelve 422 (regla de negocio).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente."),
            @ApiResponse(responseCode = "404", description = "No existe un cliente con ese ID.",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "El cliente tiene préstamos asociados.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del cliente a eliminar.", example = "1")
            @PathVariable("id") Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
