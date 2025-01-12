package com.familiahuecas.backend.rest.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UbicacionResponse {
	
	
	private Long id;
	private String nombre;
    private String ubicacion;
    private String foto;
	public UbicacionResponse(String nombre, String ubicacion) {
		this.nombre = nombre;
		this.ubicacion=ubicacion;
	}
	
   
}
