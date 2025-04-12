package com.example.demo.repositories;

import com.example.demo.models.Disponibilidade;
import com.example.demo.models.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {
    List<Disponibilidade> findByUsuario(Usuario usuario);
}
