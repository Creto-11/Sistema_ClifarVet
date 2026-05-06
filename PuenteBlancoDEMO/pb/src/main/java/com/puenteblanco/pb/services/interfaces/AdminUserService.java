package com.puenteblanco.pb.services.interfaces;

import com.puenteblanco.pb.dto.request.AdminCreateUserRequestDto;
import com.puenteblanco.pb.dto.request.AdminUpdateUserRequestDto;
import com.puenteblanco.pb.dto.response.AdminUserResponseDto;

import java.util.List;

public interface AdminUserService {
    List<AdminUserResponseDto> listarUsuariosPorRol(String rol);
    
void toggleEstadoUsuario(Long id, String motivo); 

    void crearUsuario(AdminCreateUserRequestDto dto);

    void desactivarUsuarioConMotivo(Long id, String motivo);

    void actualizarUsuario(Long id, AdminUpdateUserRequestDto dto);
}
