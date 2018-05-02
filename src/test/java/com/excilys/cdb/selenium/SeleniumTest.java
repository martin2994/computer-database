package com.excilys.cdb.selenium;

import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeleniumTest {
    private WebDriver driver;

    /**
     */
    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.gecko.driver", "/home/martin/softwares/geckodriver");
        driver = new FirefoxDriver();
    }

    /**
     */
    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    /**
     */
    @Test
    public void verifySearchButton() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/cdb/dashboard");
        WebElement text = driver.findElement(By.id("addComputer"));
        assertTrue(text.getText().equals("Add Computer"));
    }

}
