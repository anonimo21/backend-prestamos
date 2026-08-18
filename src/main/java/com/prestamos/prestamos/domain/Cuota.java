package com.prestamos.prestamos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa una cuota individual de un préstamo.
 *
 * <p>Mapea la tabla {@code cuotas} y almacena el número de cuota, el
 * monto a pagar, la fecha de vencimiento, el estado actual
 * ({@link EstadoCuota}) y la fecha en la que fue pagada (si aplica).
 * Al persistirse, si el estado es {@code null}, se asigna
 * {@link EstadoCuota#PENDIENTE} por defecto.</p>
 */
@Entity
@Table(name = "cuotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestamo_id", nullable = false)
    private Prestamo prestamo;

    @Column(name = "numero_cuota", nullable = false)
    private Integer numeroCuota;

    @Column(nullable = false)
    private Double monto;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCuota estado;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    /**
     * Hook del ciclo de vida JPA que se ejecuta antes de persistir la entidad.
     *
     * <p>Inicializa el estado en {@link EstadoCuota#PENDIENTE} cuando aún
     * no fue establecido, garantizando que cada cuota nueva nazca como
     * pendiente de pago.</p>
     */
    @PrePersist
    public void prePersist() {
        if (this.estado == null) {
            this.estado = EstadoCuota.PENDIENTE;
        }
    }
}
