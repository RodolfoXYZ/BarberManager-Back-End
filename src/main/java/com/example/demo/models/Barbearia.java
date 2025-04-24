package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Barbearia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuario> equipeBarbeiros;

    @Column(nullable = false)
    private String horarioFuncionamento;

    @Column(nullable = false)
    private Double faturamento;
}
