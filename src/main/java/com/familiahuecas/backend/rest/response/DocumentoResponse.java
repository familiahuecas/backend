package com.familiahuecas.backend.rest.response;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentoResponse {
    private Long id;
    private String nombre;
    private String path;
    private LocalDateTime fecha;
}

