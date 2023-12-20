package upskill.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import upskill.security.JwtUtil;
import upskill.security.RouterValidator;

@RequiredArgsConstructor
public abstract class AbstractJwtFilter implements GatewayFilter {

  protected final JwtUtil jwtUtil;
  protected final RouterValidator routerValidator;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    if (routerValidator.isSecured.test(exchange.getRequest())) {
      var token = parseToken(exchange);
      if (token != null && jwtUtil.validateToken(token)) {
        exchange.getResponse().setStatusCode(HttpStatus.OK);
      } else {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return routerValidator.redirectToLogin(exchange);
      }
    }
    return chain.filter(exchange);
  }

  public abstract String parseToken(ServerWebExchange exchange);
}
