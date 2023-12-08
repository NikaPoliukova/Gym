package upskill;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import upskill.controller.GymController;


@SpringBootApplication
@EnableEurekaClient
@RequiredArgsConstructor
@EnableFeignClients(basePackages = "upskill.client")
public class GatewayApplication {

  private GymController gymController;

  public static void main(String[] args) {
    SpringApplication.run(GatewayApplication.class, args);
  }

}