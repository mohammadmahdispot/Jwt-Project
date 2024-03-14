package org.example;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JWTExample {
    private static final  String SECRET_KEY = "mySecretKey";
    private static final long EXPIRATION_TIME = 864_000_000;
    public static void main(String[] args) {
        // Create a JWT
        String token = Jwts.builder()
                .setSubject("user123")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        System.out.println("Generated JWT: " + token);

        // Verify and extract information from JWT
        try {
            String subject = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
            System.out.println("Extracted Subject from JWT: " + subject);
        } catch (Exception e) {
            System.err.println("Invalid JWT: " + e.getMessage());
        }
    }
}
