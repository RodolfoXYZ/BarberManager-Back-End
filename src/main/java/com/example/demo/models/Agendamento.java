package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHorario;

    @Column(nullable = false)
    private String barbeiroNome;

    @Column(nullable = false)
    private String tipoServico;

    @Column(nullable = false)
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
