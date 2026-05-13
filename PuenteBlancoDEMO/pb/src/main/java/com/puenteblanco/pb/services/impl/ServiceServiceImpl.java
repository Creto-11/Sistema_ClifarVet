package com.puenteblanco.pb.services.impl;
import com.puenteblanco.pb.entity.Servicio;
import com.puenteblanco.pb.repository.ServiceRepository;
import com.puenteblanco.pb.services.interfaces.ServiceService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<Servicio> obtenerTodos() {
        return serviceRepository.findByEstadoTrue();
    }
}

