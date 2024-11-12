package com.familiahuecas.backend.repository;

import com.familiahuecas.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // No necesitas definir findAllById, ya que es parte de JpaRepository
}
