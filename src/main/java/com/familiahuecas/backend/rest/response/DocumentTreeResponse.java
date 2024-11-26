package com.familiahuecas.backend.rest.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentTreeResponse {
    private Long id;
    private String nombre;
    private boolean esCarpeta;
    private String path;
    private List<DocumentTreeResponse> children;
}

