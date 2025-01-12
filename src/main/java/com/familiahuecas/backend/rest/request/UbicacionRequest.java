package com.familiahuecas.backend.rest.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UbicacionRequest {
	private Long id;
	private String nombre;

	private String ubicacion;
	
	private String foto;

}
