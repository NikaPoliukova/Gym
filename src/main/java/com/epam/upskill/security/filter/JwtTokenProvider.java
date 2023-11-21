//package com.epam.upskill.security.filter;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import io.jsonwebtoken.Jwts;
//import java.time.LocalDate;
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//  @Value("${jwt.secret}")
//  private String jwtSecret;
//
//  @Value("${jwt.expiration}")
//  private int jwtExpiration;
//
//  public String generateToken(String username) {
//    Date now = new Date();
//    Date expiryDate = new Date(now.getTime() + jwtExpiration * 1000);
//
//    return Jwts.builder()
//        .setSubject(username)
//        .setIssuedAt(now)
//        .setExpiration(expiryDate)
//        .signWith(SignatureAlgorithm.HS512, jwtSecret)
//        .compact();
//  }
//
//  public String getUsernameFromToken(String token) {
//    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
//  }
//
//  public boolean validateToken(String token) {
//    try {
//      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
//      return true;
//    } catch (Exception e) {
//      // Handle exception, e.g., token expired or invalid signature
//      return false;
//    }
//  }
//}