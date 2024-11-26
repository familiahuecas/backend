package com.familiahuecas.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.familiahuecas.backend.entity.ConceptoGastoAdelanto;
import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.entity.UsuariosConAdelanto;
import com.familiahuecas.backend.repository.UserRepository;
import com.familiahuecas.backend.repository.UsuariosConAdelantoRepository;
import com.familiahuecas.backend.rest.request.UsuarioConAdelantoRequest;
import com.familiahuecas.backend.rest.response.UsuarioConAdelantoResponse;

@Service
public class UsuariosConAdelantoService {

    @Autowired
    private UsuariosConAdelantoRepository usuariosConAdelantoRepository;
   
    @Autowired
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
    }
    public UsuarioConAdelantoResponse saveOrUpdate(UsuarioConAdelantoRequest request) {
        // Buscar usuario relacionado
        User usuario = userRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Buscar si ya existe la relación UsuariosConAdelanto para este usuario
        UsuariosConAdelanto usuariosConAdelanto = usuariosConAdelantoRepository.findByUsuario(usuario)
                .orElse(null);

        if (usuariosConAdelanto != null) {
            // Si ya existe, sumar la cantidad asignada
            float nuevaCantidad = usuariosConAdelanto.getCantidadAsignada() + request.getCantidadSolicitada();
            usuariosConAdelanto.setCantidadAsignada(nuevaCantidad);
        } else {
            // Si no existe, crear un nuevo registro
            usuariosConAdelanto = new UsuariosConAdelanto();
            usuariosConAdelanto.setUsuario(usuario); // Relacionar el usuario
            usuariosConAdelanto.setCantidadAsignada(request.getCantidadSolicitada()); // Asignar la cantidad
        }

        // Guardar la relación
        usuariosConAdelanto = usuariosConAdelantoRepository.save(usuariosConAdelanto);

        // Retornar la respuesta
        return new UsuarioConAdelantoResponse(
                usuariosConAdelanto.getUsuario().getId(),
                usuariosConAdelanto.getCantidadAsignada()
        );
    }


    public void delete(Long id) {
        UsuariosConAdelanto usuariosConAdelanto = usuariosConAdelantoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adelanto no encontrado"));
        usuariosConAdelantoRepository.delete(usuariosConAdelanto);
    }
}

