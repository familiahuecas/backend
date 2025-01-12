package com.familiahuecas.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.familiahuecas.backend.entity.ConceptoGastoAdelanto;
import com.familiahuecas.backend.entity.Numeracion;
import com.familiahuecas.backend.entity.Ubicacion;
import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.entity.UsuariosConAdelanto;
import com.familiahuecas.backend.repository.UbicacionRepository;
import com.familiahuecas.backend.repository.UserRepository;
import com.familiahuecas.backend.repository.UsuariosConAdelantoRepository;
import com.familiahuecas.backend.rest.request.UbicacionRequest;
import com.familiahuecas.backend.rest.request.UsuarioConAdelantoRequest;
import com.familiahuecas.backend.rest.response.NumeracionResponse;
import com.familiahuecas.backend.rest.response.UbicacionResponse;
import com.familiahuecas.backend.rest.response.UsuarioConAdelantoResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;
   
    public UbicacionResponse saveOrUpdate(UbicacionRequest request) {
        Ubicacion ubicacion = new Ubicacion();
        if (request.getId() != null) {
            ubicacion = ubicacionRepository.findById(request.getId())
                    .orElse(new Ubicacion());
        }

        ubicacion.setNombre(request.getNombre());
        ubicacion.setUbicacion(request.getUbicacion());
        ubicacion.setFoto(request.getFoto()); // Guardar la foto

        ubicacion = ubicacionRepository.save(ubicacion);

        return new UbicacionResponse(ubicacion.getId(), ubicacion.getNombre(), ubicacion.getUbicacion(), ubicacion.getFoto());
    }


    public void delete(Long id) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ubicación no encontrada"));
        ubicacionRepository.delete(ubicacion);
    }


    public UbicacionResponse getUbicacionByName(String nombre) {
        Ubicacion ubicacion = ubicacionRepository.findByNombre(nombre); // Método correcto

        if (ubicacion == null) {
            throw new EntityNotFoundException("Ubicación con nombre " + nombre + " no encontrada.");
        }

        return new UbicacionResponse(ubicacion.getId(),ubicacion.getNombre(), ubicacion.getUbicacion(), ubicacion.getFoto());
    }


	public Page<UbicacionResponse> getAllPaginated(Pageable sortedPageable, String orderBy, boolean isOrderDesc) {
		
		        // Obtener las entidades paginadas desde el repositorio
		        Page<Ubicacion> ubicaciones = ubicacionRepository.findAll(sortedPageable);

		        // Convertir las entidades en NumeracionResponse
		        return ubicaciones.map(ubicacion -> new UbicacionResponse(
		        		ubicacion.getId(),ubicacion.getNombre(),ubicacion.getUbicacion(),ubicacion.getFoto()
		                ));
		    
	}
}

