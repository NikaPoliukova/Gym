package upskill.security;//package upskill.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import upskill.entity.User;


import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
  public static final String JWT_REFRESH = "JWT-REFRESH";
  public static final String JWT = "Bearer";

  private static final String secretKey = "111111111111122222222222222224444444444444444444444444444444444444ty" +
      "utghfcjdecuijednklfcmedfbdsziokplfhivokfplddcgdvgcvdduhvuifmklvmdklmvkfhgvuihfuid8";

  private final Key secret = getSecretKeySpec();

  public String generateAccessToken(User user) {
    Claims claims = Jwts.claims().setSubject(user.getUsername());
    claims.put("username", user.getUsername());
    Date expirationDate = generateExpirationDate(50);
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(expirationDate)
        .signWith(secret, SignatureAlgorithm.HS512)
        .compact();
  }

  public String generateRefreshToken(String username) {
    Date expirationDate = generateExpirationDate(5000);
    return Jwts.builder()
        .setSubject(username)
        .setExpiration(expirationDate)
        .signWith(secret, SignatureAlgorithm.HS512)
        .compact();
  }

  public String getRefreshTokenFromRequest(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, JWT_REFRESH);
    if (cookie != null) {
      return cookie.getValue();
    }
    return null;
  }

  public String getAccessTokenFromRequest(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, JWT);
    if (cookie != null) {
      return cookie.getValue();
    }
    return null;
  }


  public String refreshAccessToken(String expiredToken) {
    Claims claims;
    try {
      claims = Jwts.parserBuilder()
          .setSigningKey(secret)
          .build()
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
        .signWith(secret, SignatureAlgorithm.HS512)
        .compact();
  }


  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(secret)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secret)
        .build()
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
      claims = Jwts.parserBuilder()
          .setSigningKey(secret)
          .build()
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

  private SecretKeySpec getSecretKeySpec() {
    byte[] secretKeyBytes = secretKey.getBytes();
    return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
  }
}