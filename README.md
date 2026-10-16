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

## 📂 Project Architecture

```text
src/
└── test/
    ├── java/
    │   └── Framework/
    │       ├── Api/                       # API request handlers and service clients
    │       ├── BaseTest/                  # Core setup and teardown configurations for tests
    │       ├── DriverFactory/             # WebDriver initialization and management
    │       ├── Models/                    # POJOs and data models (e.g., UserModel)
    │       ├── PageObjects/               # Page Object Model (POM) classes and locators
    │       │   ├── CreatNewTodoPage.java
    │       │   ├── RegistrationPage.java
    │       │   └── TodoPage.java
    │       ├── Utils/                     # Helper utilities (ConfigUtils, TestUtils)
    │       └── TestCases/                 # Test suites (UI, API, and Mixed tests)
    │           ├── MixedUIApiTest.java
    │           ├── RegisterUserApiTest.java
    │           ├── RegisterUserUiTest.java
    │           └── TodoTest.java
    └── resources/
        └── env/                           # Environment-specific configuration properties
            ├── dev.properties
            ├── qa.properties
            └── uat.properties
```

## 🛠️ Component Description

* **`Api/`**: Contains classes responsible for handling backend API requests, enabling hybrid testing workflows.
* **`BaseTest/`**: Houses the base test configuration class (`BaseTest`)[cite: 1] that manages common preconditions, such as browser setup, execution hooks, and teardown procedures.
* **`DriverFactory/`**: Implements the Factory design pattern via `DriverFactory`[cite: 1] to cleanly instantiate, configure, and manage parallel or sequential WebDriver instances.
* **`Models/`**: Contains data objects and POJOs like `UserModel`[cite: 1] to structure data payloads seamlessly across tests.
* **`PageObjects/`**: Implements the **Page Object Model (POM)** design pattern to separate test logic from UI locators:
  * `CreatNewTodoPage`[cite: 1]: Handles actions and element locators for creating new to-do items.
  * `RegistrationPage`[cite: 1]: Manages user registration inputs and form actions.
  * `TodoPage`[cite: 1]: Handles validation and interaction with the main to-do list interface.
* **`Utils/`**: Utility and helper classes providing common helper functions:
  * `ConfigUtils`[cite: 1]: Manages reading configuration and environment property files.
  * `TestUtils`[cite: 1]: Houses reusable helper methods for test execution.
* **`TestCases/`**: Contains the actual automated test suites categorized by test type:
  * `MixedUIApiTest`[cite: 1]: Integration tests combining both UI and API validations.
  * `RegisterUserApiTest`[cite: 1]: API-level test scenarios for user registration.
  * `RegisterUserUiTest`[cite: 1]: UI-level automated test scenarios for user registration flows.
  * `TodoTest`[cite: 1]: Functional test suites for the to-do features.
* **`resources/env/`**: Environment configuration property files (`dev.properties`, `qa.properties`, `uat.properties`)[cite: 1] used to manage environment-specific parameters dynamically.

### Running Tests

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


## Technology Stack and Dependencies

This project is a Java-based Selenium automation framework managed by Apache Maven. It supports UI automation, API testing, test data generation, JSON processing, parallel test execution, and Allure reporting.

### 1. Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 25 | Programming language used to develop the automation framework. |
| Apache Maven | — | Manages project dependencies, builds the project, and executes tests. |
| Selenium WebDriver | 4.50.0 | Automates browser interactions for end-to-end UI testing. |
| TestNG | 7.12.0 | Provides test annotations, assertions, test organization, and execution control. |
| Rest Assured | 6.0.1 | Supports REST API testing by sending HTTP requests and validating responses. |
| JavaFaker | 1.0.2 | Generates realistic test data, such as names and email addresses. |
| Jackson Databind | 3.2.3 | Supports JSON serialization and deserialization for working with JSON data. |
| Allure TestNG | 3.0.0 | Integrates TestNG test execution with Allure reporting. |
| Apache Commons IO | 2.22.0 | Provides utility methods for file operations and input/output handling. |
| AspectJ Weaver | 1.9.25.1 | Provides runtime instrumentation used by the configured Allure reporting integration. |
| Maven Surefire Plugin | 3.6.0 | Runs automated tests during Maven's test lifecycle and configures parallel execution. |

### 2. Maven Dependencies Explained

#### Selenium Java

**Artifact:** `org.seleniumhq.selenium:selenium-java`

Used to automate web browsers and interact with web elements. It supports operations such as clicking buttons, entering text, navigating pages, and validating UI behavior.

#### TestNG

**Artifact:** `org.testng:testng`

Used to organize and execute test cases through annotations such as `@Test`, `@BeforeMethod`, and `@AfterMethod`. It also provides assertions and test execution configuration.

**Scope:** `test` — available for test compilation and execution.

#### JavaFaker

**Artifact:** `com.github.javafaker:javafaker`

Generates sample data for test scenarios, helping reduce hardcoded test values and making tests easier to reuse.

#### Rest Assured

**Artifact:** `io.rest-assured:rest-assured`

Used for API automation, including sending HTTP requests, checking response status codes, validating response bodies, and testing API behavior.

**Scope:** `test` — available for test compilation and execution.

#### Jackson Databind

**Artifact:** `tools.jackson.core:jackson-databind`

Provides JSON data-binding capabilities for converting between JSON and Java objects. It can support API request and response handling when compatible with the project's JSON-processing setup.

#### Allure TestNG

**Artifact:** `io.qameta.allure:allure-testng`

Integrates Allure reporting with TestNG, allowing test results and supported execution details to be collected for report generation.

#### Apache Commons IO

**Artifact:** `commons-io:commons-io`

Provides reusable file and input/output utilities that can simplify common file-handling operations in the framework.

### 3. Test Execution and Parallelization

The project uses the **Maven Surefire Plugin 3.6.0** to execute automated tests.

The current configuration includes:

```xml
<parallel>methods</parallel>
<threadCount>5</threadCount>
```

- **Parallel mode — methods:** Allows eligible test methods to run concurrently.
- **Thread count — 5:** Configures up to five parallel test threads for this execution mode.

Parallel execution can reduce total test runtime. However, tests must be designed to avoid shared-state conflicts, such as multiple tests unintentionally using the same WebDriver instance or modifying the same test data.

The configuration also uses the AspectJ Weaver Java agent through the `argLine` property, supporting the project's configured reporting instrumentation.

### 4. Dependency Management

The project uses Maven's `pom.xml` to define dependencies, versions, and build plugins.

It also imports the Allure BOM (Bill of Materials), which helps manage versions of related Allure dependencies centrally.

```xml
<allure.version>3.0.0</allure.version>
```

This property is referenced by the Allure BOM and the `allure-testng` dependency, helping keep their configured versions consistent.

### 5. Running the Tests

Run the following command from the project root:

```bash
mvn clean test
```

This command removes previous build output and runs the tests through Maven Surefire.

After test execution, generate and view an Allure report using the Allure CLI:

```bash
allure serve allure-results
```

The Allure CLI must be installed separately. In CI/CD, GitHub Actions can execute the tests, generate and publish the report, and send email notifications about the pipeline run.


## Author

**Ron Macayaon**  
Automation QA Engineer
