package com.example.demo.services;

import com.example.demo.DTOs.BarbeariaDTO;
import com.example.demo.models.Barbearia;
import com.example.demo.repositories.BarbeariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarbeariaService {

    @Autowired
    private BarbeariaRepository barbeariaRepository;

    public Barbearia cadastrarBarbearia(BarbeariaDTO barbeariaDTO) {
        Barbearia barbearia = new Barbearia();
        barbearia.setNome(barbeariaDTO.nome());
        barbearia.setEndereco(barbeariaDTO.endereco());
        barbearia.setCnpj(barbeariaDTO.cnpj());
        barbearia.setHorarioFuncionamento(barbeariaDTO.horarioFuncionamento());
        barbearia.setFaturamento(barbeariaDTO.faturamento());

        return barbeariaRepository.save(barbearia);
    }

    public Barbearia atualizarBarbearia(Long id, BarbeariaDTO barbeariaDTO) {
        Barbearia barbearia = barbeariaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbearia não encontrada"));

        barbearia.setNome(barbeariaDTO.nome());
        barbearia.setEndereco(barbeariaDTO.endereco());
        barbearia.setCnpj(barbeariaDTO.cnpj());
        barbearia.setHorarioFuncionamento(barbeariaDTO.horarioFuncionamento());
        barbearia.setFaturamento(barbeariaDTO.faturamento());

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
