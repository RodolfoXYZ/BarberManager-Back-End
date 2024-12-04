package com.example.demo.controllers;

import com.example.demo.DTOs.AgendamentoDTO;
import com.example.demo.services.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping("/agendar")
    public ResponseEntity<AgendamentoDTO> agendar(@RequestBody AgendamentoDTO agendamentoDTO) {
        try {
            AgendamentoDTO agendamentoCriado = agendamentoService.agendar(agendamentoDTO);
            return new ResponseEntity<>(agendamentoCriado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/cancelar/{id}")
    public ResponseEntity<?> cancelarAgendamento(@PathVariable Long id) {
        try {
            agendamentoService.cancelarAgendamento(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AgendamentoDTO>> listarAgendamentos(@PathVariable Long usuarioId) {
        List<AgendamentoDTO> agendamentos = agendamentoService.listarAgendamentosPorUsuario(usuarioId);
        return new ResponseEntity<>(agendamentos, HttpStatus.OK);
    }
}
