package com.familiahuecas.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.familiahuecas.backend.entity.Numeracion;
import com.familiahuecas.backend.repository.NumeracionRepository;
import com.familiahuecas.backend.rest.response.NumeracionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class NumeracionService {
    private final NumeracionRepository numeracionRepository;

    public NumeracionService(NumeracionRepository numeracionRepository) {
        this.numeracionRepository = numeracionRepository;
    }

    public Page<NumeracionResponse> getAllPaginated(Pageable pageable, String orderBy, boolean isOrderDesc) {
        // Obtener las entidades paginadas desde el repositorio
        Page<Numeracion> numeraciones = numeracionRepository.findAll(pageable);

        // Convertir las entidades en NumeracionResponse
        return numeraciones.map(numeracion -> new NumeracionResponse(
                numeracion.getId(),
                numeracion.getEntradaM1(),
                numeracion.getSalidaM1(),
                numeracion.getEntradaM2(),
                numeracion.getSalidaM2(),
                numeracion.getBar(),
                numeracion.getFecha()
        ));
    }
}

