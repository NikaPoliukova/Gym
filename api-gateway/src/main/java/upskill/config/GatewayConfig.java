package upskill.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import upskill.security.filter.CookieJwtFilter;
import upskill.security.filter.HeaderJwtFilter;


@RequiredArgsConstructor
@Configuration
public class GatewayConfig {
  private final CookieJwtFilter jwtTokenFilter;
  private final HeaderJwtFilter jwtTokenFilterForWorkload;
  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("gym-service", r -> r
            .path("/gym-service/**")
            .filters(f -> f.stripPrefix(1).filter(jwtTokenFilter))
            .uri("lb://gym-service"))

        .route("workload-service", r -> r
            .path("/workload-service/**")
            .filters(f -> f.stripPrefix(1).filter(jwtTokenFilterForWorkload))
            .uri("lb://workload-service"))
        .build();
  }
}

