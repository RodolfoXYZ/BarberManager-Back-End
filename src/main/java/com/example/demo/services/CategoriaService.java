package com.example.demo.services;

import com.example.demo.DTOs.CategoriaDTO;
import com.example.demo.models.Categoria;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Categoria cadastrarCategoria(CategoriaDTO categoriaDTO) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        Categoria categoria = new Categoria();
        categoria.setUsuario(usuario);
        categoria.setNome(categoriaDTO.nome());
        categoria.setDescricao(categoriaDTO.descricao());

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategoriasUsuario() {
        Usuario usuario = usuarioService.getUsuarioLogado();
        return categoriaRepository.findByUsuarioId(usuario.getId());
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public Categoria atualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoria = buscarPorId(id);
        Usuario usuario = usuarioService.getUsuarioLogado();

        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta categoria");
        }
        
        categoria.setNome(categoriaDTO.nome());
        categoria.setDescricao(categoriaDTO.descricao());

        return categoriaRepository.save(categoria);
    }

    public void deletarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);
        Usuario usuario = usuarioService.getUsuarioLogado();

        if (!categoria.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para remover esta categoria");
        }

        categoriaRepository.delete(categoria);
    }

    public Categoria buscarCategoriaPorNome(String nome) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        return categoriaRepository.findByUsuarioIdAndNomeContainingIgnoreCase(usuario.getId(), nome);
    }
}