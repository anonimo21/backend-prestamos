package com.prestamos.prestamos.data;

import com.prestamos.prestamos.domain.Cliente;
import com.prestamos.prestamos.domain.Role;
import com.prestamos.prestamos.domain.User;
import com.prestamos.prestamos.dto.PrestamoRequestDTO;
import com.prestamos.prestamos.repository.ClienteRepository;
import com.prestamos.prestamos.repository.RoleRepository;
import com.prestamos.prestamos.repository.UserRepository;
import com.prestamos.prestamos.service.IPrestamoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final IPrestamoService prestamoService;

    @Override
    @Transactional
    /**
     * Inicializa los roles, usuarios y datos de demostración cuando la
     * aplicación arranca y aún no existen en la base de datos.
     *
     * @param args argumentos de línea de comandos proporcionados por Spring Boot.
     */
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_ADMIN");
                    return roleRepository.save(newRole);
                });

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setUsername("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);
            admin.setRoles(adminRoles);

            userRepository.save(admin);
            System.out.println("Usuario 'admin' creado con éxito");
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setName("Usuario Normal");
            user.setUsername("user");
            user.setEmail("user@user.com");
            user.setPassword(passwordEncoder.encode("user"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            user.setRoles(userRoles);

            userRepository.save(user);
            System.out.println("Usuario 'user' creado con éxito");
        }

        // Seed demo clients and loans if database is empty
        if (clienteRepository.count() == 0) {
            Cliente c1 = new Cliente();
            c1.setIdentificacion("12345678A");
            c1.setNombre("Juan");
            c1.setApellido("Pérez");
            c1.setEmail("juan.perez@email.com");
            c1.setTelefono("+34 600 123 456");
            c1.setDireccion("Calle Mayor 10, Madrid");
            c1 = clienteRepository.save(c1);

            Cliente c2 = new Cliente();
            c2.setIdentificacion("87654321B");
            c2.setNombre("María");
            c2.setApellido("Gómez");
            c2.setEmail("maria.gomez@email.com");
            c2.setTelefono("+34 600 987 654");
            c2.setDireccion("Av. Diagonal 200, Barcelona");
            c2 = clienteRepository.save(c2);

            Cliente c3 = new Cliente();
            c3.setIdentificacion("45678912C");
            c3.setNombre("Carlos");
            c3.setApellido("Rodríguez");
            c3.setEmail("carlos.rodriguez@email.com");
            c3.setTelefono("+34 611 222 333");
            c3.setDireccion("Calle Gran Vía 45, Valencia");
            c3 = clienteRepository.save(c3);

            System.out.println("Clientes de prueba creados con éxito");

            // Seed demo loans
            prestamoService.crear(new PrestamoRequestDTO(c1.getId(), 15000.0, 8.5, 12));
            prestamoService.crear(new PrestamoRequestDTO(c2.getId(), 5000.0, 5.0, 6));
            prestamoService.crear(new PrestamoRequestDTO(c3.getId(), 25000.0, 10.0, 24));

            System.out.println("Préstamos y cuotas de prueba creados con éxito");
        }
    }
}
