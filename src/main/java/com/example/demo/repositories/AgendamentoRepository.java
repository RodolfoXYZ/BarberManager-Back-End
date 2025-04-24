package com.example.demo.repositories;

import com.example.demo.models.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Busca por ID de usuário
    List<Agendamento> findByUsuarioId(Long usuarioId);

    // Consulta para buscar agendamentos no dia específico de um usuário
    @Query("SELECT a FROM Agendamento a WHERE a.dataHorario >= :inicioDia AND a.dataHorario <= :fimDia AND a.usuario.id = :usuarioId")
    List<Agendamento> findByDataHorarioDia(@Param("inicioDia") LocalDateTime inicioDia, 
                                            @Param("fimDia") LocalDateTime fimDia,
                                            @Param("usuarioId") Long usuarioId);

    // Consulta para agendamentos na mesma semana de um usuário
    @Query("SELECT a FROM Agendamento a WHERE WEEK(a.dataHorario) = :semana AND YEAR(a.dataHorario) = :ano AND a.usuario.id = :usuarioId")
    List<Agendamento> findByDataHorarioSemana(@Param("semana") int semana, 
                                               @Param("ano") int ano, 
                                               @Param("usuarioId") Long usuarioId);

    // Consulta para agendamentos no mesmo mês de um usuário
    @Query("SELECT a FROM Agendamento a WHERE MONTH(a.dataHorario) = :mes AND YEAR(a.dataHorario) = :ano AND a.usuario.id = :usuarioId")
    List<Agendamento> findByDataHorarioMes(@Param("mes") int mes, 
                                            @Param("ano") int ano, 
                                            @Param("usuarioId") Long usuarioId);
}
