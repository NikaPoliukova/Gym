package com.epam.upskill.actuator;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
  @Autowired
  private MeterRegistry meterRegistry;

  @Bean
  public Counter customRequestsCounter() {
    return Counter.builder("my_custom_requests_total")
        .description("Total number of custom requests.")
        .register(meterRegistry);
  }

  @Bean
  public Timer customRequestLatencyTimer() {
    return Timer.builder("my_custom_request_latency_seconds")
        .description("Custom request latency in seconds.")
        .register(meterRegistry);
  }
}