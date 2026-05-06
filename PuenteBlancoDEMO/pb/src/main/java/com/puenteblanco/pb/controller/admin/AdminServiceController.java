package com.puenteblanco.pb.controller.admin;

import com.puenteblanco.pb.dto.request.AdminCreateServiceRequestDto;
import com.puenteblanco.pb.dto.response.AdminServiceResponseDto;
import com.puenteblanco.pb.services.interfaces.AdminServiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
public class AdminServiceController {

    private final AdminServiceService adminServiceService;

    @GetMapping
    public ResponseEntity<List<AdminServiceResponseDto>> listarServicios() {
        return ResponseEntity.ok(adminServiceService.listarServicios());
    }

    @PostMapping
    public ResponseEntity<Void> registrarServicio(@Valid @RequestBody AdminCreateServiceRequestDto dto) {
        adminServiceService.registrarServicio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
        
    }

    @PutMapping("/{id}")
public ResponseEntity<Void> editarServicio(@PathVariable Long id,
                                           @RequestBody @Valid AdminCreateServiceRequestDto dto) {
    adminServiceService.editarServicio(id, dto);
    return ResponseEntity.noContent().build();
}

@PatchMapping("/{id}/estado")
public ResponseEntity<Void> cambiarEstado(@PathVariable Long id) {
    adminServiceService.cambiarEstadoServicio(id);
    return ResponseEntity.noContent().build();
}

@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
    adminServiceService.eliminarServicio(id);
    return ResponseEntity.noContent().build();
}
}
