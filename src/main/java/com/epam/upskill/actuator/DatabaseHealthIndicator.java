package com.epam.upskill.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

  private final DataSource dataSource;


  @Override
  public Health health() {
    try (Connection connection = dataSource.getConnection()) {
      boolean isDatabaseUp = true;

      if (isDatabaseUp) {
        return Health.up()
            .withDetail("database", connection.getMetaData().getDatabaseProductName())
            .withDetail("url", connection.getMetaData().getURL())
            .withDetail("user", connection.getMetaData().getUserName())
            .build();
      } else {
        return Health.down()
            .withDetail("error", "Database is not available")
            .build();
      }
    } catch (SQLException e) {
      return Health.down()
          .withDetail("error", "Error connecting to the database: " + e.getMessage())
          .build();
    }
  }
}