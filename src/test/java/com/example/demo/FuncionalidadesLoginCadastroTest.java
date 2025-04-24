package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.DTOs.LoginRequestDTO;
import com.example.demo.DTOs.UsuarioDTO;
import com.example.demo.models.Usuario;
import com.example.demo.services.UsuarioService;
import com.example.demo.controllers.AuthController;
import com.example.demo.controllers.UsuarioController;

public class FuncionalidadesLoginCadastroTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthController authController;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastroUsuarioSucesso() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(1L, "Nome", "email@test.com", "senha", "Endereco", null, null);
        when(usuarioService.cadastrarUsuario(usuarioDTO)).thenReturn(new Usuario());

        ResponseEntity<?> response = usuarioController.cadastrarUsuario(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testLoginUsuarioNaoEncontrado() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("naoexiste@test.com", "senha");

        when(usuarioService.autenticarUsuario(loginRequest)).thenThrow(new RuntimeException("Usuário não encontrado"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Usuário não encontrado"));
    }

    @Test
    void testLoginFalhaCredenciaisInvalidas() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("email@test.com", "senhaErrada");

        when(usuarioService.autenticarUsuario(loginRequest)).thenThrow(new RuntimeException("Credenciais inválidas"));

        ResponseEntity<?> response = authController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testCadastroUsuarioEmailExistente() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(1L, "Nome", "email@test.com", "senha", "Endereco", null, null);
        when(usuarioService.cadastrarUsuario(usuarioDTO)).thenThrow(new RuntimeException("E-mail já cadastrado."));

        ResponseEntity<?> response = usuarioController.cadastrarUsuario(usuarioDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
