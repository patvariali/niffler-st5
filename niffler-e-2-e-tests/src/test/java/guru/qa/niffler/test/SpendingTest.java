package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendExtension;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.authorisation.AuthPage;
import guru.qa.niffler.pages.authorisation.LoginPage;
import guru.qa.niffler.utilities.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;

@ExtendWith(CategoryExtension.class)
@ExtendWith(SpendExtension.class)
public class SpendingTest {

    private final AuthPage authPage = new AuthPage();
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();

    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {

        Driver.getDriver().get("http://127.0.0.1:3000/main");
        authPage.loginClick();
        loginPage.sendUsername("ali1")
                .sendPassword("Misha280620")
                .clickSignIn();

    }

    @GenerateCategory(
            username = "ali1",
            category = "Обучение1"
    )
    @GenerateSpend(
            username = "ali1",
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB,
            category = "Обучение1"
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {

        mainPage.scrollToButtom();
        WebElement requiredRow = mainPage.findSpendByDescription(spendJson.description());
        mainPage.chooseRow(requiredRow)
                .deleteSpend()
                .checkIfDeleted();

        Driver.closeDriver();

    }
}
