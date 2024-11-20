package com.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Barbeiro extends Usuario {

    private String especialidades;
    private float precoPorServico;
    private float avaliacao;
    private String horariosDisponiveis;
    private String areaDeAtendimento;

    @ManyToOne
    @JoinColumn(name = "barbearia_id")
    private Barbearia barbearia;
}