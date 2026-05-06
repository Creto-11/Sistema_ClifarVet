package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.AdminCreateServiceRequestDto;
import com.puenteblanco.pb.dto.response.AdminServiceResponseDto;
import com.puenteblanco.pb.entity.Servicio;
import com.puenteblanco.pb.entity.TipoServicio;
import com.puenteblanco.pb.exception.ResourceNotFoundException;
import com.puenteblanco.pb.repository.ServiceRepository;
import com.puenteblanco.pb.repository.TipoServicioRepository;
import com.puenteblanco.pb.services.interfaces.AdminServiceService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull; // Importante para la seguridad de nulos
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceServiceImpl implements AdminServiceService {

    private final ServiceRepository serviceRepository;
    private final TipoServicioRepository tipoServicioRepository;

    @Override
    public void registrarServicio(@NonNull AdminCreateServiceRequestDto dto) {
        TipoServicio tipo = tipoServicioRepository.findById(dto.getTipoServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de servicio no encontrado"));

        Servicio servicio = new Servicio();
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecioBase(BigDecimal.valueOf(dto.getPrecioBase()));
        servicio.setTipoServicio(tipo);
        servicio.setEstado(true);

        serviceRepository.save(servicio);
    }

    @Override
    public List<AdminServiceResponseDto> listarServicios() {
        return serviceRepository.findAll().stream().map(servicio -> {
            AdminServiceResponseDto dto = new AdminServiceResponseDto();
            dto.setId(servicio.getId());
            dto.setNombre(servicio.getDescripcion());
            dto.setPrecio(servicio.getPrecioBase());
            dto.setEstado(servicio.getEstado());
            if (servicio.getTipoServicio() != null) {
                dto.setCategoria(servicio.getTipoServicio().getNombre());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void editarServicio(@NonNull Long id, @NonNull AdminCreateServiceRequestDto dto) {
        Servicio servicio = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        TipoServicio tipo = tipoServicioRepository.findById(dto.getTipoServicioId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de servicio no encontrado"));

        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecioBase(BigDecimal.valueOf(dto.getPrecioBase()));
        servicio.setTipoServicio(tipo);

        serviceRepository.save(servicio);
    }

    @Override
    @Transactional
    public void cambiarEstadoServicio(@NonNull Long id) {
        Servicio servicio = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));

        if (servicio.getEstado() != null) {
            servicio.setEstado(!servicio.getEstado());
        }
        serviceRepository.save(servicio);
    }

    @Override
    public void eliminarServicio(@NonNull Long id) {
        Servicio servicio = serviceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado"));
        serviceRepository.delete(servicio);
    }
}