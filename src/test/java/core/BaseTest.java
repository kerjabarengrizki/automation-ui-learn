package core;

import java.util.Properties;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    protected static Properties config;

    @BeforeSuite(alwaysRun = true)
    public void loadConfig() {
        ensureConfigLoaded();
    }

    /** Loads config if it hasn't been already. Safety net for runs where @BeforeSuite is skipped. */
    private static synchronized void ensureConfigLoaded() {
        if (config != null) return;
        String env = System.getProperty("env");
        env = (env == null || env.isEmpty()) ? "staging" : env;
        config = ConfigManager.loadProperties(env);
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        ensureConfigLoaded();
        DriverManager.initDriver(browser);
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().get(config.getProperty("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

}
