package com.familiahuecas.backend.rest.request;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DetalleAdelantoRequest {
	private Long idUsuario;
    private float cantidadSolicitada;
    private String descripcion;
    private LocalDateTime fecha;
}
