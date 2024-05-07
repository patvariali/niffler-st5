package guru.qa.niffler.pages;

import guru.qa.niffler.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class FriendsPage {
    public FriendsPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindAll(
            @FindBy(xpath = "//table[@class='table abstract-table']/tbody/tr")
    )
    private List<WebElement> allFriendsRows;

}
