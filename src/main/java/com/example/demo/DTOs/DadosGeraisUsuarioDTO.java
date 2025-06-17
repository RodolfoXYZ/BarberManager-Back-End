package com.example.demo.DTOs;

import java.util.List;

import com.example.demo.models.Barbearia;
import com.example.demo.models.Categoria;
import com.example.demo.models.TipoUsuario;

public record DadosGeraisUsuarioDTO (String nome, String email, String senha, String endereco, TipoUsuario tipo, List<Barbearia> barbearias, Barbearia barbearia, List<Categoria> categorias) {
}
