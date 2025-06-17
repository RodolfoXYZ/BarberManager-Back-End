package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "barbearias")
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

    private String url;

    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    @JsonIgnoreProperties({"barbearias", "proprietario"})
    private Usuario proprietario;

    @OneToMany(mappedBy = "barbearia", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("barbearia")
    private List<Usuario> equipeBarbeiros;

    @Column(nullable = false)
    private String horarioFuncionamento;
}