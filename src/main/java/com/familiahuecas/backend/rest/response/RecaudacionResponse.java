package com.familiahuecas.backend.rest.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecaudacionResponse {
	private int entradaM1;
    private int salidaM1;
    private int entradaM2;
    private int salidaM2;
    
    private int lastEntradaM1;
    private int lastSalidaM1;
    private int lastEntradaM2;
    private int lastSalidaM2;
    
    private int restaEntradaM1;
    private int restaSalidaM1;
    private int restaEntradaM2;
    private int restaSalidaM2;
	
	
	private float total;
	private float totalcadauno;
	private float totalm1;
	private float totalm2;
	
}
