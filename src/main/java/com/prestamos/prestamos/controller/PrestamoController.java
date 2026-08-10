package com.prestamos.prestamos.controller;

import com.prestamos.prestamos.dto.*;
import com.prestamos.prestamos.model.EstadoPrestamo;
import com.prestamos.prestamos.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    @Autowired
    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PrestamoResponseDTO>>> listar(
            @RequestParam(name = "estado", required = false) EstadoPrestamo estado) {
        List<PrestamoResponseDTO> prestamos;
        if (estado != null) {
            prestamos = prestamoService.listarPorEstado(estado);
        } else {
            prestamos = prestamoService.listarTodos();
        }
        return ResponseEntity.ok(ApiResponse.ok(prestamos, "Lista de préstamos recuperada"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrestamoResponseDTO>> obtenerPorId(@PathVariable("id") Long id) {
        PrestamoResponseDTO prestamo = prestamoService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.ok(prestamo, "Detalle del préstamo obtenido"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PrestamoResponseDTO>> crear(@Valid @RequestBody PrestamoRequestDTO dto) {
        PrestamoResponseDTO prestamo = prestamoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(prestamo, "Préstamo creado exitosamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PrestamoResponseDTO>> actualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody PrestamoRequestDTO dto) {
        PrestamoResponseDTO prestamo = prestamoService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(prestamo, "Préstamo actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable("id") Long id) {
        prestamoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Préstamo eliminado exitosamente"));
    }
}