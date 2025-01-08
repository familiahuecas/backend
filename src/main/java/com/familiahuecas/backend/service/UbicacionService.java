package com.familiahuecas.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familiahuecas.backend.entity.ConceptoGastoAdelanto;
import com.familiahuecas.backend.entity.Ubicacion;
import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.entity.UsuariosConAdelanto;
import com.familiahuecas.backend.repository.UbicacionRepository;
import com.familiahuecas.backend.repository.UserRepository;
import com.familiahuecas.backend.repository.UsuariosConAdelantoRepository;
import com.familiahuecas.backend.rest.request.UbicacionRequest;
import com.familiahuecas.backend.rest.request.UsuarioConAdelantoRequest;
import com.familiahuecas.backend.rest.response.UbicacionResponse;
import com.familiahuecas.backend.rest.response.UsuarioConAdelantoResponse;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;
   
 /*   @Autowired
    private UserRepository userRepository;
    
    public List<UsuarioConAdelantoResponse> getUsuariosConDetalles() {
        return usuariosConAdelantoRepository.findAll().stream().map(usuario -> {
            double totalGastado = usuario.getConceptosGasto().stream()
                    .mapToDouble(ConceptoGastoAdelanto::getTotal)
                    .sum();
            int numeroDeApuntes = usuario.getConceptosGasto().size();
            float cantidadRestante = usuario.getCantidadAsignada() - (float) totalGastado;

            return new UsuarioConAdelantoResponse(
                usuario.getUsuario().getId(),
                usuario.getUsuario().getName(),
                usuario.getCantidadAsignada(),
                cantidadRestante,
                numeroDeApuntes
            );
        }).collect(Collectors.toList());
    }*/
    public UbicacionResponse saveOrUpdate(UbicacionRequest request) {
    	
    	Ubicacion ubicacion = new Ubicacion();
    	ubicacion.setNombre(request.getNombre());
    	ubicacion.setUbicacion(request.getUbicacion());
               ubicacion= ubicacionRepository.save(ubicacion);
              UbicacionResponse ur = new UbicacionResponse(ubicacion.getNombre(),ubicacion.getUbicacion());
               return ur;

       
    }


    public void delete(Long id) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ubicación no encontrada"));
        ubicacionRepository.delete(ubicacion);
    }
}

