package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTOs.AgendamentoDTO;
import com.example.demo.services.AgendamentoService;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping("/agendar")
    public ResponseEntity<?> agendar(@RequestBody AgendamentoDTO agendamentoDTO) {
        try {
            AgendamentoDTO agendamentoCriado = agendamentoService.agendar(agendamentoDTO);
            return new ResponseEntity<>(agendamentoCriado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Erro ao agendar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao agendar: " + e.getMessage());
        }
    }

    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarAgendamento(@PathVariable Long id) {
        try {
            agendamentoService.cancelarAgendamento(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            System.err.println("Erro ao cancelar agendamento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao cancelar agendamento: " + e.getMessage());
        }
    }

    @GetMapping("/meus-agendamentos")
    public ResponseEntity<List<AgendamentoDTO>> listarAgendamentos() {
        try {
            List<AgendamentoDTO> agendamentos = agendamentoService.listarAgendamentosUsuarioAutenticado();
            return new ResponseEntity<>(agendamentos, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Usuário autenticado não encontrado")) {
                System.err.println("Erro de autenticação: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            System.err.println("Erro ao listar agendamentos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(List.of());
        }
    }
}
