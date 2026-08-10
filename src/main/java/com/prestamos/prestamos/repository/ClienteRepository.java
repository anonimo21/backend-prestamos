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
    
    Optional<Cliente> findByDni(String dni);

    boolean existsByDni(String dni);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :kw, '%')) OR c.dni LIKE CONCAT('%', :kw, '%')")
    List<Cliente> buscarPorNombreApellidoODni(@Param("kw") String keyword);
}
