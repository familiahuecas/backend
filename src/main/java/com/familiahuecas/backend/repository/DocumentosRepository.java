package com.familiahuecas.backend.repository;

import com.familiahuecas.backend.entity.Documento;
import com.familiahuecas.backend.entity.Numeracion;
import com.familiahuecas.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentosRepository extends JpaRepository<Documento, Long> {

	List<Documento> findByParentIsNull();

	List<Documento> findByParent(Documento documento);

	List<Documento> findByParentId(Long id);

	
  
	
}
