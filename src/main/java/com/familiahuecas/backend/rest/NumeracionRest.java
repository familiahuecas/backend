package com.familiahuecas.backend.rest;

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

import com.familiahuecas.backend.rest.request.RecaudacionRequest;
import com.familiahuecas.backend.rest.response.NumeracionResponse;
import com.familiahuecas.backend.rest.response.RecaudacionResponse;
import com.familiahuecas.backend.rest.response.RecaudacionesResponse;
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
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "fecha") String orderBy,
            @RequestParam(defaultValue = "true") boolean isOrderDesc) {

        // Crear la dirección de ordenamiento en base al parámetro isOrderDesc
        Sort sort = Sort.by(isOrderDesc ? Sort.Direction.DESC : Sort.Direction.ASC, orderBy);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // Llamar al servicio con el pageable ordenado
        Page<NumeracionResponse> numeraciones = numeracionService.getAllPaginated(sortedPageable, orderBy, isOrderDesc);
        return ResponseEntity.ok(numeraciones);
    }

    @DeleteMapping("/deleteNumeracion/{id}")
    public ResponseEntity<String> deleteNumeracion(@PathVariable Long id) {
        try {
            numeracionService.deleteNumeracionById(id);
            return ResponseEntity.ok("Numeración eliminada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar la numeración: " + e.getMessage());
        }
    }

    @PostMapping("/calculateRec")
    public ResponseEntity<RecaudacionResponse> calculateSum(
    		@RequestBody RecaudacionRequest recaudacionRequest) {

        // Llamar al servicio para realizar el cálculo
        RecaudacionResponse result = numeracionService.calculateRec(recaudacionRequest.getEntradaM1(), recaudacionRequest.getSalidaM1(), recaudacionRequest.getEntradaM2(), recaudacionRequest.getSalidaM2());
      
        return ResponseEntity.ok(result);
    }
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarRecaudacion(@RequestBody RecaudacionRequest recaudacionRequest) {
        try {
            numeracionService.guardarRecaudacion(recaudacionRequest);
            return ResponseEntity.ok("Recaudación guardada con éxito");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al guardar la recaudación: " + e.getMessage());
        }
    }
    @GetMapping("/listrecaudacion")
    public ResponseEntity<Page<RecaudacionesResponse>> getAllRecaudaciones(
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "fecha") String orderBy,
            @RequestParam(defaultValue = "true") boolean isOrderDesc) {

        // Crear la dirección de ordenamiento en base al parámetro isOrderDesc
        Sort sort = Sort.by(isOrderDesc ? Sort.Direction.DESC : Sort.Direction.ASC, orderBy);

        // Configurar el pageable con el orden adecuado
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // Llamar al servicio con el pageable ordenado
        Page<RecaudacionesResponse> recaudaciones = numeracionService.getAllRecaudcionesPaginated(sortedPageable, orderBy, isOrderDesc);

        // Devolver la respuesta
        return ResponseEntity.ok(recaudaciones);
    }

}
