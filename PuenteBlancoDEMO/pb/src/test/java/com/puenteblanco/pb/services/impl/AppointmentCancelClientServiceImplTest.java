package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.AppointmentRescheduleRequestDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.entity.Veterinario;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentCancelClientServiceImplTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AppointmentCancelClientServiceImpl appointmentCancelClientService;

    @Test
    void rescheduleAppointment_debeActualizarFechaHoraMotivoYContadorCuandoHorarioEstaLibre() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("cliente@correo.com");

        User cliente = User.builder()
                .id(1L)
                .correo("cliente@correo.com")
                .build();

        Veterinario veterinario = Veterinario.builder()
                .id(5L)
                .build();

        LocalDate fechaOriginal = LocalDate.now().plusDays(2);
        LocalTime horaOriginal = LocalTime.of(9, 0);

        LocalDate nuevaFecha = LocalDate.now().plusDays(5);
        LocalTime nuevaHora = LocalTime.of(11, 30);

        Cita cita = Cita.builder()
                .id(100L)
                .usuario(cliente)
                .veterinario(veterinario)
                .estado("PROGRAMADA")
                .fecha(fechaOriginal)
                .hora(horaOriginal)
                .cantidadReprogramaciones(0)
                .build();

        AppointmentRescheduleRequestDto dto = new AppointmentRescheduleRequestDto();
        dto.setFecha(nuevaFecha.toString());
        dto.setHora(nuevaHora.toString());
        dto.setMotivoReprogramacion("Cruce de horario del cliente");

        when(citaRepository.findById(100L)).thenReturn(Optional.of(cita));
        when(citaRepository.countActiveAppointmentsAtSameSlot(5L, nuevaFecha, nuevaHora, 100L))
                .thenReturn(0L);

        appointmentCancelClientService.rescheduleAppointment(100L, dto, auth);

        assertEquals(fechaOriginal, cita.getFechaOriginal());
        assertEquals(horaOriginal, cita.getHoraOriginal());
        assertEquals(nuevaFecha, cita.getFecha());
        assertEquals(nuevaHora, cita.getHora());
        assertEquals("Cruce de horario del cliente", cita.getMotivoReprogramacion());
        assertEquals(1, cita.getCantidadReprogramaciones());

        verify(citaRepository).save(cita);
    }
}