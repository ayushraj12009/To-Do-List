package com.apiwiz.Config;

//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.security.core.Authentication;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//
//public class JwtProvider  {
//
//    // Secret key used for signing and parsing JWT tokens
//    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
//
//    // Method to generate JWT token based on user authentication
//    public static String generateToken (Authentication auth){
//        String jwt = Jwts.builder()
//                .setIssuer("APIWIZ").setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime()+86400000))
//                .claim("email", auth.getName())
//                .signWith(key)
//                .compact();
//
//        return jwt;
//    }
//
//    // Method to extract email from JWT token
//    public static String getEamilFromJwtToken(String jwt){
//        jwt = jwt.substring(7);
//
//        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
//
//        String email = String.valueOf(claims.get("email"));
//
//        return email;
//
//    }
//
//
//}
//

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtProvider {

    // Secret key used for signing and parsing JWT tokens
    private static final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // Method to generate JWT token based on user authentication
    public static String generateToken(Authentication auth) {
        return Jwts.builder()
                .setIssuer("APIWIZ")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .claim("email", auth.getName())
                .signWith(key)
                .compact();
    }

    // Method to extract email from JWT token
    public static String getEmailFromJwtToken(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            Claims body = claimsJws.getBody();
            return body.get("email", String.class);
        } catch (Exception e) {
            // Token is invalid or malformed
            return null;
        }
    }
}
