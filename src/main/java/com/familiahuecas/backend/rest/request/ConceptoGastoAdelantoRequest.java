package com.familiahuecas.backend.rest.request;

import java.time.LocalDateTime;

import com.familiahuecas.backend.entity.UsuariosConAdelanto;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConceptoGastoAdelantoRequest {
	
	    private Long id;
	    private String descripcion;
	    private float total;
	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	    private LocalDateTime fecha;
	    private String usuario;

}
