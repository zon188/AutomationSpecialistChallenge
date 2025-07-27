package runner;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.security.Key;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {


        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("Hello and welcome!");



       // System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");  //Old Way for setup chromdriver



        WebDriverManager.chromedriver().setup();//ربط اوتماتيكلي او يحمله

        WebDriver driver = new ChromeDriver(); // object for use chrome


        driver.get("https://www.google.com/"); //get url any one
        Thread.sleep(5000); // To control speed when Aoutamion

        driver.manage().window().maximize(); //To manage window max or min ...
        Thread.sleep(5000);

        WebElement search = driver.findElement(By.xpath("//*[@id=\"prompt-textarea\"]/p"));// Find element in website by copy xpath
        Thread.sleep(5000);

        search.sendKeys("cats"); // by using object search element send word you want
        Thread.sleep(5000);

        search.sendKeys(Keys.ENTER); // press enter to search
        Thread.sleep(5000);


driver.quit(); // by using driver object quite or close window

        System.out.println("Googel Opened by new code"); //

    }
}