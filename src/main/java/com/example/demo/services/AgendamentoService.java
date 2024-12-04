package com.example.demo.services;

import com.example.demo.DTOs.AgendamentoDTO;
import com.example.demo.models.Agendamento;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.AgendamentoRepository;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AgendamentoDTO agendar(AgendamentoDTO agendamentoDTO) {
        if (agendamentoDTO.dataHorario().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("A data e hora do agendamento não pode ser no passado");
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(agendamentoDTO.usuarioId());
        if (usuarioOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHorario(agendamentoDTO.dataHorario());
        agendamento.setBarbeiroNome(agendamentoDTO.barbeiroNome());
        agendamento.setTipoServico(agendamentoDTO.tipoServico());
        agendamento.setEndereco(usuario.getEndereco());
        agendamento.setUsuario(usuario);

        agendamento = agendamentoRepository.save(agendamento);

        return new AgendamentoDTO(
                agendamento.getId(),
                agendamento.getDataHorario(),
                agendamento.getBarbeiroNome(),
                agendamento.getTipoServico(),
                agendamento.getEndereco(),
                agendamento.getUsuario().getId()
        );
    }

    public void cancelarAgendamento(Long id) {
        Optional<Agendamento> agendamentoOptional = agendamentoRepository.findById(id);
        if (agendamentoOptional.isEmpty()) {
            throw new RuntimeException("Agendamento não encontrado");
        }

        agendamentoRepository.deleteById(id);
    }

    public List<AgendamentoDTO> listarAgendamentosPorUsuario(Long usuarioId) {
        List<Agendamento> agendamentos = agendamentoRepository.findByUsuarioId(usuarioId);
        return agendamentos.stream().map(agendamento -> new AgendamentoDTO(
                agendamento.getId(),
                agendamento.getDataHorario(),
                agendamento.getBarbeiroNome(),
                agendamento.getTipoServico(),
                agendamento.getEndereco(),
                agendamento.getUsuario().getId()
        )).collect(Collectors.toList());
    }
}
