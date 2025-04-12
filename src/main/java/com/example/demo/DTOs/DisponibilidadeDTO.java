package com.example.demo.DTOs;

import java.time.LocalTime;

public record DisponibilidadeDTO(Long usuarioId, LocalTime horaInicio, LocalTime horaFim) {}
