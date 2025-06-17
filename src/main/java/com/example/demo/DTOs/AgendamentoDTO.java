package com.example.demo.DTOs;

import java.time.LocalDateTime;

public record AgendamentoDTO(LocalDateTime dataHorario, String barbeiroNome, String clienteNome, String tipoServico, String endereco) {}
