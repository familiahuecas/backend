package com.familiahuecas.backend.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "6e756d65726163696f6e") // Nombre ofuscado para la tabla 'numeracion'
@Getter
@Setter
@NoArgsConstructor
public class Numeracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entrada_m1", nullable = false)
    private int entradaM1;

    @Column(name = "salida_m1", nullable = false)
    private int salidaM1;

    @Column(name = "entrada_m2", nullable = false)
    private int entradaM2;

    @Column(name = "salida_m2", nullable = false)
    private int salidaM2;

    @Column(name = "bar", length = 50, nullable = true)
    private String bar = "lucy";

    @Column(name = "fecha", nullable = true)
    private LocalDateTime fecha;
}

