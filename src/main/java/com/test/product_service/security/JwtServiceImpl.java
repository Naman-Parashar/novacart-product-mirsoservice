package com.test.product_service.security;

import com.test.product_service.config.JwtProperties;
import com.test.product_service.security.jwtInterface.IJwt;
import com.test.product_service.uttils.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwt {

    private final JwtProperties jwtProperties;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    @Override
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    @Override
    public boolean isAccessTokenValid(String token) {
        return extractTokenType(token) == TokenType.ACCESS
                && !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<Claims,T>  claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey(){
        byte[] key = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(key);
    }

    private TokenType extractTokenType(String token) {
        String tokeType = extractClaim(token, claims -> claims.get("tokenType", String.class));
        return TokenType.valueOf(tokeType);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }
}
