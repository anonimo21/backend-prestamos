package com.prestamos.prestamos.repository;

import com.prestamos.prestamos.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de los roles disponibles para los usuarios del sistema.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre técnico, como {@code ROLE_USER}.
     *
     * @param name nombre único del rol.
     * @return el rol encontrado o un resultado vacío si no existe.
     */
    Optional<Role> findByName(String name);

}
