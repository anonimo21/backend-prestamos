package com.prestamos.prestamos.security.controller;

import com.prestamos.prestamos.domain.User;
import com.prestamos.prestamos.security.dto.JwtAuthResponseDto;
import com.prestamos.prestamos.security.dto.LoginDto;
import com.prestamos.prestamos.security.dto.RegisterDto;
import com.prestamos.prestamos.security.dto.UserResponseDto;
import com.prestamos.prestamos.mapper.UserMapper;
import com.prestamos.prestamos.repository.UserRepository;
import com.prestamos.prestamos.security.jwt.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
/**
 * Expone las operaciones públicas de inicio de sesión y registro de usuarios.
 */
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping("/login")
    /**
     * Valida las credenciales recibidas y devuelve un token JWT para
     * autenticar las siguientes solicitudes.
     *
     * @param loginDto usuario y contraseña proporcionados en el inicio de sesión.
     * @return respuesta con el token de acceso generado.
     */
    public ResponseEntity<JwtAuthResponseDto> authenticateUser(@RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<>(new JwtAuthResponseDto(token),HttpStatus.OK);
    }

    @PostMapping("/register")
    /**
     * Registra un usuario nuevo tras verificar que su usuario y correo
     * electrónico no estén ya asociados a una cuenta existente.
     *
     * @param registerDto datos del usuario y los roles que se asignarán.
     * @return mensaje que confirma el registro o informa un conflicto.
     */
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Email de usuario ya existe", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.registerDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);
        return new ResponseEntity<>("Usuario registrado", HttpStatus.OK);
    }

    /**
     * Lista las cuentas registradas sin exponer credenciales ni otros datos
     * sensibles. El acceso se restringe a ROLE_ADMIN en SecurityConfig.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> users = userRepository.findAll().stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet())
                ))
                .toList();
        return ResponseEntity.ok(users);
    }

}
