package com.prestamos.prestamos.repository;

import com.prestamos.prestamos.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Cliente}.
 *
 * <p>Extiende {@link JpaRepository} por lo que hereda operaciones CRUD
 * estándar (guardar, buscar por id, listar, eliminar, etc.) y suma
 * métodos de búsqueda específicos para los casos de uso del servicio.</p>
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * Busca un cliente por su número de identificación (DNI / documento).
     *
     * @param identificacion documento único del cliente.
     * @return un {@link Optional} con el cliente encontrado o vacío si
     *         no existe ninguno con esa identificación.
     */
    Optional<Cliente> findByIdentificacion(String identificacion);

    /**
     * Verifica de forma eficiente si ya existe un cliente registrado con
     * la identificación indicada.
     *
     * <p>Se usa antes de crear un cliente para evitar duplicados sin
     * necesidad de traer toda la entidad.</p>
     *
     * @param identificacion documento a comprobar.
     * @return {@code true} si existe un cliente con esa identificación,
     *         {@code false} en caso contrario.
     */
    boolean existsByIdentificacion(String identificacion);

    /**
     * Realiza una búsqueda libre sobre nombre, apellido o identificación.
     *
     * <p>La coincidencia es parcial (LIKE '%texto%') y no distingue
     * mayúsculas/minúsculas para nombre y apellido, lo que facilita
     * búsquedas desde la API tipo "autocompletar".</p>
     *
     * @param keyword palabra clave a buscar; si es {@code null} o vacía
     *                el comportamiento depende de la base de datos
     *                subyacente.
     * @return lista de clientes que coincidan con el criterio.
     */
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :kw, '%')) OR c.identificacion LIKE CONCAT('%', :kw, '%')")
    List<Cliente> buscarPorNombreApellidoOIdentificacion(@Param("kw") String keyword);
}
