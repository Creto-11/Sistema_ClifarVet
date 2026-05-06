package com.puenteblanco.pb.repository;

import com.puenteblanco.pb.dto.response.VetInternSimpleResponseDto;
import com.puenteblanco.pb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    boolean existsByNumeroIdentidad(String numeroIdentidad);

    @Query("""
            SELECT new com.puenteblanco.pb.dto.response.VetInternSimpleResponseDto(
            u.id, u.nombres, u.apellidoPaterno
            )
            FROM User u
            WHERE u.role.id = 4
            AND u.estado = true
            """)
    List<VetInternSimpleResponseDto> findAllInterns();

    List<User> findByRoleNombreIgnoreCase(String roleNombre);

    @Query("SELECT u FROM User u WHERE (:roleId IS NULL OR u.role.id = :roleId)")
    List<User> findAllByOptionalRoleId(Long roleId);

    @Query("SELECT u FROM User u WHERE u.correo = :correo AND u.role.nombre = 'CLIENT'")
    Optional<User> findClientByCorreo(String correo);
}
