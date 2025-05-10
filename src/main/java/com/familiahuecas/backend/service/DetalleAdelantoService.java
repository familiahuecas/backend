package com.familiahuecas.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.familiahuecas.backend.entity.DetalleAdelanto;
import com.familiahuecas.backend.entity.User;
import com.familiahuecas.backend.repository.DetalleAdelantoRepository;
import com.familiahuecas.backend.repository.UserRepository;
import com.familiahuecas.backend.rest.request.DetalleAdelantoRequest;
import com.familiahuecas.backend.rest.response.DetalleAdelantoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetalleAdelantoService {

    private final DetalleAdelantoRepository detalleRepo;
    private final UserRepository userRepo;

    public Page<DetalleAdelantoResponse> getAllDetallesPaginated(Pageable pageable, String usuarioFiltro) {
        Page<DetalleAdelanto> page;

        if (usuarioFiltro != null && !usuarioFiltro.isBlank()) {
            page = detalleRepo.findByUsuarioNameContainingIgnoreCase(usuarioFiltro, pageable);
        } else {
            page = detalleRepo.findAll(pageable);
        }

        return page.map(this::toResponse);
    }


    public DetalleAdelantoResponse createDetalle(DetalleAdelantoRequest request) {
        User user = userRepo.findById(request.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        DetalleAdelanto detalle = new DetalleAdelanto();
        detalle.setUsuario(user);
        detalle.setCantidad(request.getCantidadSolicitada());
        detalle.setDescripcion(request.getDescripcion());
        detalle.setFecha(request.getFecha() != null ? request.getFecha() : LocalDateTime.now());

        DetalleAdelanto saved = detalleRepo.save(detalle);
        return toResponse(saved);
    }


    private DetalleAdelantoResponse toResponse(DetalleAdelanto entity) {
        return new DetalleAdelantoResponse(
            entity.getId(),
            entity.getCantidad(),
            entity.getDescripcion(),
            entity.getFecha(),
            entity.getUsuario().getName()
        );
    }

    public void eliminarDetalle(Long id) {
        detalleRepo.deleteById(id);
    }

}
