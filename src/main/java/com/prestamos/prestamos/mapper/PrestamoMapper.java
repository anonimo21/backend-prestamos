package com.prestamos.prestamos.mapper;

import com.prestamos.prestamos.dto.CuotaResponseDTO;
import com.prestamos.prestamos.dto.PrestamoResponseDTO;
import com.prestamos.prestamos.domain.Prestamo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Componente que convierte la entidad {@link Prestamo} en
 * {@link PrestamoResponseDTO}.
 *
 * <p>Delega en {@link ClienteMapper} y {@link CuotaMapper} la conversión
 * de las entidades anidadas para mantener la responsabilidad única
 * de cada mapper.</p>
 */
@Component
public class PrestamoMapper {

    private final ClienteMapper clienteMapper;
    private final CuotaMapper cuotaMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param clienteMapper mapper de clientes para convertir el cliente asociado.
     * @param cuotaMapper   mapper de cuotas para convertir el plan de pagos.
     */
    @Autowired
    public PrestamoMapper(ClienteMapper clienteMapper, CuotaMapper cuotaMapper) {
        this.clienteMapper = clienteMapper;
        this.cuotaMapper = cuotaMapper;
    }

    /**
     * Convierte un {@link Prestamo} en su DTO de respuesta, incluyendo
     * el cliente asociado y la lista de cuotas.
     *
     * <p>Si el préstamo no tiene cuotas cargadas, se devuelve una lista
     * vacía en lugar de {@code null}, simplificando el manejo en el
     * cliente de la API.</p>
     *
     * @param prestamo entidad a transformar (puede ser {@code null}).
     * @return DTO equivalente, o {@code null} si la entrada es nula.
     */
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
