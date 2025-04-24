package com.example.demo.controllers;

import com.example.demo.DTOs.UsuarioDTO;
import com.example.demo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioDTO usuarioCadastrado = new UsuarioDTO(
                    usuarioService.cadastrarUsuario(usuarioDTO).getId(),
                    usuarioDTO.nome(),
                    usuarioDTO.email(),
                    null,
                    usuarioDTO.endereco(),
                    usuarioDTO.tipo(),
                    usuarioDTO.tipoServico()
            );

            return new ResponseEntity<>(usuarioCadastrado, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return new ResponseEntity<>("Erro: " + e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
