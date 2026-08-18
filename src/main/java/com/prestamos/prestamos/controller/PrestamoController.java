package com.prestamos.prestamos.controller;

import com.prestamos.prestamos.domain.EstadoPrestamo;
import com.prestamos.prestamos.dto.PrestamoRequestDTO;
import com.prestamos.prestamos.dto.PrestamoResponseDTO;
import com.prestamos.prestamos.service.IPrestamoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    private final IPrestamoService prestamoService;

    @Autowired
    public PrestamoController(IPrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoResponseDTO>> listar(
            @RequestParam(name = "estado", required = false) EstadoPrestamo estado) {
        List<PrestamoResponseDTO> prestamos;
        if (estado != null) {
            prestamos = prestamoService.listarPorEstado(estado);
        } else {
            prestamos = prestamoService.listarTodos();
        }
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> obtenerPorId(@PathVariable("id") Long id) {
        PrestamoResponseDTO prestamo = prestamoService.obtenerPorId(id);
        return ResponseEntity.ok(prestamo);
    }

    @PostMapping
    public ResponseEntity<PrestamoResponseDTO> crear(@Valid @RequestBody PrestamoRequestDTO dto) {
        PrestamoResponseDTO prestamo = prestamoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> actualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody PrestamoRequestDTO dto) {
        PrestamoResponseDTO prestamo = prestamoService.actualizar(id, dto);
        return ResponseEntity.ok(prestamo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        prestamoService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
