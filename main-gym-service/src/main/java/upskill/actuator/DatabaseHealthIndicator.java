package upskill.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import upskill.exception.ConnectionException;

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
      return Health.up()
          .withDetail("database", connection.getMetaData().getDatabaseProductName())
          .withDetail("url", connection.getMetaData().getURL())
          .withDetail("user", connection.getMetaData().getUserName())
          .build();

    } catch (SQLException e) {
      throw new ConnectionException("No connection");
    }

  }
}