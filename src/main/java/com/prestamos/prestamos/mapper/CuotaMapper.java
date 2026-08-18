package com.prestamos.prestamos.mapper;

import com.prestamos.prestamos.dto.CuotaResponseDTO;
import com.prestamos.prestamos.domain.Cuota;
import org.springframework.stereotype.Component;

/**
 * Componente que convierte la entidad {@link Cuota} en
 * {@link CuotaResponseDTO}.
 */
@Component
public class CuotaMapper {

    /**
     * Transforma una cuota persistida en el DTO que se expone por la API.
     *
     * <p>Agrega la descripción legible del estado para evitar que el
     * cliente de la API tenga que traducir el enum.</p>
     *
     * @param cuota entidad a convertir (puede ser {@code null}).
     * @return DTO equivalente, o {@code null} si la entrada es nula.
     */
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
