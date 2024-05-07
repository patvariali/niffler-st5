package guru.qa.niffler.test;

import com.codeborne.selenide.Configuration;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.pages.authorisation.AuthPage;
import guru.qa.niffler.utilities.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingTest {

    private final MainPage mainPage = new MainPage();
    static {
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void doLogin() {
        Driver.open("http://127.0.0.1:3000/main", AuthPage.class)
                .loginClick()
                .doLogin("ali2", "12345");
    }

    @GenerateCategory(
            username = "ali2",
            category = "Обучение5"
    )
    @GenerateSpend(
            description = "QA.GURU Advanced 5",
            amount = 65000.00,
            currency = CurrencyValues.RUB
    )
    @Test
    void spendingShouldBeDeletedAfterTableAction(SpendJson spendJson) {
        mainPage.scrollToButtom()
                .chooseRow(spendJson.description())
                .deleteSpend()
                .checkIfDeleted();
    }
}
