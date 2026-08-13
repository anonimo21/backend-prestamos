package com.prestamos.prestamos.service;

import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;

import java.util.List;

public interface IClienteService {

    List<ClienteResponseDTO> listarTodos();

    List<ClienteResponseDTO> buscarPorFiltro(String keyword);

    ClienteResponseDTO obtenerPorId(Long id);

    ClienteResponseDTO crear(ClienteRequestDTO dto);

    ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto);

    void eliminar(Long id);
}
