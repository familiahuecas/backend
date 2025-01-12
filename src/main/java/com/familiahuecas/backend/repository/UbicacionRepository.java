package com.familiahuecas.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.familiahuecas.backend.entity.Ubicacion;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

	Ubicacion findByNombre(String nombre);
    // No necesitas definir findAllById, ya que es parte de JpaRepository
}
