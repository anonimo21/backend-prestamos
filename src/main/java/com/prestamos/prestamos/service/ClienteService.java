package com.prestamos.prestamos.service;

import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;
import com.prestamos.prestamos.exception.BusinessRuleException;
import com.prestamos.prestamos.exception.DuplicateResourceException;
import com.prestamos.prestamos.exception.ResourceNotFoundException;
import com.prestamos.prestamos.mapper.ClienteMapper;
import com.prestamos.prestamos.model.Cliente;
import com.prestamos.prestamos.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ClienteResponseDTO> buscarPorFiltro(String keyword) {
        List<Cliente> clientes;
        if (keyword == null || keyword.trim().isEmpty()) {
            clientes = clienteRepository.findAll();
        } else {
            clientes = clienteRepository.buscarPorNombreApellidoOIdentificacion(keyword.trim());
        }
        return clientes.stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (clienteRepository.existsByIdentificacion(dto.getIdentificacion())) {
            throw new DuplicateResourceException("Ya existe un cliente registrado con la identificación: " + dto.getIdentificacion());
        }
        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(guardado);
    }

    @Transactional
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (!cliente.getIdentificacion().equalsIgnoreCase(dto.getIdentificacion())
                && clienteRepository.existsByIdentificacion(dto.getIdentificacion())) {
            throw new DuplicateResourceException("Ya existe otro cliente registrado con la identificación: " + dto.getIdentificacion());
        }

        clienteMapper.updateEntityFromDTO(cliente, dto);
        Cliente actualizado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        if (cliente.getPrestamos() != null && !cliente.getPrestamos().isEmpty()) {
            throw new BusinessRuleException("No se puede eliminar el cliente porque tiene préstamos asociados.");
        }
        clienteRepository.delete(cliente);
    }
}