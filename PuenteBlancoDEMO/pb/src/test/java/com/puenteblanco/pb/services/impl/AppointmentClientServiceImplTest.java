package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.dto.request.AppointmentRequestDto;
import com.puenteblanco.pb.dto.response.AppointmentBookingResponseDto;
import com.puenteblanco.pb.entity.Cita;
import com.puenteblanco.pb.entity.Pet;
import com.puenteblanco.pb.entity.Servicio;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.entity.Veterinario;
import com.puenteblanco.pb.repository.CitaRepository;
import com.puenteblanco.pb.repository.PetRepository;
import com.puenteblanco.pb.repository.ServiceRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.repository.VeterinarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentClientServiceImplTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private ServiceRepository servicioRepository;

    @Mock
    private VeterinarioRepository veterinarioRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private AppointmentClientServiceImpl appointmentClientService;

    @Test
    void bookAppointment_debeRegistrarCitaPendienteDePagoConPrecioDelServicio() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("cliente@correo.com");

        AppointmentRequestDto dto = new AppointmentRequestDto();
        dto.setServicioId(10L);
        dto.setVeterinarioId(20L);
        dto.setPetId(30L);
        dto.setFecha("2026-07-15");
        dto.setHora("10:30");

        User cliente = User.builder()
                .id(1L)
                .correo("cliente@correo.com")
                .nombres("Carlos")
                .build();

        Servicio servicio = Servicio.builder()
                .id(10L)
                .descripcion("Consulta general")
                .precioBase(new BigDecimal("50.00"))
                .build();

        Veterinario veterinario = Veterinario.builder()
                .id(20L)
                .usuario(User.builder().nombres("Ana").build())
                .build();

        Pet mascota = Pet.builder()
                .id(30L)
                .petId("PET-001")
                .name("Firulais")
                .ownerEmail("cliente@correo.com")
                .build();

        when(userRepository.findByCorreo("cliente@correo.com")).thenReturn(Optional.of(cliente));
        when(servicioRepository.findById(10L)).thenReturn(Optional.of(servicio));
        when(veterinarioRepository.findById(20L)).thenReturn(Optional.of(veterinario));
        when(petRepository.findById(30L)).thenReturn(Optional.of(mascota));

        when(citaRepository.save(any(Cita.class))).thenAnswer(invocation -> {
            Cita cita = invocation.getArgument(0);
            cita.setId(100L);
            return cita;
        });

        AppointmentBookingResponseDto respuesta = appointmentClientService.bookAppointment(auth, dto);

        assertEquals(100L, respuesta.getCitaId());
        assertEquals("PENDIENTE_PAGO", respuesta.getEstado());
        assertEquals(new BigDecimal("50.00"), respuesta.getMonto());
        assertTrue(respuesta.getMensaje().contains("pendiente de pago"));

        ArgumentCaptor<Cita> citaCaptor = ArgumentCaptor.forClass(Cita.class);
        verify(citaRepository).save(citaCaptor.capture());

        Cita citaGuardada = citaCaptor.getValue();

        assertEquals(cliente, citaGuardada.getUsuario());
        assertEquals(servicio, citaGuardada.getServicio());
        assertEquals(veterinario, citaGuardada.getVeterinario());
        assertEquals(mascota, citaGuardada.getPet());
        assertEquals(LocalDate.of(2026, 7, 15), citaGuardada.getFecha());
        assertEquals(LocalTime.of(10, 30), citaGuardada.getHora());
        assertEquals("PENDIENTE_PAGO", citaGuardada.getEstado());
        assertEquals(new BigDecimal("50.00"), citaGuardada.getPrecioCobrado());
    }
}