package com.prestamos.prestamos.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un cliente del sistema de préstamos.
 *
 * <p>Mapea la tabla {@code clientes} y mantiene la información personal
 * junto con la lista de préstamos asociados. La fecha de registro se
 * asigna automáticamente al persistir la entidad por primera vez.</p>
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String identificacion;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(length = 255)
    private String direccion;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prestamo> prestamos = new ArrayList<>();

    /**
     * Hook del ciclo de vida JPA que se ejecuta antes de persistir la entidad.
     *
     * <p>Si la fecha de registro aún no fue establecida, se inicializa con
     * la fecha y hora actuales. Esto garantiza que {@code fechaRegistro}
     * siempre tenga un valor al guardar el cliente.</p>
     */
    @PrePersist
    public void prePersist() {
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
    }

    /**
     * Devuelve el nombre completo del cliente concatenando nombre y apellido.
     *
     * <p>Si alguno de los dos campos es {@code null}, se reemplaza por una
     * cadena vacía para evitar un valor literal {@code "null"} en la salida.</p>
     *
     * @return nombre completo del cliente (ej.: "Juan Pérez").
     */
    public String getNombreCompleto() {
        return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
    }
}
