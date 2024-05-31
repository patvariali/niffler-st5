package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.UserRepositoryJdbc;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.user.UserJson;

public class CreateUserJdbcExtension extends CreateUserExtension {
    private final UserRepositoryJdbc userRepositoryJdbc = new UserRepositoryJdbc();

    @Override
    protected UserJson createUser(UserJson user) {




        UserAuthEntity authEntity = userRepositoryJdbc.createUserInAuth(UserAuthEntity.fromUserJson(user));

        UserEntity userEntity = new UserEntity();
        userEntity.setCurrency(user.currency());
        userEntity.setUsername(user.username());

        userEntity = userRepositoryJdbc.createUserInUserData(userEntity);

        return UserJson.simpleUser(authEntity.getUsername(), authEntity.getPassword(), authEntity.getId(), userEntity.getId());


    }
}
