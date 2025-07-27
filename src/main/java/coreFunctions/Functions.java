package coreFunctions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class Functions
{




    public void click(WebDriver driver , By locator , String name )throws Exception{

        try {

            WebElement input = driver.findElement(locator);
            input.click();
            Thread.sleep(5000);

        } catch (Exception e) {
            throw new RuntimeException("click on button  ("+name+")  " +e.getMessage());
        }



    }


    public  void  sendKeys(WebDriver driver , By locator,String value,String name) throws Exception{



        try {

            WebElement element1 = driver.findElement(locator);
            element1.sendKeys(value);
            Thread.sleep(5000);
            System.out.println("Passing ");

        } catch (Exception e) {
            throw new RuntimeException("Click on button ("+name+")  "+e.getMessage());
        }
    }









}
