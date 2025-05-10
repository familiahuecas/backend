package com.familiahuecas.backend.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.familiahuecas.backend.rest.request.DetalleAdelantoRequest;
import com.familiahuecas.backend.rest.response.DetalleAdelantoResponse;
import com.familiahuecas.backend.service.DetalleAdelantoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/detalleadelanto")
@RequiredArgsConstructor
public class DetalleAdelantoRest {

    private final DetalleAdelantoService service;

    @GetMapping("/list")
    public ResponseEntity<Page<DetalleAdelantoResponse>> getAllDetalles(
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "fecha") String orderBy,
            @RequestParam(defaultValue = "true") boolean isOrderDesc,
            @RequestParam(required = false) String usuario // nombre
    ) {
        Sort sort = Sort.by(isOrderDesc ? Sort.Direction.DESC : Sort.Direction.ASC, orderBy);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<DetalleAdelantoResponse> detalles = service.getAllDetallesPaginated(sortedPageable, usuario);
        return ResponseEntity.ok(detalles);
    }


    @PostMapping
    public DetalleAdelantoResponse crear(@RequestBody DetalleAdelantoRequest request) {
        return service.createDetalle(request);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminarDetalle(id);
    }
}
