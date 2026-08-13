package com.prestamos.prestamos.service;

import com.prestamos.prestamos.domain.EstadoPrestamo;
import com.prestamos.prestamos.dto.PrestamoRequestDTO;
import com.prestamos.prestamos.dto.PrestamoResponseDTO;

import java.util.List;

public interface IPrestamoService {

    List<PrestamoResponseDTO> listarTodos();

    List<PrestamoResponseDTO> listarPorEstado(EstadoPrestamo estado);

    PrestamoResponseDTO obtenerPorId(Long id);

    PrestamoResponseDTO crear(PrestamoRequestDTO dto);

    PrestamoResponseDTO actualizar(Long id, PrestamoRequestDTO dto);

    void eliminar(Long id);
}
