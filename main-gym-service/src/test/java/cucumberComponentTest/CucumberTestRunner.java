package cucumberComponentTest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"cucumberComponentTest.steps"},
    plugin = {"pretty", "html:target/cucumber-reports/component"}
)
public class CucumberTestRunner {
}
