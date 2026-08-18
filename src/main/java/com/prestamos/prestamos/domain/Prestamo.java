package com.prestamos.prestamos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un préstamo solicitado por un cliente.
 *
 * <p>Mapea la tabla {@code prestamos} y almacena tanto los datos
 * financieros del préstamo (monto, tasa, plazo, cuota mensual) como
 * su estado, fechas relevantes y el conjunto de cuotas asociadas.
 * Al persistirse, se asignan automáticamente la fecha de solicitud y
 * el estado {@link EstadoPrestamo#PENDIENTE} si aún no fueron definidos.</p>
 */
@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private Double monto;

    @Column(name = "tasa_interes", nullable = false)
    private Double tasaInteres;

    @Column(name = "plazo_meses", nullable = false)
    private Integer plazoMeses;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;

    @Column(name = "cuota_mensual", nullable = false)
    private Double cuotaMensual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoPrestamo estado;

    @Column(name = "fecha_solicitud", nullable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("numeroCuota ASC")
    private List<Cuota> cuotas = new ArrayList<>();

    /**
     * Hook del ciclo de vida JPA que se ejecuta antes de persistir la entidad.
     *
     * <p>Si la fecha de solicitud no fue establecida, se inicializa con la
     * fecha y hora actuales. Si el estado es {@code null}, se asigna
     * {@link EstadoPrestamo#PENDIENTE} como valor por defecto.</p>
     */
    @PrePersist
    public void prePersist() {
        if (this.fechaSolicitud == null) {
            this.fechaSolicitud = LocalDateTime.now();
        }
        if (this.estado == null) {
            this.estado = EstadoPrestamo.PENDIENTE;
        }
    }
}
