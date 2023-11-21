package com.epam.upskill.actuator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {

  @Override
  public Health health() {
     return Health.up().withDetail("GymApplication", "Available")
        .withDetail("URL", "http://localhost:8091").build();
  }
}
