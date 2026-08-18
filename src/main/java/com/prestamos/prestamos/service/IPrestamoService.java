package com.prestamos.prestamos.service;

import com.prestamos.prestamos.domain.EstadoPrestamo;
import com.prestamos.prestamos.dto.PrestamoRequestDTO;
import com.prestamos.prestamos.dto.PrestamoResponseDTO;

import java.util.List;

/**
 * Contrato del servicio de préstamos.
 *
 * <p>La implementación ({@link PrestamoServiceImpl}) se encarga de
 * calcular el plan de pagos, generar las cuotas y aplicar las reglas
 * de negocio correspondientes.</p>
 */
public interface IPrestamoService {

    /**
     * Lista todos los préstamos registrados.
     *
     * @return lista (posiblemente vacía) de préstamos.
     */
    List<PrestamoResponseDTO> listarTodos();

    /**
     * Filtra los préstamos por su estado.
     *
     * @param estado estado por el que filtrar (PENDIENTE, APROBADO, etc.).
     * @return lista de préstamos con ese estado.
     */
    List<PrestamoResponseDTO> listarPorEstado(EstadoPrestamo estado);

    /**
     * Obtiene un préstamo por su identificador.
     *
     * @param id identificador del préstamo.
     * @return el préstamo encontrado.
     * @throws com.prestamos.prestamos.exception.ResourceNotFoundException
     *         si no existe.
     */
    PrestamoResponseDTO obtenerPorId(Long id);

    /**
     * Crea un nuevo préstamo, calcula el plan de cuotas y lo persiste.
     *
     * @param dto datos del préstamo a crear.
     * @return el préstamo creado con su plan de cuotas.
     * @throws com.prestamos.prestamos.exception.ResourceNotFoundException
     *         si el cliente indicado no existe.
     */
    PrestamoResponseDTO crear(PrestamoRequestDTO dto);

    /**
     * Actualiza los datos de un préstamo existente y recalcula las
     * cuotas afectadas por los nuevos valores.
     *
     * @param id  identificador del préstamo a actualizar.
     * @param dto nuevos valores del préstamo.
     * @return el préstamo actualizado.
     * @throws com.prestamos.prestamos.exception.ResourceNotFoundException
     *         si el préstamo o el cliente no existen.
     */
    PrestamoResponseDTO actualizar(Long id, PrestamoRequestDTO dto);

    /**
     * Elimina un préstamo del sistema.
     *
     * @param id identificador del préstamo a eliminar.
     * @throws com.prestamos.prestamos.exception.ResourceNotFoundException
     *         si el préstamo no existe.
     */
    void eliminar(Long id);
}
