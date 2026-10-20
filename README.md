# Dibimbing — Web UI Test Automation

A Selenium WebDriver + TestNG automation framework for testing the
[SauceDemo](https://www.saucedemo.com/v1/) web application. It follows the
**Page Object Model (POM)** design pattern and supports parallel, cross-browser,
and CI (GitHub Actions) execution.

## Tech Stack

| Tool | Version | Purpose |
| --- | --- | --- |
| Java | 17+ | Language |
| Gradle | 8.10 (wrapper) | Build & dependency management |
| Selenium Java | 4.25.0 | Browser automation |
| WebDriverManager | 5.9.2 | Automatic driver binary management |
| TestNG | 7.10.2 | Test runner, grouping & parallelism |
| Log4j 2 | 2.23.1 | Logging |

## Prerequisites

- **JDK 17** or newer (`java -version` to check)
- **Google Chrome** and/or **Firefox** installed locally
- No need to install Gradle — use the bundled wrapper (`./gradlew`)
- Driver binaries are resolved automatically by WebDriverManager

## Getting Started

```bash
# Clone the repository
git clone <repository-url>
cd dibimbing

# Run the full test suite (uses src/test/resources/testng.xml)
./gradlew test
```

On Windows use `gradlew.bat test`.

### Running Options

```bash
# Run against a specific environment (loads <env>.properties)
./gradlew test -Denv=staging

# Override the parallel thread count defined in testng.xml
./gradlew test -PthreadCount=5

# Clean build then test
./gradlew clean test
```

### Viewing Results

After a run, TestNG/Gradle reports are generated under:

```
build/reports/tests/test/index.html   # Gradle HTML report
build/test-results/test/              # Raw XML results
```

Open the HTML report in a browser to inspect passed/failed/skipped tests.

## How It Works

1. **Suite definition** — `testng.xml` declares the suite, the `browser`
   parameter, parallel mode (`parallel="methods"`, `thread-count="3"`), and which
   test classes/groups to run (e.g. the `smoke` group).
2. **Config loading** — `@BeforeSuite` in `BaseTest` calls `ConfigManager` to load
   `src/test/resources/config/<env>.properties` (defaults to `staging`). This
   provides `baseUrl`, usernames, and password.
3. **Driver setup** — Before each test method, `BaseTest.setUp()` asks
   `DriverManager` to spin up a browser. `DriverManager` keeps each browser in a
   `ThreadLocal<WebDriver>` so parallel tests stay isolated. It auto-detects CI
   (`GITHUB_ACTIONS=true`) and switches to headless Chrome with CI-friendly flags.
4. **Test execution** — Each `@Test` drives the app through Page Objects
   (e.g. `LoginPage`), which extend `BasePage` (shared waits, scrolling, and
   click-with-retry helpers backed by `PageFactory`).
5. **Teardown** — `@AfterMethod` quits the driver and clears the ThreadLocal.

### Environment / CI

`DriverManager` reads these environment variables when running in CI:

| Variable | Meaning |
| --- | --- |
| `GITHUB_ACTIONS` | When `true`, enables headless + CI Chrome flags |
| `CHROMEDRIVER_PATH` | Path to a pre-installed chromedriver |
| `CHROME_BIN` | Path to the Chrome binary |

## Project Structure

```
dibimbing/
├── build.gradle.kts              # Build config, dependencies, TestNG task
├── settings.gradle.kts           # Root project name
├── gradlew / gradlew.bat         # Gradle wrapper scripts
├── gradle/wrapper/               # Gradle wrapper (version 8.10)
└── src/
    ├── main/java/
    │   ├── commons/
    │   │   └── BasePage.java      # Base Page Object: waits, scroll, click-retry
    │   ├── saucedemo/
    │   │   └── LoginPage.java     # Page Object for the Login page
    │   └── org/example/
    │       └── Main.java          # Standalone entry point (demo)
    └── test/
        ├── java/
        │   ├── core/
        │   │   ├── BaseTest.java       # TestNG lifecycle: config + driver setup
        │   │   ├── ConfigManager.java  # Loads env .properties files
        │   │   └── DriverManager.java  # ThreadLocal WebDriver factory (CI aware)
        │   └── tests/
        │       └── LoginTests.java     # Login smoke tests
        └── resources/
            ├── testng.xml              # Suite: parallelism, groups, parameters
            └── config/
                └── staging.properties  # baseUrl + test credentials
```

### Adding a New Test

1. Create a Page Object under `src/main/java/<app>/` extending `BasePage`.
2. Add a test class under `src/test/java/tests/` extending `BaseTest`.
3. Annotate methods with `@Test`, assign a `groups` value, and register the class
   in `testng.xml`.

## License

Copyright © muhamad.rizki. All rights reserved.
