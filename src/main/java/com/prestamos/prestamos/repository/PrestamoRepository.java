package com.prestamos.prestamos.repository;

import com.prestamos.prestamos.domain.EstadoPrestamo;
import com.prestamos.prestamos.domain.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Prestamo}.
 *
 * <p>Además de las operaciones CRUD provistas por
 * {@link JpaRepository}, expone búsquedas filtradas por cliente y
 * estado, conteos y agregaciones usadas por el servicio y los
 * endpoints de reportes.</p>
 */
@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /**
     * Devuelve todos los préstamos asociados a un cliente.
     *
     * @param clienteId identificador del cliente.
     * @return lista (posiblemente vacía) de préstamos del cliente.
     */
    List<Prestamo> findByClienteId(Long clienteId);

    /**
     * Filtra los préstamos cuyo estado coincide con el parámetro.
     *
     * <p>Útil para listar, por ejemplo, todos los préstamos
     * {@link EstadoPrestamo#PENDIENTE} que aún no fueron aprobados.</p>
     *
     * @param estado estado por el que se desea filtrar.
     * @return lista de préstamos con ese estado.
     */
    List<Prestamo> findByEstado(EstadoPrestamo estado);

    /**
     * Cuenta cuántos préstamos existen en el estado indicado.
     *
     * @param estado estado a contar.
     * @return número de préstamos en ese estado.
     */
    long countByEstado(EstadoPrestamo estado);

    /**
     * Suma el monto de todos los préstamos que están efectivamente
     * vigentes o saldados ({@link EstadoPrestamo#APROBADO} o
     * {@link EstadoPrestamo#PAGADO}).
     *
     * <p>Los pendientes y rechazados no se incluyen porque no representan
     * dinero desembolsado.</p>
     *
     * @return monto total prestado a la fecha.
     */
    @Query("SELECT COALESCE(SUM(p.monto), 0.0) FROM Prestamo p WHERE p.estado = 'APROBADO' OR p.estado = 'PAGADO'")
    Double obtenerMontoTotalPrestado();

    /**
     * Devuelve los 5 préstamos más recientes ordenados por fecha de
     * solicitud de manera descendente.
     *
     * <p>Se usa típicamente para alimentar un panel/dashboard con los
     * últimos préstamos creados.</p>
     *
     * @return lista con hasta 5 préstamos ordenados del más reciente al más antiguo.
     */
    @Query("SELECT p FROM Prestamo p ORDER BY p.fechaSolicitud DESC")
    List<Prestamo> findTop5ByOrderByFechaSolicitudDesc();
}
