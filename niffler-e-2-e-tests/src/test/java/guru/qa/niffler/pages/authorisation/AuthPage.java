package guru.qa.niffler.pages.authorisation;

import guru.qa.niffler.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AuthPage {
    public AuthPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//a[.='Login']")
    private WebElement loginBtn;

    @FindBy(xpath = "//a[.='Register']")
    private WebElement registerBtn;

    public LoginPage loginClick() {
        loginBtn.click();
        return new LoginPage();
    }

    public void registerClick() {
        registerBtn.click();
    }
}
