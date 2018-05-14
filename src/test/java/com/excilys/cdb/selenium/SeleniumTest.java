package com.excilys.cdb.selenium;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.excilys.cdb.services.Facade;

public class SeleniumTest {
    private WebDriver driver;

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Facade.class);

    /**
     * Initialise le driver Firefox.
     */
    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.gecko.driver", "/home/martin/softwares/geckodriver");
        driver = new FirefoxDriver();
    }

    /**
     * Ferme le driver Firefox.
     */
    @AfterClass
    public void afterClass() {
        driver.quit();
    }

    /**
     * Teste si le button Add du dashboard renvoie vers la page Add.
     */
    @Test
    public void verifyAddButtonDashboard() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/cdb/dashboard");
        driver.findElement(By.id("addComputer")).click();
        WebElement form = driver.findElement(By.id("computerForm"));
        assertNotNull(form);
    }

    /**
     * Teste si la recherche fonctionne.
     */
    @Test
    public void verifySearchDashboard() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/cdb/dashboard");
        WebElement searchInput = driver.findElement(By.id("searchbox"));
        searchInput.sendKeys("test");
        driver.findElement(By.id("searchsubmit")).click();
        WebElement title = driver.findElement(By.id("homeTitle"));
        assertTrue("5 Computers found".equals(title.getText()));
    }

    /**
     * Teste si l'ajout de computer avec une company fonctionne.
     */
    @Test
    public void verifyAddComputerWithCompany() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/cdb/addComputer");
        WebElement input = driver.findElement(By.name("computerName"));
        input.sendKeys("testSelenium");
        Select company = new Select(driver.findElement(By.name("companyId")));
        company.selectByIndex(1);
        driver.findElement(By.id("buttonAdd")).click();
        driver.get("http://localhost:8080/cdb/dashboard");
        WebElement title = driver.findElement(By.id("homeTitle"));
        assertTrue("13 Computers found".equals(title.getText()));
    }

    /**
     * Teste si la suppression d'un computer fonctionne.
     */
    @Test
    public void verifyDeleteComputer() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/cdb/dashboard");
        driver.findElement(By.id("editComputer")).click();
        driver.findElements(By.name("cb")).get(0).click();
        driver.findElement(By.id("trash")).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.get("http://localhost:8080/cdb/dashboard");
        WebElement title = driver.findElement(By.id("homeTitle"));
        assertTrue("12 Computers found".equals(title.getText()));
    }

    /**
     * Teste si l'edit est effectué.
     */
    @Test
    public void verifyEditComputer() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/cdb/editComputer?id=5");
        WebElement input = driver.findElement(By.name("computerName"));
        input.sendKeys("testSeleniumEdit");
        driver.findElement(By.id("buttonEdit")).click();
        assertTrue("Update done.".equals(driver.findElement(By.id("success")).getText()));
    }

}