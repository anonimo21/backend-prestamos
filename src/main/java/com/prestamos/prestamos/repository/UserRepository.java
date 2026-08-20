package com.prestamos.prestamos.repository;

import com.prestamos.prestamos.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de usuarios utilizado por el registro y la autenticación.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recupera un usuario a partir de su nombre de inicio de sesión.
     *
     * @param username nombre de usuario.
     * @return el usuario encontrado o vacío si no existe.
     */
    Optional<User> findByUsername(String username);

    /**
     * Comprueba si un nombre de usuario ya está registrado.
     *
     * @param username nombre de usuario a validar.
     * @return {@code true} si el nombre ya está en uso.
     */
    Boolean existsByUsername(String username);

    /**
     * Comprueba si una dirección de correo ya pertenece a un usuario.
     *
     * @param email correo electrónico a validar.
     * @return {@code true} si el correo ya está en uso.
     */
    Boolean existsByEmail(String email);

}
