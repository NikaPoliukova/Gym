package upskill.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import upskill.security.JwtUtil;

import java.net.URI;

@RefreshScope
@Component
@RequiredArgsConstructor
public class JwtTokenFilterForWorkload implements GatewayFilter {

  private final JwtUtil jwtUtil;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    var token = exchange.getRequest().getHeaders().get("Authorization").get(0);
    if (token != null && jwtUtil.validateToken(jwtUtil.extractToken(token))) {
      exchange.getResponse().setStatusCode(HttpStatus.OK);
    } else {
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return redirectToLogin(exchange);
    }
    return chain.filter(exchange);
  }

  private Mono<Void> redirectToLogin(ServerWebExchange exchange) {
    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
    exchange.getResponse().getHeaders().setLocation(URI.create("/gym-service/login"));
    return exchange.getResponse().setComplete();
  }
}
