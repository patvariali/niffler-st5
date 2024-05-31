package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.user.UserJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter

public class UserAuthEntity implements Serializable {
    private UUID id;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;

    public static UserAuthEntity fromUserJson(UserJson userJson) {
        UserAuthEntity userEntity = new UserAuthEntity();
        userEntity.setId(userJson.id());
        userEntity.setUsername(userJson.username());
        userEntity.setPassword(userJson.testData().password());
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        return userEntity;
    }
}
