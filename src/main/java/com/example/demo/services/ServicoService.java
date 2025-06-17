package com.example.demo.services;

import com.example.demo.DTOs.ServicoDTO;
import com.example.demo.models.Categoria;
import com.example.demo.models.Servico;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private UsuarioService usuarioService;;

    @Autowired
    private CategoriaService categoriaService;

    public ServicoDTO cadastrarServico(ServicoDTO dto) {

        Categoria categoria = categoriaService.buscarCategoriaPorNome(dto.categoria());

        Servico servico = new Servico();
        servico.setTitle(dto.title());
        servico.setDescricao(dto.descricao());
        servico.setPrice(dto.price());
        servico.setCategoria(categoria);

        servico = servicoRepository.save(servico);

        return toDTO(servico);
    }

    public ServicoDTO atualizarServico(Long id, ServicoDTO dto) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        Categoria categoria = categoriaService.buscarCategoriaPorNome(dto.categoria());

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!servico.getCategoria().getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este serviço");
        }

        servico.setTitle(dto.title());
        servico.setDescricao(dto.descricao());
        servico.setPrice(dto.price());
        servico.setCategoria(categoria);

        servico = servicoRepository.save(servico);

        return toDTO(servico);
    }

    public void deletarServico(Long id) {
        Usuario usuario = usuarioService.getUsuarioLogado();

        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        if (!servico.getCategoria().getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este serviço");
        }

        servicoRepository.deleteById(id);
    }

    public List<ServicoDTO> listarServicosUsuario() {
        Usuario usuario = usuarioService.getUsuarioLogado();

        return servicoRepository.findByCategoriaUsuarioId(usuario.getId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ServicoDTO toDTO(Servico s) {
        return new ServicoDTO(s.getTitle(), s.getDescricao(), s.getPrice(), s.getCategoria().getNome());
    }
}