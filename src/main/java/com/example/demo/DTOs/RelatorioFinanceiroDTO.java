package com.example.demo.DTOs;

public record RelatorioFinanceiroDTO(
    Double receitaTotal,
    int totalAgendamentos,
    String periodo,
    Long barbeiroId,
    String nomeBarbeiro
) {}
