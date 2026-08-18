package com.prestamos.prestamos.repository;

import com.prestamos.prestamos.domain.Cuota;
import com.prestamos.prestamos.domain.EstadoCuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Cuota}.
 *
 * <p>Provee operaciones CRUD heredadas de {@link JpaRepository} y
 * consultas agregadas útiles para los reportes y cálculos del servicio.</p>
 */
@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    /**
     * Devuelve todas las cuotas asociadas a un préstamo, en el orden
     * natural devuelto por la base de datos.
     *
     * @param prestamoId identificador del préstamo.
     * @return lista (posiblemente vacía) de cuotas del préstamo.
     */
    List<Cuota> findByPrestamoId(Long prestamoId);

    /**
     * Indica si existe al menos una cuota del préstamo con el estado dado.
     *
     * <p>Se utiliza, por ejemplo, para saber si quedan cuotas
     * {@link EstadoCuota#PENDIENTE} o {@link EstadoCuota#VENCIDO} y
     * decidir si el préstamo puede marcarse como pagado.</p>
     *
     * @param prestamoId identificador del préstamo.
     * @param estado     estado a comprobar.
     * @return {@code true} si existe al menos una cuota que coincida.
     */
    boolean existsByPrestamoIdAndEstado(Long prestamoId, EstadoCuota estado);

    /**
     * Calcula la suma total de los montos de todas las cuotas en estado
     * {@link EstadoCuota#PAGADO} en todo el sistema.
     *
     * <p>Devuelve {@code 0.0} (vía {@code COALESCE}) cuando aún no hay
     * cuotas pagadas, evitando así un valor {@code null}.</p>
     *
     * @return monto total recaudado hasta el momento.
     */
    @Query("SELECT COALESCE(SUM(c.monto), 0.0) FROM Cuota c WHERE c.estado = 'PAGADO'")
    Double obtenerMontoTotalRecaudado();

    /**
     * Calcula la suma de los montos pagados de un préstamo específico.
     *
     * @param prestamoId identificador del préstamo a consultar.
     * @return monto acumulado pagado del préstamo (0.0 si aún no pagó nada).
     */
    @Query("SELECT COALESCE(SUM(c.monto), 0.0) FROM Cuota c WHERE c.prestamo.id = :prestamoId AND c.estado = 'PAGADO'")
    Double obtenerMontoPagadoPorPrestamo(@Param("prestamoId") Long prestamoId);
}
