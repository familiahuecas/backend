package com.familiahuecas.backend.rest.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioConAdelantoResponse {
	public UsuarioConAdelantoResponse(Long id, float cantidadAsignada2) {
		// TODO Auto-generated constructor stub
	}
	private Long idUsuario;
	private String nombre;
    private float cantidadAsignada;
    private float cantidadRestante;
    private int numeroDeApuntes;
}
