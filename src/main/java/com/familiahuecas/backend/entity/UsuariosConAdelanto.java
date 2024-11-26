package com.familiahuecas.backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "7573756172696f73636f6e6164656c616e746f") // Nombre ofuscado
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuariosConAdelanto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad_asignada")
    private float cantidadAsignada;

    // Relación con ConceptoGastoAdelanto
    @OneToMany(mappedBy = "usuarioconadelanto",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConceptoGastoAdelanto> conceptosGasto;

    // Relación con User
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private User usuario;
}


