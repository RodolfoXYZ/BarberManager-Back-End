package com.example.demo.controllers;

import com.example.demo.DTOs.LoginRequestDTO;
import com.example.demo.DTOs.UsuarioDTO;
import com.example.demo.models.Usuario;
import com.example.demo.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Usuario usuarioAutenticado = usuarioService.autenticarUsuario(loginRequestDTO);
            
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    usuarioAutenticado.getId(), 
                    usuarioAutenticado.getNome(), 
                    usuarioAutenticado.getEmail(),
                    null,
                    usuarioAutenticado.getEndereco(), 
                    usuarioAutenticado.getTipo()
            );
            return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
