package com.example.demo.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

    private static final String SECRET_KEY = "yU7rGz8t9HnW3qLpXsVwKmJ4NcZaR5Pb";
    private static final long TOKEN_EXPIRATION_TIME = 3600000;

    public String gerarToken(String email) {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), 
                                                SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String validarToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .build();
            Claims claims = parser.parseClaimsJws(token).getBody();

            String email = claims.getSubject();
            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("Token inválido: 'subject' ausente ou vazio");
            }

            return email;
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token malformado", e);
        } catch (JwtException e) {
            throw new RuntimeException("Erro ao processar o token JWT", e);
        }
    }
}
