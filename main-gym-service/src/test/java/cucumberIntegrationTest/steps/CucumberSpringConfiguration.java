package cucumberIntegrationTest.steps;


import com.epam.upskill.config.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import upskill.GymApplication;


@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfig.class)
@SpringBootTest(classes = GymApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {

}
