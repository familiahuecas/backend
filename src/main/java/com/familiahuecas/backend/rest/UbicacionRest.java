package com.familiahuecas.backend.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.familiahuecas.backend.rest.request.UbicacionRequest;
import com.familiahuecas.backend.rest.response.UbicacionResponse;
import com.familiahuecas.backend.service.UbicacionService;


@RestController
@RequestMapping("/ubicacion")
public class UbicacionRest {

    @Autowired
    private UbicacionService ubicacionService;

  /*  @GetMapping("/usuariosconapuntes")
    public List<UsuarioConAdelantoResponse> getUsuariosConDetalles() {
        return usuariosConAdelantoService.getUsuariosConDetalles();
    }*/

    @PostMapping("/create")
    public ResponseEntity<UbicacionResponse> saveOrUpdate(@RequestBody UbicacionRequest request) {
        UbicacionResponse response = ubicacionService.saveOrUpdate(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
    	ubicacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


