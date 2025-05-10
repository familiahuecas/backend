package com.familiahuecas.backend.rest.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdelantosListResponse {

    
	private Long id;
    private String nombre;
    private double cantidadAsignada;
    private String descripcion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime fecha;
    
    public AdelantosListResponse(Long id, String descripcion, float cantidadAsignada, LocalDateTime fecha,
			String nombre) {
		this.id = id;
		this.descripcion=descripcion;
		this.cantidadAsignada=cantidadAsignada;
		this.nombre=nombre;
		this.fecha=fecha;
	}

}
