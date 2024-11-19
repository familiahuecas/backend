package com.familiahuecas.backend.repository;

import com.familiahuecas.backend.entity.Numeracion;
import com.familiahuecas.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NumeracionRepository extends JpaRepository<Numeracion, Long> {
  
	 Optional<Numeracion> findTopByBarOrderByFechaDesc(String bar);
}
