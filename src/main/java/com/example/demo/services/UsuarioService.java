package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTOs.LoginRequestDTO;
import com.example.demo.DTOs.UsuarioDTO;
import com.example.demo.models.TipoUsuario;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario cadastrarUsuario(UsuarioDTO usuarioDTO) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDTO.email());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.nome());
        usuario.setEmail(usuarioDTO.email());
        usuario.setEndereco(usuarioDTO.endereco());
        usuario.setTipo(usuarioDTO.tipo());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.senha()));

        return usuarioRepository.save(usuario);
    }

    public Usuario autenticarUsuario(LoginRequestDTO loginRequestDTO) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequestDTO.senha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

        public String getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        throw new RuntimeException("Erro: Usuário não autenticado");
    }

        public Usuario getUsuarioLogado() {
        String email = getUsuarioAutenticado();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado"));
    }

        public Usuario buscarBarbeiroPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Usuario> listarBarbeiros() {
        return usuarioRepository.findByTipo(TipoUsuario.BARBEIRO);
    }
}
