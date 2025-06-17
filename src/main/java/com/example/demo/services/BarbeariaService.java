package com.example.demo.services;

import com.example.demo.DTOs.BarbeariaDTO;
import com.example.demo.models.Barbearia;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.BarbeariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarbeariaService {

    @Autowired
    private BarbeariaRepository barbeariaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Barbearia cadastrarBarbearia(BarbeariaDTO barbeariaDTO) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        Barbearia barbearia = new Barbearia();
        barbearia.setNome(barbeariaDTO.nome());
        barbearia.setEndereco(barbeariaDTO.endereco());
        barbearia.setCnpj(barbeariaDTO.cnpj());
        barbearia.setHorarioFuncionamento(barbeariaDTO.horarioFuncionamento());
        barbearia.setProprietario(usuario);

        return barbeariaRepository.save(barbearia);
    }

    public Barbearia atualizarBarbearia(Long id, BarbeariaDTO barbeariaDTO) {
        Usuario usuario = usuarioService.getUsuarioLogado();
        Barbearia barbearia = barbeariaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbearia não encontrada"));

        if (!barbearia.getProprietario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta barbearia");
        }
        
        barbearia.setNome(barbeariaDTO.nome());
        barbearia.setEndereco(barbeariaDTO.endereco());
        barbearia.setCnpj(barbeariaDTO.cnpj());
        barbearia.setHorarioFuncionamento(barbeariaDTO.horarioFuncionamento());

        return barbeariaRepository.save(barbearia);
    }

    public void excluirBarbearia(Long id) {
        Barbearia barbearia = barbeariaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbearia não encontrada"));

        barbeariaRepository.delete(barbearia);
    }

    public Barbearia buscarBarbearia(Long id) {
        return barbeariaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbearia não encontrada"));
    }

    public List<Barbearia> listarBarbearias() {
        return barbeariaRepository.findAll();
    }
}
