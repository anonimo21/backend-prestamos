package com.prestamos.prestamos.controller;

import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;
import com.prestamos.prestamos.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    private final IClienteService clienteService;

    @Autowired
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar(
            @RequestParam(name = "buscar", required = false) String buscar) {
        List<ClienteResponseDTO> clientes = clienteService.buscarPorFiltro(buscar);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable("id") Long id) {
        ClienteResponseDTO cliente = clienteService.obtenerPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO nuevoCliente = clienteService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizar(
            @PathVariable("id") Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO actualizado = clienteService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}
