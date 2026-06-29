package com.puenteblanco.pb.services.impl;

import com.puenteblanco.pb.entity.RecoveryToken;
import com.puenteblanco.pb.entity.User;
import com.puenteblanco.pb.repository.RecoveryTokenRepository;
import com.puenteblanco.pb.repository.UserRepository;
import com.puenteblanco.pb.services.interfaces.RecoveryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull; // Importante para la seguridad de tipos
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RecoveryServiceImpl implements RecoveryService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final RecoveryTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void enviarCodigoRecuperacion(@NonNull String correo) {
        // Se quitó "User user =" porque no se utilizaba (limpia el aviso amarillo)
        userRepository.findClientByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("El correo no pertenece a ningún cliente registrado"));
        // Invalidar todos los códigos anteriores
        tokenRepository.findByCorreoAndUsadoFalse(correo)
                .forEach(token -> {
                    token.setUsado(true);
                    tokenRepository.save(token);
                });

        String codigo = generarCodigo();
        RecoveryToken token = RecoveryToken.builder()
                .correo(correo)
                .codigo(codigo)
                .fechaCreacion(LocalDateTime.now())
                .usado(false)
                .build();

        tokenRepository.save(token);
        enviarCorreo(correo, codigo);
    }

    @Transactional
    @Override
    public void cambiarContrasena(@NonNull String correo, @NonNull String codigo, @NonNull String nuevaContrasena) {
        RecoveryToken token = tokenRepository.findByCorreoAndCodigoAndUsadoFalse(correo, codigo)
                .orElseThrow(() -> new RuntimeException("Código inválido o expirado"));

        if (token.getFechaCreacion().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El código ha expirado, solicita uno nuevo.");
        }

        User user = userRepository.findClientByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("El correo no pertenece a ningún cliente registrado"));

        user.setContrasena(passwordEncoder.encode(nuevaContrasena));
        userRepository.save(user);

        token.setUsado(true);
        tokenRepository.save(token);
    }

    private String generarCodigo() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void enviarCorreo(String correo, String codigo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(correo);
            helper.setSubject("Recuperación de contraseña - Clínica Puente Blanco");
            helper.setText("<p>Tu código de recuperación es: <b>" + codigo + "</b></p>", true);
            mailSender.send(message);
        } catch (MessagingException e) {
            // Log simplificado para evitar ruidos en consola
            System.err.println("❌ Error al enviar correo a " + correo + ": " + e.getMessage());
        }
    }
}