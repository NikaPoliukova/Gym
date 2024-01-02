package upskill.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import upskill.security.filter.CookieJwtFilter;


@RequiredArgsConstructor
@Configuration
public class GatewayConfig {
  private final CookieJwtFilter jwtTokenFilter;

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("gym-service", r -> r
            .path("/gym-service/**")
            .filters(f -> f.stripPrefix(1).filter(jwtTokenFilter))
            .uri("lb://gym-service"))

        .route("workload-service", r -> r
            .path("/workload-service/**")
            .uri("lb://workload-service"))
        .build();
  }
}

