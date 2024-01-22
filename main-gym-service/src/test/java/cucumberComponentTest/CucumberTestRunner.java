package cucumberComponentTest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@RunWith(Cucumber.class)
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@ActiveProfiles("test")
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"cucumberComponentTest.steps"},
    plugin = {"pretty", "html:target/cucumber-reports/component"}
)
public class CucumberTestRunner {
}
