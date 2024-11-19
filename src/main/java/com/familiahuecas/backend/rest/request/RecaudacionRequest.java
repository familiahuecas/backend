package com.familiahuecas.backend.rest.request;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class RecaudacionRequest {
    private String bar;
    private int entradaM1;
    private int salidaM1;
    private int entradaM2;
    private int salidaM2;
    
}
