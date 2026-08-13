package com.prestamos.prestamos.mapper;

import com.prestamos.prestamos.dto.CuotaResponseDTO;
import com.prestamos.prestamos.domain.Cuota;
import org.springframework.stereotype.Component;

@Component
public class CuotaMapper {

    public CuotaResponseDTO toResponseDTO(Cuota cuota) {
        if (cuota == null) return null;
        return CuotaResponseDTO.builder()
                .id(cuota.getId())
                .numeroCuota(cuota.getNumeroCuota())
                .monto(cuota.getMonto())
                .fechaVencimiento(cuota.getFechaVencimiento())
                .estado(cuota.getEstado())
                .estadoDescripcion(cuota.getEstado() != null ? cuota.getEstado().getDescripcion() : null)
                .fechaPago(cuota.getFechaPago())
                .build();
    }
}