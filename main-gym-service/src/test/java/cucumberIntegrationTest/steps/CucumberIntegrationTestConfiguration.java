package cucumberIntegrationTest.steps;


import com.epam.upskill.config.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import upskill.GymApplication;


//@CucumberContextConfiguration
//@Import(TestConfig.class)
//@ContextConfiguration(classes = TestConfig.class)
//@ActiveProfiles("test")
//@TestPropertySource(properties = "spring.config.activate.on-profile=test")
//@SpringBootTest(classes = GymApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class CucumberIntegrationTestConfiguration {
//}
