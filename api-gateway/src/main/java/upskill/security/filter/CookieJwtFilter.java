package upskill.security.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import upskill.security.JwtUtil;
import upskill.security.RouterValidator;

@Component
public class CookieJwtFilter extends AbstractJwtFilter {

  public CookieJwtFilter(JwtUtil jwtUtil, RouterValidator routerValidator) {
    super(jwtUtil, routerValidator);
  }

  @Override
  public String parseToken(ServerWebExchange exchange) {
    return jwtUtil.extractTokenFromCookies(exchange.getRequest().getCookies());
  }
}
