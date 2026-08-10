package com.prestamos.prestamos.repository;

import com.prestamos.prestamos.model.EstadoPrestamo;
import com.prestamos.prestamos.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByClienteId(Long clienteId);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

    long countByEstado(EstadoPrestamo estado);

    @Query("SELECT COALESCE(SUM(p.monto), 0.0) FROM Prestamo p WHERE p.estado = 'APROBADO' OR p.estado = 'PAGADO'")
    Double obtenerMontoTotalPrestado();

    @Query("SELECT p FROM Prestamo p ORDER BY p.fechaSolicitud DESC")
    List<Prestamo> findTop5ByOrderByFechaSolicitudDesc();
}
