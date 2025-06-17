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

@Service
public class DisponibilidadeService {

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Disponibilidade cadastrarDisponibilidade(DisponibilidadeDTO disponibilidadeDTO) {
        Usuario usuario = usuarioService.getUsuarioLogado();

        if (usuario.getTipo() != TipoUsuario.BARBEIRO) {
            throw new RuntimeException("O usuário não é um barbeiro.");
        }

        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setUsuario(usuario);
        disponibilidade.setHoraInicio(disponibilidadeDTO.horaInicio());
        disponibilidade.setHoraFim(disponibilidadeDTO.horaFim());

        return disponibilidadeRepository.save(disponibilidade);
    }

    public List<Disponibilidade> buscarDisponibilidadesPorBarbeiro(Long usuarioId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getTipo() != TipoUsuario.BARBEIRO) {
            throw new RuntimeException("O usuário não é um barbeiro.");
        }

        return disponibilidadeRepository.findByUsuario(usuario);
    }
}
