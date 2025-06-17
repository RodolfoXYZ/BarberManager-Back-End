package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTOs.CategoriaDTO;
import com.example.demo.models.Categoria;
import com.example.demo.services.CategoriaService;

@CrossOrigin
@RestController
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

        @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCategoria(@RequestBody CategoriaDTO dto) {
        try {
            Categoria categoria = categoriaService.cadastrarCategoria(dto);
            return ResponseEntity.status(201).body(categoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar serviço: " + e.getMessage());
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarCategoria(@PathVariable Long id, @RequestBody CategoriaDTO dto) {
        try {
            Categoria categoria = categoriaService.atualizarCategoria(id, dto);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body("Erro ao atualizar serviço: " + e.getMessage());
        }
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<?> deletarCategoria(@PathVariable Long id) {
        try {
            categoriaService.deletarCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body("Erro ao remover serviço: " + e.getMessage());
        }
    }

    @GetMapping("/minhas-categorias")
    public ResponseEntity<?> listarCategorias() {
        try {
            List<Categoria> categorias = categoriaService.listarCategoriasUsuario();
            return ResponseEntity.ok(categorias);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }
}
