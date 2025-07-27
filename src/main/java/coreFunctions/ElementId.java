package coreFunctions;

import org.openqa.selenium.By;

public class ElementId
{

    //crate Varibale type By for using in testcase
    //pageName-locator-locator type
    //homePage_inputPage_inputField



    //input page
    public static By homePage_inputButton = By.xpath("//*[@id=\"content\"]/ul/li[27]/a");
    public static By homePage_inputPage_inputField = By.xpath("//*[@id=\"content\"]/div/div/div/input");


    //Form Authentication

//    public static By getHomePage_Form_AuthenticationButton = By.xpath("//*[@id=\"content\"]/ul/li[21]/a");
//    public static By getHomePage_Form_AuthenticationPage_usernameField = By.id("username");
//    public static By getHomePage_Form_AuthenticationPage_passwordField = By.id("password");
//    public static By getHomePage_Form_AuthenticationPage_LoginButton = By.xpath("//*[@id=\"login\"]/button/i");
//    public static By getHomePage_Form_AuthenticationPage_msg = By.id("flash");




    public  static By getHomePage_SearchQuotes = By.xpath("//*[@id=\"quote-form\"]/div[5]/button");






}
