package com.prestamos.prestamos.mapper;

import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;
import com.prestamos.prestamos.domain.Cliente;
import org.springframework.stereotype.Component;

/**
 * Componente que convierte entre la entidad {@link Cliente} y sus DTOs.
 *
 * <p>Centraliza la lógica de transformación para evitar duplicarla en
 * el servicio y para mantener una única forma de "mapear" entre las
 * capas de la aplicación.</p>
 */
@Component
public class ClienteMapper {

    /**
     * Convierte un {@link ClienteRequestDTO} en una entidad nueva
     * {@link Cliente}.
     *
     * <p>El id y la fecha de registro se dejan vacíos para que JPA los
     * complete al persistir.</p>
     *
     * @param dto DTO con los datos ingresados por el cliente.
     * @return una instancia de {@link Cliente} lista para ser guardada,
     *         o {@code null} si el DTO recibido es {@code null}.
     */
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

    /**
     * Convierte una entidad {@link Cliente} en un
     * {@link ClienteResponseDTO} apto para devolver al cliente de la API.
     *
     * <p>Incluye el campo derivado {@code nombreCompleto} calculado por
     * la entidad.</p>
     *
     * @param cliente entidad a transformar (puede ser {@code null}).
     * @return el DTO equivalente, o {@code null} si la entrada es nula.
     */
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

    /**
     * Actualiza los campos editables de una entidad {@link Cliente}
     * con los valores recibidos en un {@link ClienteRequestDTO}.
     *
     * <p>No modifica el {@code id} ni la {@code fechaRegistro}. Se usa
     * típicamente en el endpoint de actualización (PUT/PATCH).</p>
     *
     * @param cliente entidad a actualizar (se modifica in-place).
     * @param dto     DTO con los nuevos valores.
     */
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
