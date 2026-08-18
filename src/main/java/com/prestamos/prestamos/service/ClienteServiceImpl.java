package com.prestamos.prestamos.service;

import com.prestamos.prestamos.domain.Cliente;
import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;
import com.prestamos.prestamos.exception.BusinessRuleException;
import com.prestamos.prestamos.exception.DuplicateResourceException;
import com.prestamos.prestamos.exception.ResourceNotFoundException;
import com.prestamos.prestamos.mapper.ClienteMapper;
import com.prestamos.prestamos.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de {@link IClienteService}.
 *
 * <p>Centraliza las reglas de negocio para clientes (unicidad de
 * identificación, borrado seguro) y delega el acceso a datos en
 * {@link ClienteRepository} y la conversión a DTOs en
 * {@link ClienteMapper}.</p>
 */
@Service
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param clienteRepository repositorio de clientes.
     * @param clienteMapper     mapper entre entidad y DTOs.
     */
    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Devuelve todos los clientes del sistema mapeados a DTO de respuesta.
     *
     * <p>Se ejecuta dentro de una transacción de solo lectura.</p>
     *
     * @return lista con todos los clientes.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Aplica un filtro libre sobre los clientes existentes.
     *
     * <p>Si {@code keyword} es {@code null} o queda vacía tras
     * {@code trim()}, devuelve todos los clientes (mismo comportamiento
     * que {@link #listarTodos()}).</p>
     *
     * @param keyword texto a buscar en nombre, apellido o identificación.
     * @return lista de clientes que coinciden con el filtro.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarPorFiltro(String keyword) {
        List<Cliente> clientes = clienteRepository.findAll();

        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim().toLowerCase();
            clientes = clientes.stream()
                    .filter(c -> c.getNombre().toLowerCase().contains(kw)
                            || c.getApellido().toLowerCase().contains(kw)
                            || c.getIdentificacion().contains(kw))
                    .collect(Collectors.toList());
        }

        return clientes.stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un cliente por id.
     *
     * @param id identificador del cliente.
     * @return el cliente encontrado como DTO.
     * @throws ResourceNotFoundException si no existe.
     */
    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toResponseDTO(cliente);
    }

    /**
     * Crea un cliente, validando que su identificación no esté repetida.
     *
     * <p>La comprobación de unicidad se hace recorriendo los clientes
     * existentes; podría sustituirse por el método
     * {@code existsByIdentificacion} del repositorio.</p>
     *
     * @param dto datos del cliente a crear.
     * @return el cliente persistido.
     * @throws DuplicateResourceException si la identificación ya existe.
     */
    @Override
    @Transactional
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        boolean existe = clienteRepository.findAll().stream()
                .anyMatch(c -> c.getIdentificacion().equalsIgnoreCase(dto.getIdentificacion()));

        if (existe) {
            throw new DuplicateResourceException("Ya existe un cliente registrado con la identificación: " + dto.getIdentificacion());
        }
        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(guardado);
    }

    /**
     * Actualiza los datos de un cliente.
     *
     * <p>Si la identificación cambia, se valida que el nuevo valor no
     * esté siendo usado por otro cliente.</p>
     *
     * @param id  identificador del cliente a actualizar.
     * @param dto nuevos valores.
     * @return el cliente actualizado.
     * @throws ResourceNotFoundException si el cliente no existe.
     * @throws DuplicateResourceException si la nueva identificación ya existe.
     */
    @Override
    @Transactional
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (!cliente.getIdentificacion().equalsIgnoreCase(dto.getIdentificacion())) {
            boolean existe = clienteRepository.findAll().stream()
                    .anyMatch(c -> c.getIdentificacion().equalsIgnoreCase(dto.getIdentificacion()));

            if (existe) {
                throw new DuplicateResourceException("Ya existe otro cliente registrado con la identificación: " + dto.getIdentificacion());
            }
        }

        clienteMapper.updateEntityFromDTO(cliente, dto);
        Cliente actualizado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(actualizado);
    }

    /**
     * Elimina un cliente, siempre que no tenga préstamos asociados.
     *
     * @param id identificador del cliente a eliminar.
     * @throws ResourceNotFoundException si el cliente no existe.
     * @throws BusinessRuleException     si el cliente tiene préstamos.
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        if (cliente.getPrestamos() != null && !cliente.getPrestamos().isEmpty()) {
            throw new BusinessRuleException("No se puede eliminar el cliente porque tiene préstamos asociados.");
        }
        clienteRepository.delete(cliente);
    }
}
