// VetHorarioUpdateServiceImpl.java
package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.HorarioUpdateRequest;
import com.puenteblanco.pb.entity.Horario;
import com.puenteblanco.pb.entity.Veterinario;
import com.puenteblanco.pb.repository.HorarioRepository;
import com.puenteblanco.pb.repository.VeterinarioRepository;
import com.puenteblanco.pb.services.interfaces.VetHorarioUpdateService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VetHorarioUpdateServiceImpl implements VetHorarioUpdateService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private VeterinarioRepository veterinarioRepository;

    @Override
    @Transactional
    public void actualizarHorariosSemanaSiguiente(List<HorarioUpdateRequest> nuevosHorarios, String correoVeterinario) {
        Veterinario vet = veterinarioRepository.findByUsuarioCorreo(correoVeterinario)
                .orElseThrow(() -> new RuntimeException("Veterinario no encontrado."));

        for (HorarioUpdateRequest dto : nuevosHorarios) {
            Horario horarioExistente = horarioRepository.findByVeterinarioAndDiaSemana(vet, dto.getDiaSemana())
                    .orElse(Horario.builder()
                            .diaSemana(dto.getDiaSemana())
                            .veterinario(vet)
                            .build());

            horarioExistente.setHoraComienzo(dto.getHoraComienzo());
            horarioExistente.setHoraFin(dto.getHoraFin());
            horarioExistente.setEstado(true);
            horarioRepository.save(horarioExistente);
        }
    }
}
