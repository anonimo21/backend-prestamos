package com.prestamos.prestamos.service;

import com.prestamos.prestamos.domain.Cliente;
import com.prestamos.prestamos.domain.Cuota;
import com.prestamos.prestamos.domain.EstadoCuota;
import com.prestamos.prestamos.domain.EstadoPrestamo;
import com.prestamos.prestamos.domain.Prestamo;
import com.prestamos.prestamos.dto.PrestamoRequestDTO;
import com.prestamos.prestamos.dto.PrestamoResponseDTO;
import com.prestamos.prestamos.exception.ResourceNotFoundException;
import com.prestamos.prestamos.mapper.PrestamoMapper;
import com.prestamos.prestamos.repository.ClienteRepository;
import com.prestamos.prestamos.repository.PrestamoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrestamoServiceImpl implements IPrestamoService {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoServiceImpl.class);

    private final PrestamoRepository prestamoRepository;
    private final ClienteRepository clienteRepository;
    private final PrestamoMapper prestamoMapper;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository,
                               ClienteRepository clienteRepository,
                               PrestamoMapper prestamoMapper) {
        this.prestamoRepository = prestamoRepository;
        this.clienteRepository = clienteRepository;
        this.prestamoMapper = prestamoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listarTodos() {
        return prestamoRepository.findAll().stream()
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> listarPorEstado(EstadoPrestamo estado) {
        return prestamoRepository.findAll().stream()
                .filter(p -> p.getEstado() == estado)
                .map(prestamoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponseDTO obtenerPorId(Long id) {
        Prestamo p = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));
        return prestamoMapper.toResponseDTO(p);
    }

    @Override
    @Transactional
    public PrestamoResponseDTO crear(PrestamoRequestDTO dto) {
        logger.info("Creando préstamo para el cliente ID: {}", dto.getClienteId());

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + dto.getClienteId()));

        double interesTotal = dto.getMonto() * (dto.getTasaInteres() / 100.0);
        double montoTotal = dto.getMonto() + interesTotal;
        double cuotaMensual = Math.round((montoTotal / dto.getPlazoMeses()) * 100.0) / 100.0;

        Prestamo prestamo = new Prestamo();
        prestamo.setCliente(cliente);
        prestamo.setMonto(dto.getMonto());
        prestamo.setTasaInteres(dto.getTasaInteres());
        prestamo.setPlazoMeses(dto.getPlazoMeses());
        prestamo.setMontoTotal(montoTotal);
        prestamo.setCuotaMensual(cuotaMensual);
        prestamo.setEstado(EstadoPrestamo.PENDIENTE);
        prestamo.setFechaSolicitud(LocalDateTime.now());

        List<Cuota> cuotas = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        double sumaCuotas = 0.0;

        for (int i = 1; i <= dto.getPlazoMeses(); i++) {
            Cuota cuota = new Cuota();
            cuota.setPrestamo(prestamo);
            cuota.setNumeroCuota(i);

            if (i == dto.getPlazoMeses()) {
                cuota.setMonto(Math.round((montoTotal - sumaCuotas) * 100.0) / 100.0);
            } else {
                cuota.setMonto(cuotaMensual);
                sumaCuotas += cuotaMensual;
            }

            cuota.setFechaVencimiento(hoy.plusMonths(i));
            cuota.setEstado(EstadoCuota.PENDIENTE);
            cuotas.add(cuota);
        }

        prestamo.setCuotas(cuotas);
        Prestamo guardado = prestamoRepository.save(prestamo);
        logger.info("Préstamo creado con ID: {} y {} cuotas generadas", guardado.getId(), cuotas.size());
        return prestamoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional
    public PrestamoResponseDTO actualizar(Long id, PrestamoRequestDTO dto) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + dto.getClienteId()));

        double interesTotal = dto.getMonto() * (dto.getTasaInteres() / 100.0);
        double montoTotal = dto.getMonto() + interesTotal;
        double cuotaMensual = Math.round((montoTotal / dto.getPlazoMeses()) * 100.0) / 100.0;

        prestamo.setCliente(cliente);
        prestamo.setMonto(dto.getMonto());
        prestamo.setTasaInteres(dto.getTasaInteres());
        prestamo.setPlazoMeses(dto.getPlazoMeses());
        prestamo.setMontoTotal(montoTotal);
        prestamo.setCuotaMensual(cuotaMensual);

        Prestamo actualizado = prestamoRepository.save(prestamo);
        logger.info("Préstamo actualizado con ID: {}", actualizado.getId());
        return prestamoMapper.toResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));
        prestamoRepository.delete(prestamo);
        logger.info("Préstamo eliminado con ID: {}", id);
    }
}
