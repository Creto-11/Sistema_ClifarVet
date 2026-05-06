package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.response.InternAppointmentResponseDto;
import com.puenteblanco.pb.dto.response.InternCitaValidadaResponseDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.services.interfaces.InternAppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InternAppointmentServiceImpl implements InternAppointmentService {

    private final CitaRepository citaRepository;
    private final UserRepository userRepository;

    public InternAppointmentServiceImpl(CitaRepository citaRepository, UserRepository userRepository) {
        this.citaRepository = citaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<InternAppointmentResponseDto> getCitasDerivadasDelInterno(Long internId) {
        List<Cita> citas = citaRepository.findByIntern_IdAndEstado(internId, "DERIVADA");

        return citas.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<InternCitaValidadaResponseDto> getCitasValidadasPorIntern(Long internId) {
        List<Cita> citas = citaRepository.findCitasValidadasPorIntern(internId);

        return citas.stream().map(cita -> new InternCitaValidadaResponseDto(
                cita.getFecha().toString() + " " + cita.getHora().toString(),
                cita.getPet().getName(),
                cita.getServicio().getDescripcion(),
                cita.getVeterinario().getUsuario().getNombres() + " " + cita.getVeterinario().getUsuario().getApellidoPaterno(),
                cita.getEstado(),
                cita.getUsuario().getNombres() + " " + cita.getUsuario().getApellidoPaterno()
        )).collect(Collectors.toList());
    }

    @Override
    public List<InternAppointmentResponseDto> getCitasDerivadasNoVistas(Long internId) {
        User intern = userRepository.findById(internId)
                .orElseThrow(() -> new RuntimeException("Intern no encontrado"));

        List<Cita> citas = citaRepository.findDerivadasNoVistas(intern, "DERIVADA");

        return citas.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
public void marcarCitasDerivadasComoVistas(Long internId) {
    User intern = userRepository.findById(internId)
            .orElseThrow(() -> new RuntimeException("Intern no encontrado"));

    List<Cita> citasNoVistas = citaRepository.findDerivadasNoVistas(intern, "DERIVADA");

    // Marcar como visto
    citasNoVistas.forEach(cita -> cita.setVistoInterno(true));
    citaRepository.saveAll(citasNoVistas);
}


    private InternAppointmentResponseDto mapToDto(Cita cita) {
        return InternAppointmentResponseDto.builder()
                .citaId(cita.getId())
                .nombreCliente(cita.getUsuario().getNombres() + " " +
                               cita.getUsuario().getApellidoPaterno() + " " +
                               cita.getUsuario().getApellidoMaterno())
                .nombreMascota(cita.getPet().getName())
                .tipoMascota(cita.getPet().getType())
                .razaMascota(cita.getPet().getBreed())
                .edadMascota(cita.getPet().getAge())
                .servicio(cita.getServicio().getDescripcion())
                .fecha(cita.getFecha().toString())
                .hora(cita.getHora().toString())
                .veterinarioACargo(cita.getVeterinario().getUsuario().getNombres() + " " +
                                   cita.getVeterinario().getUsuario().getApellidoPaterno())
                .estado(cita.getEstado())
                .build();
    }
}
 