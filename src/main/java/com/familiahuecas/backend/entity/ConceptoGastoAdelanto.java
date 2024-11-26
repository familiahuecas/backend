package com.familiahuecas.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "636f6e636570746f676173746f6164656c616e746f") // Nombre ofuscado
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoGastoAdelanto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "total")
    private float total;

    @Column(name = "fecha")
    private LocalDateTime fecha;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuariosconadelanto", referencedColumnName = "usuario_id", nullable = false)
    private UsuariosConAdelanto usuarioconadelanto;

}

