package com.example.demo;

import com.example.demo.DTOs.DisponibilidadeDTO;
import com.example.demo.models.Disponibilidade;
import com.example.demo.models.TipoUsuario;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.DisponibilidadeRepository;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.services.DisponibilidadeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GerenciarDisponibilidadeTest {

    @Mock
    private DisponibilidadeRepository disponibilidadeRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DisponibilidadeService disponibilidadeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarDisponibilidadeComSucesso() {
        Usuario barbeiro = new Usuario();
        barbeiro.setId(1L);
        barbeiro.setTipo(TipoUsuario.BARBEIRO);

        DisponibilidadeDTO dto = new DisponibilidadeDTO(1L, LocalTime.of(9, 0), LocalTime.of(12, 0));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(barbeiro));
        when(disponibilidadeRepository.save(any(Disponibilidade.class))).thenAnswer(i -> i.getArgument(0));

        Disponibilidade resultado = disponibilidadeService.cadastrarDisponibilidade(dto);

        assertNotNull(resultado);
        assertEquals(barbeiro, resultado.getUsuario());
        assertEquals(LocalTime.of(9, 0), resultado.getHoraInicio());
        assertEquals(LocalTime.of(12, 0), resultado.getHoraFim());
    }

    @Test
    public void testCadastrarDisponibilidadeUsuarioNaoEncontrado() {
        DisponibilidadeDTO dto = new DisponibilidadeDTO(1L, LocalTime.of(9, 0), LocalTime.of(12, 0));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                disponibilidadeService.cadastrarDisponibilidade(dto));

        assertEquals("Usuário (Barbeiro) não encontrado", exception.getMessage());
    }

    @Test
    public void testCadastrarDisponibilidadeUsuarioNaoEhBarbeiro() {
        Usuario cliente = new Usuario();
        cliente.setId(1L);
        cliente.setTipo(TipoUsuario.CLIENTE);

        DisponibilidadeDTO dto = new DisponibilidadeDTO(1L, LocalTime.of(9, 0), LocalTime.of(12, 0));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Exception exception = assertThrows(RuntimeException.class, () ->
                disponibilidadeService.cadastrarDisponibilidade(dto));

        assertEquals("O usuário não é um barbeiro.", exception.getMessage());
    }

    @Test
    public void testBuscarDisponibilidadesPorBarbeiroComSucesso() {
        Usuario barbeiro = new Usuario();
        barbeiro.setId(1L);
        barbeiro.setTipo(TipoUsuario.BARBEIRO);

        Disponibilidade disp1 = new Disponibilidade();
        Disponibilidade disp2 = new Disponibilidade();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(barbeiro));
        when(disponibilidadeRepository.findByUsuario(barbeiro)).thenReturn(Arrays.asList(disp1, disp2));

        List<Disponibilidade> resultado = disponibilidadeService.buscarDisponibilidadesPorBarbeiro(1L);

        assertEquals(2, resultado.size());
    }

    @Test
    public void testBuscarDisponibilidadesUsuarioNaoEhBarbeiro() {
        Usuario cliente = new Usuario();
        cliente.setId(1L);
        cliente.setTipo(TipoUsuario.CLIENTE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Exception exception = assertThrows(RuntimeException.class, () ->
                disponibilidadeService.buscarDisponibilidadesPorBarbeiro(1L));

        assertEquals("O usuário não é um barbeiro.", exception.getMessage());
    }
}
