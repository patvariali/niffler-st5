package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UserQueueExtension;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.pages.authorisation.AuthPage;
import guru.qa.niffler.utilities.Driver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.annotation.User.UserType.*;

@WebTest
@ExtendWith(UserQueueExtension.class)
public class UsersQueueExampleTest {


    @Test
    void checkHaveFiends(@User(userType = WITH_FRIENDS) UserJson user,
                         @User(userType = WITH_FRIENDS) UserJson anotherUser) {
        Driver.open("http://127.0.0.1:3000/main", AuthPage.class)
                .loginClick()
                .doLogin(user.username(), user.testData().password())
                .goToAllPeople()
                .checkHasFriendByName(anotherUser.username());
    }


    @Test
    void checkInvitationSent(@User(userType = INVITATION_SENT) UserJson user) {

        Driver.open("http://127.0.0.1:3000/main", AuthPage.class)
                .loginClick()
                .doLogin(user.username(), user.testData().password())
                .goToAllPeople()
                .checkInviteSentByName("ali3");
    }

    @Test
    void checkInvitationReceived(@User(userType = INVITATION_RECEIVED) UserJson user,
                                 @User(userType = INVITATION_SENT) UserJson anotherUser) {

        Driver.open("http://127.0.0.1:3000/main", AuthPage.class)
                .loginClick()
                .doLogin(user.username(), user.testData().password())
                .goToAllPeople()
                .checkInviteReceivedByName(anotherUser.username());
    }

}
