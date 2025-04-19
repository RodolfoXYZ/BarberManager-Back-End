package com.example.demo.controllers;

import com.example.demo.services.RelatorioFinanceiroService;
import com.example.demo.DTOs.RelatorioFinanceiroDTO;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.models.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioFinanceiroController {

    @Autowired
    private RelatorioFinanceiroService relatorioFinanceiroService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/financeiro")
    public ResponseEntity<List<RelatorioFinanceiroDTO>> gerarRelatorioFinanceiro(
            @RequestParam Long usuarioId, @RequestParam String periodo) {

        List<RelatorioFinanceiroDTO> relatorio;

        if (isDonoDeBarbearia(usuarioId)) {
            relatorio = relatorioFinanceiroService.gerarRelatorioPorBarbearia(usuarioId, periodo);
        } else {
            relatorio = relatorioFinanceiroService.gerarRelatorioPorUsuario(usuarioId, periodo);
        }

        return ResponseEntity.ok(relatorio);
    }

    private boolean isDonoDeBarbearia(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> usuario.getTipo() == TipoUsuario.DONO_BARBEARIA)
                .orElse(false);
    }
}
