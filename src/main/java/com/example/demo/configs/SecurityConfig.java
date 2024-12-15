package com.example.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.services.JwtAuthenticationFilter;
import com.example.demo.services.TokenService;
import com.example.demo.services.UsuarioService;

@Configuration
public class SecurityConfig {

    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public SecurityConfig(TokenService tokenService, UsuarioService usuarioService) {
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/usuarios/cadastro", "/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.disable())
            .addFilterBefore(new JwtAuthenticationFilter(tokenService, usuarioService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
