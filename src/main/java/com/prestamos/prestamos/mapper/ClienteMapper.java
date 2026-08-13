package com.prestamos.prestamos.mapper;

import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;
import com.prestamos.prestamos.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        if (dto == null) return null;
        Cliente cliente = new Cliente();
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        return cliente;
    }

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        if (cliente == null) return null;
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .identificacion(cliente.getIdentificacion())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .nombreCompleto(cliente.getNombreCompleto())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .fechaRegistro(cliente.getFechaRegistro())
                .build();
    }

    public void updateEntityFromDTO(Cliente cliente, ClienteRequestDTO dto) {
        if (cliente == null || dto == null) return;
        cliente.setIdentificacion(dto.getIdentificacion());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
    }
}