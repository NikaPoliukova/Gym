package upskill.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@RequiredArgsConstructor
@Component
public class JwtUtil {

  private final Key secret = getSecretKeySpec();
  private static final String JWT = "Bearer";
  private static final String SECRET_KEY = "111111111111122222222222222224444444444444444444444444444444444444ty" +
      "utghfcjdecuijednklfcmedfbdsziokplfhivokfplddcgdvgcvdduhvuifmklvmdklmvkfhgvuihfuid8";


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

  public SecretKeySpec getSecretKeySpec() {
    byte[] secretKeyBytes = SECRET_KEY.getBytes();
    return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
  }

  public String extractTokenFromCookies(MultiValueMap<String, HttpCookie> cookies) {
    var tokenCookies = cookies.get(JWT);
    if (tokenCookies != null && !tokenCookies.isEmpty()) {
      return tokenCookies.get(0).getValue();
    }
    return null;
  }
  public String extractToken(String authorizationHeader) {
    return authorizationHeader.substring(7);
  }
}
