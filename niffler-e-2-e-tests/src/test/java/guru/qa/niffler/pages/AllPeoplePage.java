package guru.qa.niffler.pages;

import guru.qa.niffler.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllPeoplePage {
    public AllPeoplePage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindAll(
            @FindBy(xpath = "//table[@class='table abstract-table']/tbody/tr")
    )
    private List<WebElement> allPeopleRows;

    private WebElement getRowByUsername(String username) {
        for (WebElement eachPerson : allPeopleRows) {
            if (eachPerson.findElement(By.xpath("./td[2]")).getText().equals(username))
                return eachPerson;
        }
        return null;
    }

    public void checkHasFriendByName(String username) {
        assertTrue(getRowByUsername(username).findElement(By.xpath(".//div[.='You are friends']")).isDisplayed());
    }

    public void checkInviteSentByName(String username) {
        assertTrue(getRowByUsername(username).findElement(By.xpath(".//div[.='Pending invitation']")).isDisplayed());
    }

    public void checkInviteReceivedByName(String username) {
        assertTrue(getRowByUsername(username).findElement(By.xpath(".//div[@data-tooltip-id='submit-invitation']")).isDisplayed());
    }

}
