package com.familiahuecas.backend.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.familiahuecas.backend.rest.request.ConceptoGastoAdelantoRequest;
import com.familiahuecas.backend.rest.response.ConceptoGastoAdelantoResponse;
import com.familiahuecas.backend.securiry.JwtService;
import com.familiahuecas.backend.service.ConceptoGastoAdelantoService;
import com.familiahuecas.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conceptos")
public class ConceptoGastoAdelantoRest {

    private final ConceptoGastoAdelantoService conceptoGastoAdelantoService;

   /* public ConceptoGastoAdelantoRest(ConceptoGastoAdelantoService conceptoGastoAdelantoService) {
        this.conceptoGastoAdelantoService = conceptoGastoAdelantoService;
    }*/

    @GetMapping("/list")
    public ResponseEntity<Page<ConceptoGastoAdelantoResponse>> getAllConceptos(
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "fecha") String orderBy,
            @RequestParam(defaultValue = "true") boolean isOrderDesc,
            @RequestParam(required = false) String usuario // Filtro opcional por usuario
    ) {
        // Crear la dirección de ordenamiento en base al parámetro isOrderDesc
        Sort sort = Sort.by(isOrderDesc ? Sort.Direction.DESC : Sort.Direction.ASC, orderBy);

        // Configurar el pageable con el orden adecuado
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // Llamar al servicio con el pageable ordenado y filtro de usuario
        Page<ConceptoGastoAdelantoResponse> conceptos = 
                conceptoGastoAdelantoService.getAllConceptosPaginated(sortedPageable, orderBy, isOrderDesc, usuario);

        // Devolver la respuesta
        return ResponseEntity.ok(conceptos);
    }
    @PostMapping("/create")
    public ResponseEntity<ConceptoGastoAdelantoResponse> createConceptoGasto(
            @RequestBody ConceptoGastoAdelantoRequest request) {
        // Llama al servicio para crear el concepto
        ConceptoGastoAdelantoResponse createdConcepto = conceptoGastoAdelantoService.saveOrUpdate(request);

        // Devuelve la respuesta
        return ResponseEntity.ok(createdConcepto);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
        	conceptoGastoAdelantoService.deleteApunteById(id);
            return ResponseEntity.ok("Apunte eliminado con éxito");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el apunte: " + e.getMessage());
        }
    }

}
