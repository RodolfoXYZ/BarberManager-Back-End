package com.example.demo.services;

import com.example.demo.DTOs.DisponibilidadeDTO;
import com.example.demo.models.Disponibilidade;
import com.example.demo.models.TipoUsuario;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.DisponibilidadeRepository;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisponibilidadeService {

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Disponibilidade cadastrarDisponibilidade(DisponibilidadeDTO disponibilidadeDTO) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(disponibilidadeDTO.usuarioId());
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário (Barbeiro) não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario.getTipo() != TipoUsuario.BARBEIRO) {
            throw new RuntimeException("O usuário não é um barbeiro.");
        }

        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setUsuario(usuario); // Relaciona a disponibilidade com o usuário
        disponibilidade.setHoraInicio(disponibilidadeDTO.horaInicio());
        disponibilidade.setHoraFim(disponibilidadeDTO.horaFim());

        return disponibilidadeRepository.save(disponibilidade);
    }

    public List<Disponibilidade> buscarDisponibilidadesPorBarbeiro(Long usuarioId) {
        // Buscando as disponibilidades de um barbeiro específico
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getTipo() != TipoUsuario.BARBEIRO) {
            throw new RuntimeException("O usuário não é um barbeiro.");
        }

        return disponibilidadeRepository.findByUsuario(usuario);
    }
}
