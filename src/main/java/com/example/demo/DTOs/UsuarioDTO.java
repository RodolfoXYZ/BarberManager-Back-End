package com.example.demo.DTOs;

import com.example.demo.models.TipoUsuario;

public record UsuarioDTO(String nome, String email, String senha, String endereco, TipoUsuario tipo) {}
