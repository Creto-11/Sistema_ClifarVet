package com.puenteblanco.pb.repository;

import com.puenteblanco.pb.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // Busca un rol por su nombre (ej: "CLIENT", "ADMIN", etc.)
    Optional<Role> findByNombre(String nombre);

    Optional<Role> findById(Long id);

}
