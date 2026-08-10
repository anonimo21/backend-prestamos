package com.prestamos.prestamos.dto;

import com.prestamos.prestamos.model.EstadoCuota;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuotaResponseDTO {

    private Long id;
    private Integer numeroCuota;
    private Double monto;
    private LocalDate fechaVencimiento;
    private EstadoCuota estado;
    private String estadoDescripcion;
    private LocalDate fechaPago;
}
