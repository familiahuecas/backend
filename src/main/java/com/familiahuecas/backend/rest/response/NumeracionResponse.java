package com.familiahuecas.backend.rest.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NumeracionResponse {
    private Long id;
    private int entrada_m1;
    private int salida_m1;
    private int entrada_m2;
    private int salida_m2;
    private String bar;
    private LocalDateTime fecha; 
}
