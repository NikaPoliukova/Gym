//package upskill.security.filter;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import upskill.security.JwtUtil;
//import upskill.security.RouterValidator;
//
//import java.util.Objects;
//
//@Component
//public class HeaderJwtFilter extends AbstractJwtFilter {
//  public HeaderJwtFilter(JwtUtil jwtUtil, RouterValidator routerValidator) {
//    super(jwtUtil, routerValidator);
//  }
//
//  @Override
//  public String parseToken(ServerWebExchange exchange) {
//    var header = Objects.requireNonNull(exchange.getRequest().getHeaders().get("Authorization")).get(0);
//    return jwtUtil.extractToken(header);
//  }
//}
