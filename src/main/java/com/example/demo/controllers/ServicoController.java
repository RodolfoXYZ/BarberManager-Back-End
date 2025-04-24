package com.example.demo.controllers;

import com.example.demo.DTOs.ServicoDTO;
import com.example.demo.services.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarServico(@RequestBody ServicoDTO dto) {
        try {
            ServicoDTO criado = servicoService.cadastrarServico(dto);
            return ResponseEntity.status(201).body(criado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar serviço: " + e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarServico(@PathVariable Long id, @RequestBody ServicoDTO dto) {
        try {
            ServicoDTO atualizado = servicoService.atualizarServico(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body("Erro ao atualizar serviço: " + e.getMessage());
        }
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletarServico(@PathVariable Long id) {
        try {
            servicoService.deletarServico(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body("Erro ao remover serviço: " + e.getMessage());
        }
    }

    @GetMapping("/meus-servicos")
    public ResponseEntity<List<ServicoDTO>> listarServicos() {
        try {
            List<ServicoDTO> servicos = servicoService.listarServicosUsuario();
            return ResponseEntity.ok(servicos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }
}