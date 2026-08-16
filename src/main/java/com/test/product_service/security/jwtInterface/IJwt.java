package com.test.product_service.security.jwtInterface;

public interface IJwt {

    String extractUsername(String token);

    Long extractUserId(String token);

    String extractRole(String token);

    boolean isAccessTokenValid(String token);

}