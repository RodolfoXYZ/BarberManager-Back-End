package com.example.demo.services;

import com.example.demo.DTOs.RelatorioFinanceiroDTO;
import com.example.demo.models.Agendamento;
import com.example.demo.models.Servico;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.AgendamentoRepository;
import com.example.demo.repositories.ServicoRepository;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class RelatorioFinanceiroService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para gerar relatório financeiro por período
    public List<RelatorioFinanceiroDTO> gerarRelatorioPorUsuario(Long usuarioId, String periodo) {
        List<Agendamento> agendamentos;

        // Dependendo do período solicitado, filtramos os agendamentos
        switch (periodo.toLowerCase()) {
            case "diario":
                agendamentos = agendamentoRepository.findByDataHorarioDia(
                        calcularInicioDia(), calcularFimDia(), usuarioId);
                break;
            case "semanal":
                agendamentos = agendamentoRepository.findByDataHorarioSemana(getSemanaAtual(), LocalDate.now().getYear(), usuarioId);
                break;
            case "mensal":
                agendamentos = agendamentoRepository.findByDataHorarioMes(LocalDate.now().getMonthValue(), LocalDate.now().getYear(), usuarioId);
                break;
            default:
                agendamentos = new ArrayList<>();
        }

        // Gerar lista de relatórios financeiros com base nos agendamentos
        List<RelatorioFinanceiroDTO> relatorios = new ArrayList<>();

        // Iterar sobre os agendamentos para calcular o faturamento
        for (Agendamento agendamento : agendamentos) {
            Double totalFaturamento = calcularFaturamento(agendamento);
            relatorios.add(new RelatorioFinanceiroDTO(
                    totalFaturamento,
                    1, // Considerando um único agendamento por vez
                    periodo,
                    usuarioId,
                    agendamento.getBarbeiroNome()
            ));
        }

        return relatorios;
    }

    // Método para gerar relatório financeiro por barbearia (dono de barbearia)
    public List<RelatorioFinanceiroDTO> gerarRelatorioPorBarbearia(Long barbeariaId, String periodo) {
        // Buscar todos os barbeiros da barbearia
        List<Usuario> barbeiros = usuarioRepository.findByBarbeariaId(barbeariaId);
        
        List<RelatorioFinanceiroDTO> relatorios = new ArrayList<>();
        
        // Para cada barbeiro, gerar o relatório
        for (Usuario barbeiro : barbeiros) {
            List<Agendamento> agendamentos;

            // Dependendo do período solicitado, filtramos os agendamentos
            switch (periodo.toLowerCase()) {
                case "diario":
                    agendamentos = agendamentoRepository.findByDataHorarioDia(
                            calcularInicioDia(), calcularFimDia(), barbeiro.getId());
                    break;
                case "semanal":
                    agendamentos = agendamentoRepository.findByDataHorarioSemana(getSemanaAtual(), LocalDate.now().getYear(), barbeiro.getId());
                    break;
                case "mensal":
                    agendamentos = agendamentoRepository.findByDataHorarioMes(LocalDate.now().getMonthValue(), LocalDate.now().getYear(), barbeiro.getId());
                    break;
                default:
                    agendamentos = new ArrayList<>();
            }

            // Gerar relatório do barbeiro
            for (Agendamento agendamento : agendamentos) {
                Double totalFaturamento = calcularFaturamento(agendamento);
                relatorios.add(new RelatorioFinanceiroDTO(
                        totalFaturamento,
                        1,  // Considerando um único agendamento por vez
                        periodo,
                        barbeiro.getId(),
                        barbeiro.getNome()
                ));
            }
        }

        return relatorios;
    }

    // Método para calcular o faturamento de um agendamento com base no serviço
    private Double calcularFaturamento(Agendamento agendamento) {
        // Aqui estamos supondo que o preço do serviço será atribuído com base no nome do serviço
        List<Servico> servicos = servicoRepository.findByUsuarioId(agendamento.getUsuario().getId());

        for (Servico servico : servicos) {
            if (servico.getTitle().equals(agendamento.getTipoServico())) {
                return (double) servico.getPrice();
            }
        }
        return 0.0;  // Retorna 0.0 se o serviço não for encontrado
    }

    // Método auxiliar para pegar a semana atual (número da semana)
    private int getSemanaAtual() {
        return LocalDate.now()
                .get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    // Método para calcular o início do dia (00:00:00)
    private LocalDateTime calcularInicioDia() {
        return LocalDate.now().atStartOfDay();
    }

    // Método para calcular o fim do dia (23:59:59)
    private LocalDateTime calcularFimDia() {
        return LocalDate.now().atTime(23, 59, 59);
    }
}
