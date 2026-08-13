package com.prestamos.prestamos.service;

import com.prestamos.prestamos.domain.Cliente;
import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;
import com.prestamos.prestamos.exception.BusinessRuleException;
import com.prestamos.prestamos.exception.DuplicateResourceException;
import com.prestamos.prestamos.exception.ResourceNotFoundException;
import com.prestamos.prestamos.mapper.ClienteMapper;
import com.prestamos.prestamos.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorFiltro(String keyword) {
        List<Cliente> clientes = clienteRepository.findAll();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim().toLowerCase();
            clientes = clientes.stream()
                    .filter(c -> c.getNombre().toLowerCase().contains(kw) 
                            || c.getApellido().toLowerCase().contains(kw)
                            || c.getIdentificacion().contains(kw))
                    .collect(Collectors.toList());
        }
        
        return clientes.stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        boolean existe = clienteRepository.findAll().stream()
                .anyMatch(c -> c.getIdentificacion().equalsIgnoreCase(dto.getIdentificacion()));
        
        if (existe) {
            throw new DuplicateResourceException("Ya existe un cliente registrado con la identificación: " + dto.getIdentificacion());
        }
        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (!cliente.getIdentificacion().equalsIgnoreCase(dto.getIdentificacion())) {
            boolean existe = clienteRepository.findAll().stream()
                    .anyMatch(c -> c.getIdentificacion().equalsIgnoreCase(dto.getIdentificacion()));
            
            if (existe) {
                throw new DuplicateResourceException("Ya existe otro cliente registrado con la identificación: " + dto.getIdentificacion());
            }
        }

        clienteMapper.updateEntityFromDTO(cliente, dto);
        Cliente actualizado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(actualizado);
    }

    @Override
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
