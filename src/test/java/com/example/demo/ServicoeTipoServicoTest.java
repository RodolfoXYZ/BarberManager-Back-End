package com.example.demo;

import com.example.demo.DTOs.ServicoDTO;
import com.example.demo.models.Servico;
import com.example.demo.models.TipoUsuario;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.ServicoRepository;
import com.example.demo.repositories.UsuarioRepository;
import com.example.demo.services.ServicoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ServicoeTipoServicoTest {

    @InjectMocks
    private ServicoService servicoService;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setEmail("usuario@teste.com");
        
        Authentication auth = new UsernamePasswordAuthenticationToken(
            usuarioMock.getEmail(), 
            null,
            null
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

      @Test
    void cadastrarServicoTest() {
        
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@teste.com");
        usuario.setTipo(TipoUsuario.BARBEIRO);

        
        ServicoDTO dto = new ServicoDTO(null, "url", "title", "descricao", 100);
        
        
        Servico servico = new Servico();
        servico.setId(1L);
        servico.setUrl(dto.url());
        servico.setTitle(dto.title());
        servico.setDescricao(dto.descricao());
        servico.setPrice(dto.price());  
        servico.setUsuario(usuario);

        
        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuario));
        when(servicoRepository.save(any(Servico.class))).thenReturn(servico);

        
        ServicoDTO result = servicoService.cadastrarServico(dto);

        
        assertEquals(dto.url(), result.url());
        assertEquals(dto.title(), result.title());
        assertEquals(dto.descricao(), result.descricao());
        assertEquals(dto.price(), result.price());
    }

    @Test
    void atualizarServicoTest() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@teste.com");

        ServicoDTO dto = new ServicoDTO(1L, "newUrl", "newTitle", "newDescricao", 120);
        Servico servicoExistente = new Servico();
        servicoExistente.setId(1L);
        servicoExistente.setUrl("url");
        servicoExistente.setTitle("title");
        servicoExistente.setDescricao("descricao");
        servicoExistente.setPrice(100);
        servicoExistente.setUsuario(usuario);

        Servico servicoAtualizado = new Servico();
        servicoAtualizado.setId(1L);
        servicoAtualizado.setUrl(dto.url());
        servicoAtualizado.setTitle(dto.title());
        servicoAtualizado.setDescricao(dto.descricao());
        servicoAtualizado.setPrice(dto.price());
        servicoAtualizado.setUsuario(usuario);

        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuario));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servicoExistente));
        when(servicoRepository.save(any(Servico.class))).thenReturn(servicoAtualizado);

        ServicoDTO result = servicoService.atualizarServico(1L, dto);

        assertEquals(dto.url(), result.url());
        assertEquals(dto.title(), result.title());
        assertEquals(dto.descricao(), result.descricao());
        assertEquals(dto.price(), result.price());
    }

    @Test
    void deletarServicoTest() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@teste.com");

        Servico servico = new Servico();
        servico.setId(1L);
        servico.setUsuario(usuario);

        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuario));
        when(servicoRepository.findById(1L)).thenReturn(Optional.of(servico));

        servicoService.deletarServico(1L);

        verify(servicoRepository, times(1)).deleteById(1L);
    }

    @Test
    void listarServicosUsuarioTest() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@teste.com");

        Servico servico = new Servico();
        servico.setId(1L);
        servico.setUsuario(usuario);

        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuario));
        when(servicoRepository.findByUsuarioId(1L)).thenReturn(List.of(servico));

        List<ServicoDTO> result = servicoService.listarServicosUsuario();

        assertEquals(1, result.size());
        assertEquals(servico.getUrl(), result.get(0).url());
    }
}