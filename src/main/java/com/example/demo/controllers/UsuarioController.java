package com.example.demo.controllers;

import com.example.demo.DTOs.UsuarioDTO;
import com.example.demo.models.Usuario;
import com.example.demo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
                usuarioService.cadastrarUsuario(usuarioDTO);
            UsuarioDTO usuarioCadastrado = new UsuarioDTO(

                    usuarioDTO.nome(),
                    usuarioDTO.email(),
                    null,
                    usuarioDTO.endereco(),
                    usuarioDTO.tipo()
            );

            return new ResponseEntity<>(usuarioCadastrado, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/barbeiros")
    public ResponseEntity<?> listarBarbeiros() {
        try {
            List<Usuario> barbeiros = usuarioService.listarBarbeiros();
            return ResponseEntity.ok(barbeiros);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Erro ao buscar barbeiros: " + e.getMessage());
        }
    }
}
