package com.prestamos.prestamos.repository;

import com.prestamos.prestamos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByIdentificacion(String identificacion);

    boolean existsByIdentificacion(String identificacion);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :kw, '%')) OR c.identificacion LIKE CONCAT('%', :kw, '%')")
    List<Cliente> buscarPorNombreApellidoOIdentificacion(@Param("kw") String keyword);
}
