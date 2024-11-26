package com.familiahuecas.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familiahuecas.backend.entity.ConceptoGastoAdelanto;
import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.entity.UsuariosConAdelanto;
import com.familiahuecas.backend.repository.ConceptoGastoAdelantoRepository;
import com.familiahuecas.backend.repository.UserRepository;
import com.familiahuecas.backend.repository.UsuariosConAdelantoRepository;
import com.familiahuecas.backend.rest.request.ConceptoGastoAdelantoRequest;
import com.familiahuecas.backend.rest.response.ConceptoGastoAdelantoResponse;

@Service
@Transactional
public class ConceptoGastoAdelantoService {

    @Autowired
    private ConceptoGastoAdelantoRepository conceptoGastoAdelantoRepository;
    @Autowired
    private UsuariosConAdelantoRepository usuariosConAdelantoRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<ConceptoGastoAdelantoResponse> getAllConceptosPaginated(Pageable pageable, String orderBy, boolean isOrderDesc, String usuario) {
        Page<ConceptoGastoAdelanto> conceptosPage;

        if (usuario != null && !usuario.isEmpty()) {
            // Filtrar por usuario si se proporciona el nombre
            conceptosPage = conceptoGastoAdelantoRepository.findByUsuario(usuario, pageable);
        } else {
            // Obtener todos los conceptos si no se especifica un usuario
            conceptosPage = conceptoGastoAdelantoRepository.findAll(pageable);
        }

        // Mapear cada entidad a su correspondiente respuesta
        return conceptosPage.map(this::toResponse);
    }

    private ConceptoGastoAdelantoResponse toResponse(ConceptoGastoAdelanto concepto) {
        return new ConceptoGastoAdelantoResponse(
            concepto.getId(),
            concepto.getDescripcion(),
            concepto.getTotal(),
            concepto.getFecha(),
            concepto.getUsuarioconadelanto() != null && concepto.getUsuarioconadelanto().getUsuario() != null
                ? concepto.getUsuarioconadelanto().getUsuario().getName()
                : "Usuario desconocido" // Manejo de casos donde usuario es null
        );
    }
    
    public ConceptoGastoAdelantoResponse saveOrUpdate(ConceptoGastoAdelantoRequest request) {
        // Buscar el usuario relacionado
        User usuario = userRepository.findByName(request.getUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Buscar la relación UsuariosConAdelanto
        UsuariosConAdelanto usuariosConAdelanto = usuariosConAdelantoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("No existe relación UsuariosConAdelanto para el usuario: " + request.getUsuario()));

        // Crear el nuevo concepto
        ConceptoGastoAdelanto concepto = new ConceptoGastoAdelanto();
        concepto.setId(request.getId());
        concepto.setDescripcion(request.getDescripcion());
        concepto.setTotal(request.getTotal());
        concepto.setFecha(java.time.LocalDateTime.now());
        concepto.setUsuarioconadelanto(usuariosConAdelanto);

        // Guardar el concepto
        concepto = conceptoGastoAdelantoRepository.save(concepto);

        // Retornar la respuesta
        return new ConceptoGastoAdelantoResponse(
                concepto.getId(),
                concepto.getDescripcion(),
                concepto.getTotal(),
                concepto.getFecha(),
                concepto.getUsuarioconadelanto().getUsuario().getName()
        );
    }

	public void deleteApunteById(Long id) {
		ConceptoGastoAdelanto apunte = conceptoGastoAdelantoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Apunte no encontrado con ID: " + id));

		conceptoGastoAdelantoRepository.delete(apunte);
		
	}


}
