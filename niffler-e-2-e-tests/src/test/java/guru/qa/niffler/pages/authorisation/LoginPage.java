package guru.qa.niffler.pages.authorisation;

import guru.qa.niffler.pages.MainPage;
import guru.qa.niffler.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public LoginPage(){
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//input[@name='username']")
    private WebElement usernameInput;

    @FindBy(xpath = "//input[@name='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement signInBtn;

    public LoginPage sendUsername(String username) {
        usernameInput.sendKeys(username);
        return this;
    }

    public LoginPage sendPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public void clickSignIn() {
        signInBtn.click();
    }

    public MainPage doLogin(String username, String password) {
        sendUsername(username).sendPassword(password).clickSignIn();
        return new MainPage();
    }
}
