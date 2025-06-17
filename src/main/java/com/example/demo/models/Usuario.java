package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Barbearia> barbearias = new ArrayList<>();

    @ManyToOne
    private Barbearia barbearia;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("usuario")
    private List<Categoria> categoria = new ArrayList<>();

    @Override
    public String toString() {
        String nomesCategorias = (categoria != null) ? categoria.stream()
            .map(Categoria::getNome)
            .reduce((a, b) -> a + ", " + b)
            .orElse("") : "";
        return "Usuario [id=" + id + ", nome=" + nome + ", email=" + email + ", senha=" + senha + ", endereco="
                + endereco + ", tipo=" + tipo + ", barbearias=" + barbearias + ", barbearia=" + barbearia
                + ", categorias=[" + nomesCategorias + "]" + "]";
    };
}
