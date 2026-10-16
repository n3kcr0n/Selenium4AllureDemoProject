## CI/CD Pipeline with GitHub Actions

This project uses **GitHub Actions** to automate the Selenium End-to-End (E2E) testing workflow, generate Allure reports, publish test reports to GitHub Pages, and send email notifications through Gmail.

The workflow is triggered by:
- Pushes to the `main` branch
- Pull requests targeting the `main` branch
- Manual execution through GitHub Actions (`workflow_dispatch`)

### Pipeline Stages

The workflow consists of four jobs:

| Job | Description |
|---|---|
| **Build Application** | Simulates the application build stage. |
| **Deploy Application** | Simulates deployment to the test environment. Runs after Build succeeds. |
| **Selenium E2E Tests** | Sets up Java 25, Chrome, and Maven dependencies, then executes `mvn clean test`. Generates and publishes the Allure report. |
| **Email Allure Report** | Sends an email containing job statuses and links to the latest published Allure report and GitHub Actions run. |

### Pipeline Flow

```text
Build Application
        |
        v
Deploy Application
        |
        v
Selenium E2E Tests
        |
        +----> Generate Allure Report
        |              |
        |              v
        |      Publish to GitHub Pages
        |
        v
Email Allure Report
```

The `needs` property controls job execution order. The email notification job uses `if: always()` so it can run even if an earlier job fails or is skipped.

**Note:** Build and Deploy currently use static console messages to illustrate a CI/CD pipeline. They do not perform actual application compilation or deployment.

### Technology and Tools

- **CI/CD:** GitHub Actions
- **Programming Language:** Java
- **Java Distribution:** Eclipse Temurin JDK 25
- **Build and Test Tool:** Apache Maven
- **Browser:** Google Chrome
- **Dependency Caching:** Maven dependency cache
- **Test Reporting:** Allure Report
- **Report Hosting:** GitHub Pages
- **Email Notifications:** Gmail SMTP over SSL

### Workflow File

The workflow configuration is located at:

```text
.github/workflows/selenium-e2e.yml
```

The Selenium test suite is executed using:

```bash
mvn clean test
```

Maven dependency caching is enabled through `actions/setup-java` to reduce repeated dependency downloads across workflow runs.

---

## Allure Test Reports

After test execution, the workflow attempts to generate an Allure report from the `allure-results` directory and publish the report history to the `gh-pages` branch.

**Latest Allure Report:**  
https://n3kcr0n.github.io/Selenium4AllureDemoProject/

The root URL is used to access the latest published report without hardcoding a numbered report path.

The workflow uses `if: always()` on the Allure generation and publishing steps to attempt report generation and publication even when the Maven test step fails.

**Note:** A new report is not guaranteed if test results are missing or report generation or publication fails.

---

## Email Notifications

The pipeline sends an automated email through Gmail SMTP after the Build, Deploy, and Test jobs finish.

The email contains:
- Build job status
- Deploy job status
- Test and reporting job status
- Link to the latest published Allure report
- Link to the specific GitHub Actions workflow run

**Example report links:**

- [View Latest Allure Report](https://n3kcr0n.github.io/Selenium4AllureDemoProject/)
- [View GitHub Actions Workflow](https://github.com/n3kcr0n/Selenium4AllureDemoProject/actions)

The notification job uses `needs: [build, deploy, test]` and `if: always()` to run after the required jobs finish, regardless of their results.

The reported Test status represents the entire `test` job, including report generation and publishing. It does not independently indicate the number of passed, failed, or skipped test cases.

---

## Gmail SMTP Configuration

Gmail SMTP is configured to send automated test report notifications. Credentials are stored securely in GitHub Actions repository secrets rather than hardcoded in the workflow.

### Step 1: Enable 2-Step Verification

1. Open [Google Account Security](https://myaccount.google.com/security).
2. Enable **2-Step Verification** if it is not already enabled.

### Step 2: Generate a Google App Password

1. Open [Google App Passwords](https://myaccount.google.com/apppasswords).
2. Sign in to the Gmail account that will send notifications.
3. Create an app password, for example, named `GitHub Actions`.
4. Copy the generated password and store it securely.

A Google App Password is used instead of the regular Gmail password. App Passwords require 2-Step Verification and may not be available for certain account types or security configurations.

### Step 3: Configure GitHub Repository Secrets

Open the repository's **Settings → Secrets and variables → Actions** page:

https://github.com/n3kcr0n/Selenium4AllureDemoProject/settings/secrets/actions

Create the following repository secrets:

| Secret Name | Description |
|---|---|
| `MAIL_USERNAME` | Gmail address used to send the notification |
| `MAIL_APP_PASSWORD` | Google App Password generated in Step 2 |
| `MAIL_TO` | Recipient email address |
| `TOKEN` | GitHub personal access token used by the existing GitHub Pages publishing step |

The `TOKEN` secret is required by the current report publishing configuration. Ensure it has sufficient permissions to publish to the repository's `gh-pages` branch.

### Step 4: Gmail SMTP Settings

The workflow uses the following SMTP configuration:

```yaml
server_address: smtp.gmail.com
server_port: 465
secure: true
```

Credentials are referenced using GitHub Actions secrets:

```yaml
username: ${{ secrets.MAIL_USERNAME }}
password: ${{ secrets.MAIL_APP_PASSWORD }}
to: ${{ secrets.MAIL_TO }}
```

The sender and recipient email addresses can be the same.

### Step 5: Verify Email Notifications

1. Commit and push the workflow configuration to the `main` branch, or trigger it manually through GitHub Actions.
2. Open the workflow run and inspect the `Email Allure Report` job.
3. Check the recipient's inbox for the notification.
4. Open the Allure report link to review the published results.

If email delivery fails, verify the repository secret names, Gmail App Password, and SMTP configuration.

### Security Considerations

- Never commit email credentials, App Passwords, or GitHub tokens to the repository.
- Store sensitive values in GitHub Actions Secrets.
- Avoid printing credentials in workflow logs.
- Repository secrets are generally unavailable to workflows triggered by pull requests from forks.
- Revoke or rotate credentials immediately if they are accidentally exposed.

---

## Future Improvements

- Replace simulated Build and Deploy jobs with actual build and deployment commands.
- Improve Allure report generation and publication when individual tests fail.
- Include passed, failed, and skipped test counts in email notifications.
- Upload Allure results and reports as GitHub Actions artifacts for troubleshooting.
- Add distinct email subjects or notifications for successful and failed test runs.
