package com.flower.ws.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${app.jwtSecret}")
    private String secret;

    @Value("${app.jwtExpirationMs}")
    private Long expiry;

    public String generateToken(final UserDetails userDetails) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + expiry);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String extractUsername(final String token) {
        return getClaimsJws(token).getBody().getSubject();
    }

    public boolean isValid(final String token) {
        try {
            getClaimsJws(token);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    private Jws<Claims> getClaimsJws(final String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    }
}