package com.prestamos.prestamos.dto;

import com.prestamos.prestamos.model.EstadoPrestamo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrestamoResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private Double monto;
    private Double tasaInteres;
    private Integer plazoMeses;
    private Double montoTotal;
    private Double cuotaMensual;
    private EstadoPrestamo estado;
    private String estadoDescripcion;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaAprobacion;
    private List<CuotaResponseDTO> cuotas;
}
