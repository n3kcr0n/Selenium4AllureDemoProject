# Java Selenium 4 Automation Framework

A test automation framework built with Java, Selenium 4, TestNG, Maven, and REST Assured. Supports UI and API testing, multiple environments, configurable browsers, automatic screenshots on UI test failures, and Allure reporting.

## Features

- **Page Object Model (POM):** Page objects use the Singleton design pattern.
- **Selenium 4:** Browser-based UI automation.
- **TestNG:** Test execution and organization.
- **Maven:** Build and dependency management.
- **Multiple Browser Support:** Chrome, Firefox, and Edge.
- **Multiple Environment Support:** DEV, QA, and UAT using separate properties files.
- **Runtime Configuration:** Override browser and environment settings through Maven command-line parameters.
- **REST API Testing:** API automation using REST Assured.
- **Automatic Screenshots:** Captures screenshots automatically when UI tests fail.
- **Allure Reporting:** Generates interactive test execution reports.
- Parallel Test Execution: Runs multiple test methods concurrently using TestNG and Maven Surefire, with up to five 
  execution threads configured to improve test execution efficiency.

## Prerequisites

Install the following tools before running the framework:

- Java JDK
- Apache Maven
- Google Chrome, Mozilla Firefox, or Microsoft Edge
- Allure Commandline (Allure CLI)

Verify your installations:

```bash
java -version
mvn -version
allure --version
```

## Running Tests

Run the full test suite using the default configuration:

```bash
mvn clean test
```

### Running Specific Tests

**Run all test methods in `MixedUIApiTest`:**

```bash
mvn clean test -Dtest=MixedUIApiTest
```

**Run only the API registration and UI deletion test:**

```bash
mvn clean test -Dtest=MixedUIApiTest#registerViaApiAndDeleteTodo
```

**Run only the API creation and UI verification test:**

```bash
mvn clean test -Dtest=MixedUIApiTest#createTodoViaApi
```

**Run all discovered tests:**

```bash
mvn clean test
```

**Notes:**
- Use the test class name without the `.java` extension.
- Use the exact test method name after `#`.
- These commands use Maven Surefire's test selection, not necessarily the test methods configured in `testng.xml`.
- The configured Surefire parallel execution settings apply when the selected tests run under that configuration.


## Browser Configuration

The framework supports Chrome, Firefox, and Edge. Chrome is the default browser.

Override the browser using the `browser` system property.

**Chrome (default):**
```bash
mvn clean test
```

**Chrome:**
```bash
mvn clean test -Dbrowser=chrome
```

**Firefox:**
```bash
mvn clean test -Dbrowser=firefox
```

**Edge:**
```bash
mvn clean test -Dbrowser=edge
```

## Environment Configuration

The framework supports multiple environments through separate property files:

- DEV
- QA
- UAT

Select the target environment using the `env` system property under `resources` and env folder.

**DEV:**
```bash
mvn clean test -Denv=dev
```

**QA:**
```bash
mvn clean test -Denv=qa
```

**UAT:**
```bash
mvn clean test -Denv=uat
```

## Combining Browser and Environment Configuration

Browser and environment settings can be overridden together.

For example, run Firefox tests against QA:

```bash
mvn clean test -Dbrowser=firefox -Denv=qa
```

Run Chrome tests against UAT:

```bash
mvn clean test -Dbrowser=chrome -Denv=uat
```

## REST API Testing

The framework supports REST API automation using REST Assured.

API tests can validate:

- HTTP status codes
- Response bodies and data
- API endpoint behavior
- API-driven workflows integrated with UI tests

## Automatic Screenshots on Test Failure

The framework automatically captures screenshots when UI tests fail.

These screenshots help investigate failures involving page states, element visibility, validation behavior, and unexpected browser behavior.

## Allure Reporting

The framework uses Allure to generate interactive test execution reports.

### Generate and Open the Report

After executing the tests, run:

```bash
allure serve .\allure-results\
```

This command generates and opens an interactive report using the results stored in the `allure-results` directory.

**Note:** Allure Commandline must be installed and available in your system PATH. Run the test suite before generating the report.

## Parallel Test Execution

The framework uses `ThreadLocal<WebDriver>` to support parallel test execution while maintaining an isolated WebDriver instance for each test thread.

### How It Works

```java
// Stores a separate WebDriver instance for each thread
protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();

@BeforeMethod
public void testSetup() {
    WebDriver driver = new DriverFactory().initDriver();
    this.driver.set(driver);
}
```

- **ThreadLocal WebDriver:** Each thread maintains its own WebDriver reference, preventing parallel tests from accidentally sharing the same browser session.
- **Test Setup:** The `@BeforeMethod` annotation initializes a new browser instance before each test method and stores it in the current thread's `ThreadLocal`.
- **Test Isolation:** Each test can interact with its own browser session, reducing interference between concurrently executing tests.

### Benefits

- Supports parallel execution of UI tests.
- Prevents WebDriver references from being shared across test threads.
- Improves test isolation and helps reduce browser-session conflicts.
- Can reduce overall execution time when multiple tests run concurrently.

### Important Considerations

`ThreadLocal<WebDriver>` provides thread-specific driver storage, but it does not enable parallel execution by itself. Parallel execution must also be configured in TestNG, such as through `testng.xml`.

After each test, the browser should be closed and the thread-local reference removed to prevent resource leaks.

For example:

```java
@AfterMethod
public void testTeardown() {
    WebDriver currentDriver = driver.get();

    if (currentDriver != null) {
        currentDriver.quit();
        driver.remove();
    }
}
```

## Configuration Properties

The framework uses a properties file to manage browser and application settings. Update these values based on your testing requirements.

```properties
URL=https://todo.qacart.com
BROWSER=CHROME
HEADLESS=True
```

### Configuration Options

| Property | Description | Example |
|---|---|---|
| `URL` | Base URL of the application under test | `https://todo.qacart.com` |
| `BROWSER` | Browser used to execute UI tests | `CHROME`, `FIREFOX`, `EDGE` |
| `HEADLESS` | Runs the browser without displaying its UI when enabled | `True` or `False` |

### Headless Mode

- `HEADLESS=True` — Runs tests in the background without displaying the browser window.
- `HEADLESS=False` — Opens the browser window during test execution.

### Reading the Headless Configuration

The framework retrieves the `HEADLESS` value from the properties file using `ConfigUtils` and converts it from a `String` into a `boolean`.

```java
String headlessProperty =
        ConfigUtils.getInstance().getProperty("HEADLESS");

boolean headless = Boolean.parseBoolean(headlessProperty);
```

**How it works:**

1. `getProperty("HEADLESS")` reads the configured value from the properties file.
2. `Boolean.parseBoolean(headlessProperty)` converts the string `"True"` or `"true"` to `true`, and `"False"` or `"false"` to `false`.
3. The resulting boolean can be used in `DriverFactory` to configure the browser for headless execution.

**Example properties:**

```properties
HEADLESS=True
```

With this setting, `headless` becomes `true`. Set `HEADLESS=False` to run the browser with its visible UI.



### Browser Configuration

Set `BROWSER` to `CHROME`, `FIREFOX`, or `EDGE` to select the browser for UI tests. Make sure the required browser and compatible WebDriver setup are available.

**Note:** The values in the properties file are the default configuration. You can override the browser or environment through Maven command-line properties if the framework is configured to support those overrides.



**Note:** The framework must consistently retrieve the driver using `driver.get()` wherever browser interactions occur. This ensures each test operates on the WebDriver instance assigned to its current thread.



## Technology Stack

| Technology | Purpose |
|---|---|
| Java | Programming language |
| Selenium 4 | UI automation |
| TestNG | Test execution and organization |
| Maven | Build and dependency management |
| REST Assured | API automation |
| Allure | Test reporting |
| Page Object Model | UI test architecture |
| Singleton Pattern | Page object instance management |

## Author

**Ron Macayaon**  
Automation QA Engineer
