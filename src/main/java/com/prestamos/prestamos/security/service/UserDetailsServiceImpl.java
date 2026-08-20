package com.prestamos.prestamos.security.service;

import com.prestamos.prestamos.domain.Role;
import com.prestamos.prestamos.domain.User;
import com.prestamos.prestamos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
/**
 * Adapta los usuarios persistidos al formato que Spring Security usa
 * durante el proceso de autenticación.
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    /**
     * Busca un usuario y crea sus credenciales de seguridad con las
     * autoridades derivadas de sus roles.
     *
     * @param username nombre del usuario que se desea autenticar.
     * @return credenciales y autoridades compatibles con Spring Security.
     * @throws UsernameNotFoundException si no existe ese usuario.
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ese username:" + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles())
        );
    }

    /**
     * Convierte los roles del dominio en autoridades que pueden ser
     * evaluadas por las reglas de autorización de Spring Security.
     *
     * @param roles roles asignados al usuario.
     * @return colección de autoridades equivalentes.
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

}
