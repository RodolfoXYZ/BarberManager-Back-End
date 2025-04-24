package com.example.demo.DTOs;

import com.example.demo.models.TipoUsuario;
import com.example.demo.models.TipoServico;

public record UsuarioDTO(Long id, String nome, String email, String senha, String endereco, TipoUsuario tipo, TipoServico tipoServico) {}
