package guru.qa.niffler.data.entity;

import guru.qa.niffler.model.CurrencyValues;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

@Getter
@Setter

public class UserEntity implements Serializable {
    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private byte[] photo;
    private byte[] photoSmall;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", currency=" + currency +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", photo=" + Arrays.toString(photo) +
                ", photoSmall=" + Arrays.toString(photoSmall) +
                '}';
    }
}