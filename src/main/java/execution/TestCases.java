package execution;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import coreFunctions.Functions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;

public class TestCases {
    WebDriver driver; // object for Chrome

    Functions functions = new Functions();  // Object

    // Reporting
    ExtentReports extent;
    ExtentTest test;
    ExtentSparkReporter spark;

    @BeforeSuite
    public void reporting() throws InterruptedException {
        String workingDir = "C:\\Users\\Huawe\\IdeaProjects\\";
        ExtentSparkReporter spark = new ExtentSparkReporter(workingDir + "mzna.html");

        // Report settings
        spark.config().setDocumentTitle("Challenge Report");
        spark.config().setReportName("Mzna Report For Test Automation");
        spark.config().setTheme(Theme.STANDARD); // Use white theme

        // System info
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Mzna");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Browser", "Chrome");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
    }

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterClass
    public void close() {
        extent.flush();
        driver.quit();
    }

    @BeforeMethod
    public void beforeTC(ITestResult method){
        String testCaseName = method.getMethod().getMethodName();
        String testCaseDesc = method.getMethod().getDescription();
        test = extent.createTest(testCaseName, testCaseDesc);
    }

    @AfterMethod
    public void afterTC(ITestResult result) throws InterruptedException {
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("failed");
        } else {
            System.out.println("pass");
        }

        System.out.println("After Test case ..........");
        driver.get("http://localhost:3000");
        Thread.sleep(4000);
    }

    // Note: Later, this method and locators will be refactored using the Page Object Model (POM) design pattern
    // to separate page elements and actions into dedicated classes for better maintainability and readability.
    public boolean login(String email, String password) throws InterruptedException {
        WebElement formEmail = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[1]/input"));
        Thread.sleep(400);
        formEmail.click();
        formEmail.sendKeys(email);
        Thread.sleep(400);
        test.info("Step 1: Email input entered successfully");

        WebElement formPassword = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/div[2]/input"));
        Thread.sleep(400);
        formPassword.click();
        formPassword.sendKeys(password);
        Thread.sleep(400);
        test.info("Step 2: Password input entered successfully");

        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/form/button"));
        loginButton.click();
        Thread.sleep(400);
        test.info("Step 3: Login button clicked successfully");

        // تحقق من الرسالة التي تظهر بعد تسجيل الدخول
        try {
            WebElement msg = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/div/div/div"));
            String messageText = msg.getText();

            if (messageText.contains("Invalid Email or Password!")) {
                test.info("Login failed due to invalid credentials");
                return false;  // Negative scenario
            } else if (messageText.contains("WelCome BacK!")) {
                test.info("Login successful");
                return true;  // Positive scenario
            } else {
                test.info("Login response unknown: " + messageText);
                return false;  // افتراض فشل إذا الرسالة غير معروفة
            }
        } catch (NoSuchElementException e) {
            // لو ما حصلنا الرسالة نفترض نجاح لأن الرسالة غير موجودة
            test.info("No error message found, assuming login success");
            return true;
        }
    }


    @Test(description = "Test Automation: TC-001-Login with valid credentials")
    public void TC1() throws InterruptedException {
        login("mznaat188@gmail.com", "mzna123");
        WebElement msg = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div[1]/h2"));
        Assert.assertTrue(msg.getText().contains("WelCome BacK!"));
        System.out.println("Login PASS test passed");
    }

    @Test(description = "Test Automation: Tc-002- Add")
    public void TC2() {
        try {
            WebElement addTask = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/form/div[1]/input"));
            addTask.sendKeys("Test For update and delete");
            test.info("Step 1: Entered task title");

            WebElement addDescrip = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/form/div[2]/textarea"));
            addDescrip.sendKeys("Test 1 For test new ...");
            test.info("Step 2: Entered task description");

            WebElement addButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[1]/form/button"));
            addButton.click();
            test.info("Step 3: Clicked Add button");

            test.pass("Add Task Test Passed!");

        } catch (Exception e) {
            test.fail("Add Task Test Failed! Error: " + e.getMessage());
            Assert.fail("Add Task Test Failed due to Exception: " + e.getMessage());
        }
    }

    @Test(description = "Test Automation: Tc-003- Update")
    public void TC3() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            WebElement firstEditBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/table/tbody/tr[1]/td[3]/button[3]")));
            firstEditBtn.click();
            test.info("Step 1: Clicked Edit button");

            WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("body > div.modal > div > div > div.modal-body > form > div:nth-child(1) > input")));
            inputField.clear();
            inputField.sendKeys("Updated To Do Title");
            test.info("Step 2: Updated text input");

            WebElement saveButton = driver.findElement(By.cssSelector("body > div.modal > div > div > div.modal-footer > button"));
            saveButton.click();
            test.info("Step 3: Clicked Save button");

            WebElement updatedTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id='root']/div/div[2]/div[2]/table/tbody/tr[1]/td[1]")));

            String actualText = updatedTitle.getText();
            String expectedText = "Updated To Do Title";

            System.out.println("Actual text: " + actualText);

            if (actualText.equals(expectedText)) {
                test.pass("Test Case for Edit Passed! Updated text matched: '" + actualText + "'");
                System.out.println("Test Case for Edit Passed!");
            } else {
                test.fail("Test Case for Edit Failed! Expected: '" + expectedText + "' Actual: '" + actualText + "'");
                System.out.println("Updated text mismatch.");
                Assert.fail("Updated text mismatch. Expected: '" + expectedText + "' Actual: '" + actualText + "'");
            }

        } catch (Exception e) {
            test.fail("Exception occurred during test execution: " + e.getMessage());
            System.out.println("Test Case for Edit Failed by Exception!");
            Assert.fail("Exception in Edit Test: " + e.getMessage());
        }
    }

    @Test(description = "Test Automation: Tc-004- delete")
    public void TC4() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            WebElement targetRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/table/tbody/tr[2]/td[2]")
            ));
            test.info("Step 1: Found target row to delete");

            WebElement deleteButton = targetRow.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/table/tbody/tr[2]/td[3]/button[4]"));
            deleteButton.click();
            test.info("Step 2: Clicked delete button");

            WebElement checkRow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"root\"]/div/div[2]/div[2]/table/tbody/tr[2]/td[1]")));

            String actualText = checkRow.getText();
            String deletedText = "Test For update and delete";

            System.out.println("Actual text after delete attempt: " + actualText);

            if (!actualText.contains(deletedText)) {
                test.pass("Deletion Test Passed! Text '" + deletedText + "' no longer found in row.");
                System.out.println("Deletion Test Passed!");
            } else {
                test.fail("Deletion Test Failed! Text '" + deletedText + "' still found in row.");
                System.out.println("Deletion Test Failed!");
                Assert.fail("Deletion Test Failed! Text '" + deletedText + "' still found in row.");
            }

        } catch (Exception e) {
            test.fail("Exception occurred during deletion test: " + e.getMessage());
            System.out.println("Deletion Test Failed by Exception!");
            Assert.fail("Exception in Deletion Test: " + e.getMessage());
        }
    }

    @Test(description = "Test Automation: TC-005-Login with Invalid credentials")
    public void TC5() throws InterruptedException {
        // Logout first if
        WebElement logOut = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[1]/div[2]/button"));
        logOut.click();

        boolean loginResult = login("mznaat188@gmail.com", "WrongPassord");

        if (!loginResult) {
            test.pass("Login failed as expected with invalid credentials.");
            System.out.println("Negative login test passed");
        } else {
            test.fail("Login succeeded unexpectedly with invalid credentials.");
            Assert.fail("Negative login test failed: login succeeded with wrong password.");
        }
    }

}
