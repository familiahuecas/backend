package com.familiahuecas.backend.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.familiahuecas.backend.rest.response.NumeracionResponse;
import com.familiahuecas.backend.service.NumeracionService;

@RestController
@RequestMapping("/numeracion")
public class NumeracionRest {
    private final NumeracionService numeracionService;

    public NumeracionRest(NumeracionService numeracionService) {
        this.numeracionService = numeracionService;
    }

   
    @GetMapping("/list")
    public ResponseEntity<Page<NumeracionResponse>> getAllNumeraciones(
            Pageable pageable,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "false") boolean isOrderDesc) {
        
        // Llamar al servicio con los filtros y paginación
        Page<NumeracionResponse> numeraciones = numeracionService.getAllPaginated(pageable, orderBy, isOrderDesc);

        return ResponseEntity.ok(numeraciones);
    }
}
