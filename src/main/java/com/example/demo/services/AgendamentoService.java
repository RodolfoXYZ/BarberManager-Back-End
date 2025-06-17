package com.example.demo.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTOs.AgendamentoDTO;
import com.example.demo.models.Agendamento;
import com.example.demo.models.Servico;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.AgendamentoRepository;
import com.example.demo.repositories.ServicoRepository;
import com.example.demo.repositories.UsuarioRepository;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicoRepository servicoRepository;

    public AgendamentoDTO agendar(AgendamentoDTO agendamentoDTO) {
        String emailUsuario = usuarioService.getUsuarioAutenticado();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Erro: Usuário autenticado não encontrado para o email: " + emailUsuario));
        Usuario barbeiro = usuarioRepository.findByNomeContainingIgnoreCase(agendamentoDTO.barbeiroNome());
        Servico servico = servicoRepository.findFirstByTitleContainingIgnoreCaseAndCategoriaUsuarioId(agendamentoDTO.tipoServico(), barbeiro.getId());

        Agendamento agendamento = new Agendamento();
        agendamento.setDataHorario(agendamentoDTO.dataHorario());
        agendamento.setBarbeiro(barbeiro);
        agendamento.setUsuario(usuario);
        agendamento.setServico(servico);
        agendamento.setEndereco(usuario.getEndereco());

        try {
            agendamento = agendamentoRepository.save(agendamento);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o agendamento: " + e.getMessage());
        }

        return new AgendamentoDTO(
                agendamento.getDataHorario(),
                agendamento.getBarbeiro().getNome(),
                agendamento.getUsuario().getNome(),
                agendamento.getServico().getTitle(),
                agendamento.getEndereco()
        );
    }

    public void cancelarAgendamento(Long id) {
        String emailUsuario = usuarioService.getUsuarioAutenticado();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Erro: Usuário autenticado não encontrado para o email: " + emailUsuario));

        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Erro: Agendamento não encontrado para o ID: " + id));

        if (!agendamento.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para cancelar este agendamento");
        }

        LocalDateTime horarioAtual = LocalDateTime.now();
        Duration diferenca = Duration.between(agendamento.getDataHorario(), horarioAtual);
        if (diferenca.toHours() < 2) {
            throw new RuntimeException("Só é possível cancelar agendamentos com pelo menos 2 horas de antecedência");
        }

        try {
            agendamentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cancelar o agendamento: " + e.getMessage());
        }
    }

    public List<AgendamentoDTO> listarAgendamentosUsuarioAutenticado() {
        String emailUsuario = usuarioService.getUsuarioAutenticado();
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Erro: Usuário autenticado não encontrado para o email: " + emailUsuario));

        List<Agendamento> agendamentos = agendamentoRepository.findByUsuarioId(usuario.getId());

        return agendamentos.stream()
                .map(agendamento -> new AgendamentoDTO(
                        agendamento.getDataHorario(),
                        agendamento.getBarbeiro().getNome(),
                        agendamento.getUsuario().getNome(),
                        agendamento.getServico().getTitle(),
                        agendamento.getEndereco()
                ))
                .collect(Collectors.toList());
    }
}
