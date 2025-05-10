package com.familiahuecas.backend.repository;

import com.familiahuecas.backend.entity.DetalleAdelanto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleAdelantoRepository extends JpaRepository<DetalleAdelanto, Long> {

    List<DetalleAdelanto> findByUsuarioId(Long usuarioId);
    Page<DetalleAdelanto> findByUsuarioNameContainingIgnoreCase(String nombre, Pageable pageable);


}
