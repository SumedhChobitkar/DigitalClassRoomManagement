package com.DigitalClassRoomManagement.security;

import com.DigitalClassRoomManagement.Enum.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

//@Service
//public class JwtService {

//    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//    // Generate token with email + role
//    public String generateToken(String email, Role role) {
//        return Jwts.builder()
//                .setSubject(email)
//                .claim("role", role)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
//                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    // Extract username (email)
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public String getRoleFromToken(String token) {
//        return extractAllClaims(token).get("role", String.class);
//    }
//
//    public boolean validateToken(String token, String userEmail) {
//        return extractUsername(token).equals(userEmail) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
//        final Claims claims = extractAllClaims(token);
//        return resolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}

    @Service
    public class JwtService {

        private final Key SECRET_KEY;

        public JwtService(@Value("${jwt.secret}") String secretKey) {
            this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
        }

        public String generateToken(String email, Role role) {
            return Jwts.builder()
                    .setSubject(email)
                    .claim("role", "ROLE_" + role.name())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                    .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                    .compact();
        }

        public String extractUsername(String token) {
            return extractClaim(token, Claims::getSubject);
        }

        public boolean validateToken(String token, String userEmail) {
            return extractUsername(token).equals(userEmail) && !isTokenExpired(token);
        }

        private Claims extractAllClaims(String token) {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        private boolean isTokenExpired(String token) {
            return extractExpiration(token).before(new Date());
        }

        private <T> T extractClaim(String token, Function<Claims, T> resolver) {
            return resolver.apply(extractAllClaims(token));
        }

        private Date extractExpiration(String token) {
            return extractClaim(token, Claims::getExpiration);
        }
    }
