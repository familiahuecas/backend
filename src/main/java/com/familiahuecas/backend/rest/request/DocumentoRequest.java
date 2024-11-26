package com.familiahuecas.backend.rest.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DocumentoRequest {
    
	private Long id;
    private String nombre;
    private String path;
    private boolean esCarpeta;
   
}

