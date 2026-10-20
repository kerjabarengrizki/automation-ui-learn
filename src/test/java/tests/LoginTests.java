package tests;

import core.BaseTest;
import core.DriverManager;
import core.Log;
import org.testng.Assert;
import org.testng.annotations.Test;
import saucedemo.LoginPage;

public class LoginTests extends BaseTest {

    @Test(priority = 1, groups = {"smoke"}, description = "Test successful login")
    public void testSuccessLogin() {
        Log.info("Memulai test login dengan credential standard user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        Log.info("User login menggunakan credential standard user");
        loginPage.login(config.getProperty("standardUser"), config.getProperty("password"));

        Log.info("Verify user sukses login dan melihat halaman Products");
        Assert.assertTrue(loginPage.isUserLoggedInSuccessfully(),
                "User should be able to see the Products page after logging in with valid credentials");

    }

    @Test(priority = 2, groups = {"smoke"}, description = "Test failed login locked out user")
    public void testLockedOutUser() {
        Log.info("Memulai test login dengan credential locked out user");
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        Log.info("User login menggunakan credential locked out user");
        loginPage.login(config.getProperty("lockedOutUser"), config.getProperty("password"));

        Log.info("Verify user melihat error message tampil");
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "User expected melihat error message tampil");

    }
}
