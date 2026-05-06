package com.puenteblanco.pb.controller.client;

import com.puenteblanco.pb.dto.EmailDTO;
import com.puenteblanco.pb.services.interfaces.RecoveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordRecoveryController {

    private final RecoveryService recoveryService;

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody EmailDTO emailDTO) {
        try {
            recoveryService.enviarCodigoRecuperacion(emailDTO.getCorreo());
            return ResponseEntity.ok("Código enviado al correo.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("El correo ingresado no está registrado.");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(
            @RequestParam String correo,
            @RequestParam String codigo,
            @RequestParam String nuevaContrasena) {
        recoveryService.cambiarContrasena(correo, codigo, nuevaContrasena);
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
