/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.scrap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author alessio.finamore
 */
public class SeleniumTestFF {
    
    public SeleniumTestFF() {
    }
    public String getUrl(){
        HtmlUnitDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX_24);
        driver.setJavascriptEnabled(true);
        // FirefoxDriver driver = new FirefoxDriver();

        // And now use this to visit Google
        driver.get("http://localhost:4502");
        
        // Form login
        WebDriverWait wait = new WebDriverWait(driver, 3); 
        WebElement formElement = wait.until(
           ExpectedConditions.presenceOfElementLocated(By.name("Submit"))
        );
        WebElement usernameElement     = driver.findElement(By.id("account"));
        WebElement passwordElement     = driver.findElement(By.id("passwd"));

        usernameElement.sendKeys("admin");
        passwordElement.sendKeys("admin");

        formElement.click();

        try {
            WebElement messageElement = wait.until( 
               ExpectedConditions. presenceOfElementLocated( By.id("div_live") )
            );
        }catch(Exception ex){
            System.out.println(ex.getCause() +" "+ex.getMessage());
            //System.out.println(driver.getPageSource());
        }

        return driver.getCurrentUrl(); 
        
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {
    
        for (int i = 1; i <= 16; i++) {
            System.out.println(this.getUrl().replaceFirst("sel=1","sel=" + i));
        }
    }
        
}
