package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Disponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Relaciona a Disponibilidade ao Usuario, que pode ser um Barbeiro

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFim;

    public List<LocalTime> getHorariosDisponiveis() {
        List<LocalTime> horarios = new ArrayList<>();
        LocalTime horario = horaInicio;

        while (horario.isBefore(horaFim)) {
            horarios.add(horario);
            horario = horario.plusMinutes(30); // Adiciona 30 minutos
        }
        return horarios;
    }
}
