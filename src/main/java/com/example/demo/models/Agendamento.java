package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHorario;

    @ManyToOne
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Usuario barbeiro;

    @Column(nullable = false)
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;
}
