package com.prestamos.prestamos.service;

import com.prestamos.prestamos.dto.ClienteRequestDTO;
import com.prestamos.prestamos.dto.ClienteResponseDTO;

import java.util.List;

/**
 * Contrato del servicio de clientes.
 *
 * <p>Define las operaciones de negocio disponibles para la gestión de
 * clientes. La implementación concreta
 * ({@link ClienteServiceImpl}) reside en el mismo paquete.</p>
 */
public interface IClienteService {

    /**
     * Lista todos los clientes registrados en el sistema.
     *
     * @return lista (posiblemente vacía) de {@link ClienteResponseDTO}.
     */
    List<ClienteResponseDTO> listarTodos();

    /**
     * Busca clientes aplicando un filtro libre de texto.
     *
     * <p>El filtro se compara contra nombre, apellido e identificación.
     * Si el filtro es {@code null} o vacío se devuelven todos los
     * clientes.</p>
     *
     * @param keyword palabra clave a buscar.
     * @return lista de clientes que coinciden.
     */
    List<ClienteResponseDTO> buscarPorFiltro(String keyword);

    /**
     * Obtiene un cliente por su identificador.
     *
     * @param id identificador del cliente.
     * @return el cliente encontrado.
     * @throws com.prestamos.prestamos.exception.ResourceNotFoundException
     *         si no existe un cliente con ese id.
     */
    ClienteResponseDTO obtenerPorId(Long id);

    /**
     * Crea un nuevo cliente a partir de los datos recibidos.
     *
     * @param dto datos del cliente a crear.
     * @return el cliente ya persistido.
     * @throws com.prestamos.prestamos.exception.DuplicateResourceException
     *         si ya existe un cliente con la misma identificación.
     */
    ClienteResponseDTO crear(ClienteRequestDTO dto);

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id  identificador del cliente a actualizar.
     * @param dto nuevos valores a aplicar.
     * @return el cliente actualizado.
     * @throws com.prestamos.prestamos.exception.ResourceNotFoundException
     *         si el cliente no existe.
     * @throws com.prestamos.prestamos.exception.DuplicateResourceException
     *         si la nueva identificación ya está usada por otro cliente.
     */
    ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto);

    /**
     * Elimina un cliente del sistema.
     *
     * @param id identificador del cliente a eliminar.
     * @throws com.prestamos.prestamos.exception.ResourceNotFoundException
     *         si el cliente no existe.
     * @throws com.prestamos.prestamos.exception.BusinessRuleException
     *         si el cliente tiene préstamos asociados.
     */
    void eliminar(Long id);
}
