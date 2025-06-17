package com.example.demo.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int price;
    
    @ManyToOne
    @JsonIgnoreProperties("servico")
    private Categoria categoria;
}