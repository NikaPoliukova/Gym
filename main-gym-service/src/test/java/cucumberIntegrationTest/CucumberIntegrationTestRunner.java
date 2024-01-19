package cucumberIntegrationTest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/integration_features",
    glue = {"cucumberIntegrationTest.steps"},
    plugin = {"pretty", "html:target/cucumber-reports/integration"}
)
public class CucumberIntegrationTestRunner {
}
