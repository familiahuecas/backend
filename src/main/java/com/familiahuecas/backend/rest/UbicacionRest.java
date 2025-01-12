package com.familiahuecas.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.familiahuecas.backend.rest.request.UbicacionRequest;
import com.familiahuecas.backend.rest.response.NumeracionResponse;
import com.familiahuecas.backend.rest.response.UbicacionResponse;
import com.familiahuecas.backend.service.UbicacionService;


@RestController
@RequestMapping("/ubicacion")
public class UbicacionRest {

    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping("/byname/{nombre}")
    public ResponseEntity<UbicacionResponse> getUbicacionByName(@PathVariable String nombre) {
        UbicacionResponse response = ubicacionService.getUbicacionByName(nombre);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/create")
    public ResponseEntity<UbicacionResponse> saveOrUpdate(@RequestBody UbicacionRequest request) {
        UbicacionResponse response = ubicacionService.saveOrUpdate(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        ubicacionService.delete(id);
        return ResponseEntity.ok("Ubicación eliminada con éxito");
    }

    @GetMapping("/list")
    public ResponseEntity<Page<UbicacionResponse>> getAllUbicaciones(
            @PageableDefault(sort = "nombre", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "nombre") String orderBy,
            @RequestParam(defaultValue = "true") boolean isOrderDesc) {

        // Crear la dirección de ordenamiento en base al parámetro isOrderDesc
        Sort sort = Sort.by(isOrderDesc ? Sort.Direction.DESC : Sort.Direction.ASC, orderBy);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // Llamar al servicio con el pageable ordenado
        Page<UbicacionResponse> ubicaciones = ubicacionService.getAllPaginated(sortedPageable, orderBy, isOrderDesc);
        return ResponseEntity.ok(ubicaciones);
    }
}


