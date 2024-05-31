package guru.qa.niffler.test;

import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.*;
import guru.qa.niffler.jupiter.annotation.TestUser;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;
import guru.qa.niffler.pages.authorisation.AuthPage;
import guru.qa.niffler.utilities.Driver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
        UserRepository userRepository = new UserRepositoryJdbc();
        SpendRepository spendRepository = new SpendRepositorySprintJdbc();

    @TestUser
    @Test
    void doLogin(UserJson userJson) {
        Objects.requireNonNull(Driver.open("http://127.0.0.1:3000/main", AuthPage.class))
                .loginClick()
                .doLogin(userJson.username(), userJson.testData().password());


        Optional<UserEntity> userEntity1 = userRepository.findUserInUserDataById(userJson.testData().userDataTableId());
        List<SpendEntity> returnedList = spendRepository.findAllByUsername("ali009");
        assertTrue(returnedList.isEmpty());

    }
}
