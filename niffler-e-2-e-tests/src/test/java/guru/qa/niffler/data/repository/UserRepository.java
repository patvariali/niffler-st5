package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.data.entity.SpendEntity;
import guru.qa.niffler.data.entity.UserAuthEntity;
import guru.qa.niffler.data.entity.UserEntity;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    static UserRepository getInstance() {
        if ("sjdbc".equals(System.getProperty("repo"))){
            return new UserRepositorySprintJdbc();
        }
        if ("hibernate".equals(System.getProperty("repo"))){
            return new UserRepositoryHibernate();
        }
        return new UserRepositoryJdbc();
    }


    UserAuthEntity createUserInAuth(UserAuthEntity user);

    UserAuthEntity updateUserInAuth(UserAuthEntity user);

    UserEntity createUserInUserData(UserEntity user);

    UserEntity updateUserInUserdata(UserEntity user);

    Optional<UserEntity> findUserInUserDataById(UUID id);
}
