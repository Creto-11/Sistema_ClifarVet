package com.puenteblanco.pb.controller.admin;

import com.puenteblanco.pb.dto.request.AdminCreateUserRequestDto;
import com.puenteblanco.pb.dto.request.AdminUpdateUserRequestDto;
import com.puenteblanco.pb.dto.request.DesactivacionRequest;
import com.puenteblanco.pb.dto.response.AdminUserResponseDto;
import com.puenteblanco.pb.services.interfaces.AdminUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<AdminUserResponseDto>> listarTodos(@RequestParam(required = false) String rol) {
        return ResponseEntity.ok(adminUserService.listarUsuariosPorRol(rol));
    }

    @PatchMapping("/{id}/estado")
public ResponseEntity<Void> cambiarEstado(
        @PathVariable Long id,
        @RequestBody(required = false) Map<String, String> payload
) {
    String motivo = payload != null ? payload.get("motivo") : null;
    adminUserService.toggleEstadoUsuario(id, motivo);
    return ResponseEntity.noContent().build();
}

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarConMotivo(
            @PathVariable Long id,
            @RequestBody DesactivacionRequest request) {
        adminUserService.desactivarUsuarioConMotivo(id, request.getMotivo());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> registrarUsuario(@Valid @RequestBody AdminCreateUserRequestDto dto) {
        adminUserService.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Usuario creado correctamente"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserRequestDto dto) {
        adminUserService.actualizarUsuario(id, dto);
        return ResponseEntity.noContent().build();
    }
}