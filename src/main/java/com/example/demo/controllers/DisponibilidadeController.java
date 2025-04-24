package com.example.demo.controllers;

import com.example.demo.DTOs.DisponibilidadeDTO;
import com.example.demo.models.Disponibilidade;
import com.example.demo.services.DisponibilidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadeController {

    @Autowired
    private DisponibilidadeService disponibilidadeService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarDisponibilidade(@RequestBody DisponibilidadeDTO disponibilidadeDTO) {
        try {
            var disponibilidade = disponibilidadeService.cadastrarDisponibilidade(disponibilidadeDTO);
            return ResponseEntity.status(201).body(disponibilidade);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<String>> listarHorariosDisponiveis(@PathVariable Long barbeiroId) {
        List<Disponibilidade> disponibilidades = disponibilidadeService.buscarDisponibilidadesPorBarbeiro(barbeiroId);
        
        List<String> horariosDisponiveis = new ArrayList<>();
        for (Disponibilidade disponibilidade : disponibilidades) {
            List<LocalTime> horarios = disponibilidade.getHorariosDisponiveis();
            for (LocalTime horario : horarios) {
                horariosDisponiveis.add(horario.toString());
            }
        }
        
        return ResponseEntity.ok(horariosDisponiveis);
    }
}
