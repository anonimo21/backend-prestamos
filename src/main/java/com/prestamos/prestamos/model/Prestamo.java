package com.prestamos.prestamos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Double tasaInteres; // Porcentaje de interés (ej: 10.0 %)

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
