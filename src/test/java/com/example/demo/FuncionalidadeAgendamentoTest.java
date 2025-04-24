package com.example.demo;

import com.example.demo.DTOs.AgendamentoDTO;
import com.example.demo.services.AgendamentoService;
import com.example.demo.models.Agendamento;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.AgendamentoRepository;
import com.example.demo.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuncionalidadeAgendamentoTest {

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Usuario getSampleUsuario() {
        Usuario user = new Usuario();
        user.setId(1L);
        user.setEmail("teste@email.com");
        user.setEndereco("Rua X, 123");
        return user;
    }

    private Agendamento getSampleAgendamento(Long id, Usuario usuario) {
        Agendamento ag = new Agendamento();
        ag.setId(id);
        ag.setDataHorario(LocalDateTime.now().plusDays(1));
        ag.setBarbeiroNome("Barbeiro Z");
        ag.setTipoServico("Corte");
        ag.setEndereco(usuario.getEndereco());
        ag.setUsuario(usuario);
        return ag;
    }

    private AgendamentoDTO getSampleDTO() {
        return new AgendamentoDTO(
                null,
                LocalDateTime.now().plusDays(1),
                "Barbeiro Z",
                "Corte",
                "Rua X, 123"
        );
    }

    @Test
    void testAgendarComSucesso() {
        when(authentication.getName()).thenReturn("teste@email.com");

        Usuario usuario = getSampleUsuario();
        Agendamento agendamento = getSampleAgendamento(1L, usuario);

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(agendamentoRepository.save(any(Agendamento.class))).thenReturn(agendamento);

        AgendamentoDTO dto = getSampleDTO();
        AgendamentoDTO result = agendamentoService.agendar(dto);

        assertNotNull(result);
        assertEquals("Barbeiro Z", result.barbeiroNome());
        verify(agendamentoRepository).save(any(Agendamento.class));
    }

    @Test
    void testCancelarAgendamentoComSucesso() {
        when(authentication.getName()).thenReturn("teste@email.com");

        Usuario usuario = getSampleUsuario();
        Agendamento agendamento = getSampleAgendamento(1L, usuario);

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));
        doNothing().when(agendamentoRepository).deleteById(1L);

        assertDoesNotThrow(() -> agendamentoService.cancelarAgendamento(1L));
        verify(agendamentoRepository).deleteById(1L);
    }

    @Test
    void testListarAgendamentosUsuarioAutenticado() {
        when(authentication.getName()).thenReturn("teste@email.com");

        Usuario usuario = getSampleUsuario();
        List<Agendamento> agendamentos = List.of(
                getSampleAgendamento(1L, usuario),
                getSampleAgendamento(2L, usuario)
        );

        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(agendamentoRepository.findByUsuarioId(usuario.getId())).thenReturn(agendamentos);

        List<AgendamentoDTO> resultado = agendamentoService.listarAgendamentosUsuarioAutenticado();

        assertEquals(2, resultado.size());
        verify(agendamentoRepository).findByUsuarioId(usuario.getId());
    }
}
