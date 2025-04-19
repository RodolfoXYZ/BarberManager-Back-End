package com.example.demo.services;

import com.example.demo.DTOs.ServicoDTO;
import com.example.demo.models.Servico;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.ServicoRepository;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        throw new RuntimeException("Erro: Usuário não autenticado");
    }

    public ServicoDTO cadastrarServico(ServicoDTO dto) {
        Usuario usuario = getUsuarioLogado();

        Servico servico = new Servico();
        servico.setUrl(dto.url());
        servico.setTitle(dto.title());
        servico.setDescricao(dto.descricao());
        servico.setPrice(dto.price());
        servico.setUsuario(usuario);

        servico = servicoRepository.save(servico);

        return toDTO(servico);
    }

    public ServicoDTO atualizarServico(Long id, ServicoDTO dto) {
        Usuario usuario = getUsuarioLogado();

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!servico.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este serviço");
        }

        servico.setUrl(dto.url());
        servico.setTitle(dto.title());
        servico.setDescricao(dto.descricao());
        servico.setPrice(dto.price());

        servico = servicoRepository.save(servico);

        return toDTO(servico);
    }

    public void deletarServico(Long id) {
        Usuario usuario = getUsuarioLogado();

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!servico.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este serviço");
        }

        servicoRepository.deleteById(id);
    }

    public List<ServicoDTO> listarServicosUsuario() {
        Usuario usuario = getUsuarioLogado();

        return servicoRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private Usuario getUsuarioLogado() {
        String email = getUsuarioAutenticado();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
    }

    private ServicoDTO toDTO(Servico s) {
        return new ServicoDTO(s.getId(), s.getUrl(), s.getTitle(), s.getDescricao(), s.getPrice());
    }
}