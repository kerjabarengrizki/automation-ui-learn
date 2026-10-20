package tests;

import core.BaseTest;
import core.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import saucedemo.LoginPage;

public class LoginTests extends BaseTest {

    private static final Logger logger = LogManager.getLogger(LoginTests.class);

    @Test(priority = 1, groups = {"smoke"}, description = "Test successful login")
    public void testSuccessLogin() {
        logger.info("Memulai test login dengan credential standard user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        logger.info("User login menggunakan credential standard user");
        loginPage.login(config.getProperty("standardUser"), config.getProperty("password"));

        logger.info("Verify user sukses login dan melihat halaman Products");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the Products page after logging in with valid credentials");

    }

    @Test(priority = 2, groups = {"smoke"}, description = "Test failed login locked out user")
    public void testLockedOutUser() {
        logger.info("Memulai test login dengan credential locked out user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        logger.info("User login menggunakan credential locked out user");
        loginPage.login(config.getProperty("lockedOutUser"), config.getProperty("password"));

        logger.info("Verify user melihat error message tampil");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "User expected melihat error message tampil");

    }
}
