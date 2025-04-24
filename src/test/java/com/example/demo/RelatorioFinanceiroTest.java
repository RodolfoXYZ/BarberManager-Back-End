package com.example.demo;

import com.example.demo.DTOs.RelatorioFinanceiroDTO;
import com.example.demo.models.Agendamento;
import com.example.demo.models.Servico;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.AgendamentoRepository;
import com.example.demo.repositories.ServicoRepository;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.services.RelatorioFinanceiroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RelatorioFinanceiroTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RelatorioFinanceiroService service;

    @InjectMocks
    private RelatorioFinanceiroService relatorioFinanceiroService;


    private Usuario usuario;
    private Agendamento agendamento;
    private Servico servico;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");

        agendamento = new Agendamento();
        agendamento.setUsuario(usuario);
        agendamento.setTipoServico("Corte");
        agendamento.setBarbeiroNome("João");

        servico = new Servico();
        servico.setTitle("Corte");
        servico.setPrice(50);
    }

    @Test
    void deveGerarRelatorioDiarioPorUsuario() {
        when(agendamentoRepository.findByDataHorarioDia(any(), any(), eq(1L)))
                .thenReturn(List.of(agendamento));
        when(servicoRepository.findByUsuarioId(1L)).thenReturn(List.of(servico));

        List<RelatorioFinanceiroDTO> relatorio = service.gerarRelatorioPorUsuario(1L, "diario");

        assertEquals(1, relatorio.size());
        assertEquals(50.0, relatorio.get(0).receitaTotal());
        assertEquals("diario", relatorio.get(0).periodo());
    }

    @Test
    void deveGerarRelatorioMensalPorUsuarioComFaturamentoZerado() {
        when(agendamentoRepository.findByDataHorarioMes(anyInt(), anyInt(), eq(1L)))
                .thenReturn(List.of(agendamento));
        when(servicoRepository.findByUsuarioId(1L)).thenReturn(List.of());

        List<RelatorioFinanceiroDTO> relatorio = service.gerarRelatorioPorUsuario(1L, "mensal");

        assertEquals(1, relatorio.size());
        assertEquals(0.0, relatorio.get(0).receitaTotal());
    }

    @Test
    void deveGerarRelatorioVazioComPeriodoInvalido() {
        List<RelatorioFinanceiroDTO> relatorio = service.gerarRelatorioPorUsuario(1L, "trimestral");

        assertTrue(relatorio.isEmpty());
    }

    @Test
    void deveGerarRelatorioPorBarbeariaComDoisBarbeiros() {
        Usuario barbeiro1 = new Usuario();
        barbeiro1.setId(2L);
        barbeiro1.setNome("Carlos");

        Usuario barbeiro2 = new Usuario();
        barbeiro2.setId(3L);
        barbeiro2.setNome("Paulo");

        when(usuarioRepository.findByBarbeariaId(10L)).thenReturn(List.of(barbeiro1, barbeiro2));
        when(agendamentoRepository.findByDataHorarioDia(any(), any(), eq(2L)))
                .thenReturn(List.of(agendamento));
        when(agendamentoRepository.findByDataHorarioDia(any(), any(), eq(3L)))
                .thenReturn(List.of(agendamento));
        when(servicoRepository.findByUsuarioId(anyLong()))
                .thenReturn(List.of(servico));

        List<RelatorioFinanceiroDTO> relatorio = service.gerarRelatorioPorBarbearia(10L, "diario");

        assertEquals(2, relatorio.size());
    }
}