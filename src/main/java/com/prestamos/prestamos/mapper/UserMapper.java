package com.prestamos.prestamos.mapper;

import com.prestamos.prestamos.domain.Role;
import com.prestamos.prestamos.domain.User;
import com.prestamos.prestamos.exception.ResourceNotFoundException;
import com.prestamos.prestamos.security.dto.RegisterDto;
import com.prestamos.prestamos.repository.RoleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Convierte los datos de registro en usuarios persistibles y resuelve
 * los nombres de roles enviados por la API a sus entidades {@link Role}.
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected RoleRepository roleRepository;

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "registerDto.roles", qualifiedByName = "mapRoleStringsToRoles")
    /**
     * Crea un usuario a partir del DTO de registro, sin copiar la
     * contraseña ni el identificador generado por la base de datos.
     *
     * @param registerDto datos enviados para crear el usuario.
     * @return usuario listo para completar y persistir.
     */
    public abstract User registerDtoToUser(RegisterDto registerDto);

    @Named("mapRoleStringsToRoles")
    /**
     * Busca las entidades de los roles indicados por nombre. Cuando no
     * se recibe ningún rol, asigna {@code ROLE_USER} por defecto.
     *
     * @param roleNames nombres de roles, por ejemplo {@code ROLE_ADMIN}.
     * @return conjunto de roles existentes en la base de datos.
     * @throws ResourceNotFoundException si alguno de los roles no existe.
     */
    public Set<Role> mapRoleStringsToRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return roleRepository.findByName("ROLE_USER")
                    .map(Collections::singleton)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Rol 'ROLE_USER' no encontrado en la base de datos, asegurate de que ROLE_USER exista"));
        }

        return roleNames.stream()
                .map(
                        roleName -> roleRepository.findByName(roleName)
                                .orElseThrow(
                                        () -> new ResourceNotFoundException("Error : Rol no encontrado " + roleName)
                                ))
                .collect(Collectors.toSet());
    }
}
