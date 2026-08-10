package com.prestamos.prestamos.repository;

import com.prestamos.prestamos.model.Cuota;
import com.prestamos.prestamos.model.EstadoCuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    List<Cuota> findByPrestamoId(Long prestamoId);

    boolean existsByPrestamoIdAndEstado(Long prestamoId, EstadoCuota estado);

    @Query("SELECT COALESCE(SUM(c.monto), 0.0) FROM Cuota c WHERE c.estado = 'PAGADO'")
    Double obtenerMontoTotalRecaudado();

    @Query("SELECT COALESCE(SUM(c.monto), 0.0) FROM Cuota c WHERE c.prestamo.id = :prestamoId AND c.estado = 'PAGADO'")
    Double obtenerMontoPagadoPorPrestamo(@Param("prestamoId") Long prestamoId);
}
