package upskill.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

  public static final List<String> openApiEndpoints = List.of(
       "/gym-service/api/v1/registration/**",  "/login",
      "/gym-service/login", "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**"
  );

  public Predicate<ServerHttpRequest> isSecured =
      request -> openApiEndpoints
          .stream()
          .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
