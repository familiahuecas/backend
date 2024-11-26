package com.familiahuecas.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.familiahuecas.backend.entity.ConceptoGastoAdelanto;

public interface ConceptoGastoAdelantoRepository extends JpaRepository<ConceptoGastoAdelanto, Long> {

	@Query("""
	        SELECT c
	        FROM ConceptoGastoAdelanto c
	        JOIN c.usuarioconadelanto uca
	        JOIN uca.usuario u
	        WHERE u.name = :usuario
	    """)
	    Page<ConceptoGastoAdelanto> findByUsuario(@Param("usuario") String usuario, Pageable pageable);


}

