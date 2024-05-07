package guru.qa.niffler.pages;

import guru.qa.niffler.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainPage {

    public MainPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    private final WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5000));

    @FindBy(xpath = "//h1[@class='header__title']")
    private WebElement header;

    @FindBy(xpath = "//a[@href='/profile']")
    private WebElement myProfile;

    @FindBy(xpath = "//a[@href='/people']")
    private WebElement allPeople;

    @FindBy(xpath = "//div[@data-tooltip-id='logout']/button")
    private WebElement logoutBtn;

    @FindBy(xpath = "//section[@class='spendings__bulk-actions']/button")
    private WebElement deleteBtn;

    @FindBy(xpath = "//div[@class='Toastify__toast-body']//div[.='Spendings deleted']")
    private WebElement spendDeleteAlert;

    @FindAll({
            @FindBy(xpath = "//table[@class='table spendings-table']/tbody//tr")
    })
    private List<WebElement> spendingRows;

    public WebElement findSpendByDescription(String description) {

        for (WebElement spendingRow : spendingRows) {

            List<WebElement> allColumnsInRow = spendingRow.findElements(By.xpath("./td//span"));

            for (WebElement column : allColumnsInRow) {
                if (column.getText().equals(description))
                    return spendingRow;
            }

        }

        return null;
    }

    public MainPage chooseRow(String description) {
        findSpendByDescription(description)
                .findElement(By.xpath("./td")).click();
        return this;
    }

    public MainPage deleteSpend() {
        deleteBtn.click();
        return this;
    }

    public void checkIfDeleted() {
        wait.until(ExpectedConditions.visibilityOf(spendDeleteAlert));
        assertTrue(spendDeleteAlert.isDisplayed());
    }

    public MainPage scrollToButtom() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();

        header.click();

        while (!(Boolean) js.executeScript("return document.documentElement.scrollHeight <= document.documentElement.scrollTop + window.innerHeight")) {
            js.executeScript("window.scrollTo(0, document.documentElement.scrollHeight)");
            wait.until(ExpectedConditions.visibilityOfAllElements(spendingRows));
        }
        return this;
    }

    public AllPeoplePage goToAllPeople() {
        allPeople.click();
        return new AllPeoplePage();
    }
}
