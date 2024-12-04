package com.example.demo.DTOs;

import java.time.LocalDateTime;

public record AgendamentoDTO(Long id, LocalDateTime dataHorario, String barbeiroNome, String tipoServico, String endereco, long usuarioId) {}
