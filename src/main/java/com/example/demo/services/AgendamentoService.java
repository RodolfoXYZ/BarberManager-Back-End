package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.DTOs.AgendamentoDTO;
import com.example.demo.models.Agendamento;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.AgendamentoRepository;
import com.example.demo.repositories.UsuarioRepository;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        throw new RuntimeException("Erro: Usuário não autenticado");
    }

    public AgendamentoDTO agendar(AgendamentoDTO agendamentoDTO) {
        String emailUsuario = getUsuarioAutenticado();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Erro: Usuário autenticado não encontrado para o email: " + emailUsuario));

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHorario(agendamentoDTO.dataHorario());
        agendamento.setBarbeiroNome(agendamentoDTO.barbeiroNome());
        agendamento.setTipoServico(agendamentoDTO.tipoServico());
        agendamento.setEndereco(usuario.getEndereco());
        agendamento.setUsuario(usuario);

        try {
            agendamento = agendamentoRepository.save(agendamento);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o agendamento: " + e.getMessage());
        }

        return new AgendamentoDTO(
                agendamento.getId(),
                agendamento.getDataHorario(),
                agendamento.getBarbeiroNome(),
                agendamento.getTipoServico(),
                agendamento.getEndereco()
        );
    }

    public void cancelarAgendamento(Long id) {
        String emailUsuario = getUsuarioAutenticado();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Erro: Usuário autenticado não encontrado para o email: " + emailUsuario));

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Agendamento não encontrado para o ID: " + id));

        if (!agendamento.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para cancelar este agendamento");
        }

        try {
            agendamentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cancelar o agendamento: " + e.getMessage());
        }
    }

    public List<AgendamentoDTO> listarAgendamentosUsuarioAutenticado() {
        String emailUsuario = getUsuarioAutenticado();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Erro: Usuário autenticado não encontrado para o email: " + emailUsuario));

        List<Agendamento> agendamentos = agendamentoRepository.findByUsuarioId(usuario.getId());

        return agendamentos.stream()
                .map(agendamento -> new AgendamentoDTO(
                        agendamento.getId(),
                        agendamento.getDataHorario(),
                        agendamento.getBarbeiroNome(),
                        agendamento.getTipoServico(),
                        agendamento.getEndereco()
                ))
                .collect(Collectors.toList());
    }
}
