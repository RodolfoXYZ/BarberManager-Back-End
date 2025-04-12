package com.example.demo.DTOs;

import java.util.List;

public record BarbeariaDTO(Long id, String nome, String endereco, String cnpj, List<Long> equipeBarbeirosIds, String horarioFuncionamento, Double faturamento) {}
