package com.example.demo.repositories;

import com.example.demo.models.Categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByUsuarioId(Long usuarioId);
    Categoria findByUsuarioIdAndNomeContainingIgnoreCase(Long usuarioId, String nome);
}
