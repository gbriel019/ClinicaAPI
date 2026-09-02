package com.clinica.api.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarAccessToken(UserDetails userDetails) {

        return Jwts.builder()
                .claim("type", "access")
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSecretKey())
                .compact();
    }

    public String gerarRefreshToken(UserDetails userDetails) {

        return Jwts.builder()
                .claim("type", "refresh")
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSecretKey())
                .compact();
    }


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extrairUsername(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    public String extrairTipoToken(String token) {
        return extrairClaim(token, claims -> claims.get("type", String.class));
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(extrairTipoToken(token));
    }

    public boolean tokenValido(String token, UserDetails userDetails) {

        String username = extrairUsername(token);

        return username.equals(userDetails.getUsername())
                && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {
        return extrairClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    private <T> T extrairClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }
}