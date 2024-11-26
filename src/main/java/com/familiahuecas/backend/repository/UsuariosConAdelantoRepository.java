package com.familiahuecas.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.entity.UsuariosConAdelanto;

public interface UsuariosConAdelantoRepository extends JpaRepository<UsuariosConAdelanto, Long> {

	Optional<UsuariosConAdelanto> findByUsuario(User usuario);
}