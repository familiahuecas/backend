package com.familiahuecas.backend.rest.response;

import java.time.LocalDateTime;

import com.familiahuecas.backend.entity.UsuariosConAdelanto;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConceptoGastoAdelantoResponse {
	
	    private Long id;

	   
	    private String descripcion;

	   
	    private float total;

	    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	    private LocalDateTime fecha;
	    
	   
	    private String usuario;

}
