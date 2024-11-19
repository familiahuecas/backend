package com.familiahuecas.backend.rest.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecaudacionesResponse {
    private Long id;
    private long maquina1;
    private long maquina2;
    private long recaudaciontotal;
    private long recaudacionparcial;
    private String bar;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime fecha; 
}
