package com.example.demo.services;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

    private static final String SECRET_KEY = "yU7rGz8t9HnW3qLpXsVwKmJ4NcZaR5Pb"; // Utilize uma variável de ambiente para maior segurança

    public String gerarToken(String email) {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // Expiração em 1 hora
                .signWith(secretKey)
                .compact();
    }

    public String validarToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token malformado", e);
        } catch (JwtException e) {
            throw new RuntimeException("Erro ao processar o token JWT", e);
        }
    }
}
