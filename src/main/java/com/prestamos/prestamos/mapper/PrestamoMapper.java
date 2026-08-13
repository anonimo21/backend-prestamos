package com.prestamos.prestamos.mapper;

import com.prestamos.prestamos.dto.CuotaResponseDTO;
import com.prestamos.prestamos.dto.PrestamoResponseDTO;
import com.prestamos.prestamos.domain.Prestamo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrestamoMapper {

    private final ClienteMapper clienteMapper;
    private final CuotaMapper cuotaMapper;

    @Autowired
    public PrestamoMapper(ClienteMapper clienteMapper, CuotaMapper cuotaMapper) {
        this.clienteMapper = clienteMapper;
        this.cuotaMapper = cuotaMapper;
    }

    public PrestamoResponseDTO toResponseDTO(Prestamo prestamo) {
        if (prestamo == null) return null;

        List<CuotaResponseDTO> cuotasDTO = prestamo.getCuotas() == null ? List.of() :
                prestamo.getCuotas().stream()
                        .map(cuotaMapper::toResponseDTO)
                        .collect(Collectors.toList());

        return PrestamoResponseDTO.builder()
                .id(prestamo.getId())
                .cliente(clienteMapper.toResponseDTO(prestamo.getCliente()))
                .monto(prestamo.getMonto())
                .tasaInteres(prestamo.getTasaInteres())
                .plazoMeses(prestamo.getPlazoMeses())
                .montoTotal(prestamo.getMontoTotal())
                .cuotaMensual(prestamo.getCuotaMensual())
                .estado(prestamo.getEstado())
                .estadoDescripcion(prestamo.getEstado() != null ? prestamo.getEstado().getDescripcion() : null)
                .fechaSolicitud(prestamo.getFechaSolicitud())
                .fechaAprobacion(prestamo.getFechaAprobacion())
                .cuotas(cuotasDTO)
                .build();
    }
}