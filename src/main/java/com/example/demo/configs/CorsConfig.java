package com.example.demo.configs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // Permite qualquer origem
        config.addAllowedMethod("*"); // Permite todos os métodos HTTP
        config.addAllowedHeader("*"); // Permite todos os cabeçalhos
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}