package com.epam.upskill.util;


import com.epam.upskill.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
  // @Value("${security.secretKey}")
//  private static String secretKey;
  @Value("${security.refresh_secret}")
  private static String jwtRefreshSecret;

  String secretKey = "111111111111122222222222222224444444444444444444444444444444444444tyutghfcjdecuijednklfcmed" +
      "fbdsziokplfhivokfplddcgdvgcvdduhvuifmklvmdklmvkfhgvuihfuid8";


  public String generateAccessToken(User user) {
    Claims claims = Jwts.claims().setSubject(user.getUsername());
    claims.put("username", user.getUsername());
    Date expirationDate = generateExpirationDate(50);
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  public String generateRefreshToken(String username) {
    Date expirationDate = generateExpirationDate(5000);
    return Jwts.builder()
        .setSubject(username)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  public String getRefreshTokenFromRequest(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, "JWT-REFRESH");
    if (cookie != null) {
      return cookie.getValue();
    }
    return null;
  }

  public String getAccessTokenFromRequest(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, "JWT");
    if (cookie != null) {
      return cookie.getValue();
    }
    return null;
  }


  public String refreshAccessToken(String expiredToken) {
    Claims claims;
    try {
      claims = Jwts.parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(expiredToken)
          .getBody();
    } catch (ExpiredJwtException e) {
      claims = e.getClaims();
    }
    Date expirationDate = generateExpirationDate(50);
    return Jwts
        .builder()
        .setClaims(claims)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }


  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      throw new AuthenticationCredentialsNotFoundException("JWT token is not valid " + token);
    }
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
    return claims.getSubject();
  }

  public boolean isTokenExpired(String token) {
    Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  private Date getExpirationDateFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser()
          .setSigningKey(secretKey)
          .parseClaimsJws(token)
          .getBody();
    } catch (ExpiredJwtException e) {
      claims = e.getClaims();
    }
    return claims.getExpiration();
  }

  private Date generateExpirationDate(int min) {
    LocalDateTime now = LocalDateTime.now();
    Instant accessExpirationInstant = now.plusMinutes(min).atZone(ZoneId.systemDefault()).toInstant();
    return Date.from(accessExpirationInstant);
  }
}