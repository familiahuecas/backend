package com.familiahuecas.backend.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioConAdelantoRequest {
	private Long id;
	private Long idUsuario;
	
    private float cantidadSolicitada;
    
}
