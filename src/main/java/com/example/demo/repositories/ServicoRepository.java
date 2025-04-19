package com.example.demo.repositories;

import com.example.demo.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByUsuarioId(Long usuarioId);
}