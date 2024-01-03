package upskill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
@EnableEurekaClient
public class TrainerWorkloadApplication {
  @Autowired
  private MongoTemplate mongoTemplate;

  public static void main(String[] args) {
    SpringApplication.run(TrainerWorkloadApplication.class, args);
  }
}