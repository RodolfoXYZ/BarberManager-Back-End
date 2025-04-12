package com.example.demo.controllers;

import com.example.demo.DTOs.BarbeariaDTO;
import com.example.demo.models.Barbearia;
import com.example.demo.services.BarbeariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barbearias")
public class BarbeariaController {

    @Autowired
    private BarbeariaService barbeariaService;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarBarbearia(@RequestBody BarbeariaDTO barbeariaDTO) {
        try {
            Barbearia barbeariaCadastrada = barbeariaService.cadastrarBarbearia(barbeariaDTO);
            return new ResponseEntity<>(barbeariaCadastrada, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarBarbearia(@PathVariable Long id, @RequestBody BarbeariaDTO barbeariaDTO) {
        try {
            Barbearia barbeariaAtualizada = barbeariaService.atualizarBarbearia(id, barbeariaDTO);
            return new ResponseEntity<>(barbeariaAtualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirBarbearia(@PathVariable Long id) {
        try {
            barbeariaService.excluirBarbearia(id);
            return new ResponseEntity<>("Barbearia excluída com sucesso", HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Barbearia>> listarBarbearias() {
        List<Barbearia> barbearias = barbeariaService.listarBarbearias();
        return new ResponseEntity<>(barbearias, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarBarbearia(@PathVariable Long id) {
        try {
            Barbearia barbearia = barbeariaService.buscarBarbearia(id);
            return new ResponseEntity<>(barbearia, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
