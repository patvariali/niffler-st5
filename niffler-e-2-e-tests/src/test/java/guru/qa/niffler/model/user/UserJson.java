package guru.qa.niffler.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.TestData;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("photoSmall")
        String photoSmall,
        @JsonProperty("friendState")
        FriendState friendState,
        @JsonIgnore
        TestData testData)  {

        public static UserJson simpleUser (String username, String password) {
                return new UserJson(
                        null,
                        username,
                        null,
                        null,
                        CurrencyValues.EUR,
                        null,
                        null,
                        null,
                        new TestData(password, null)
                );
        }

        public static UserJson simpleUser (String username, String password, UUID id, UUID userDataTableId) {
                return new UserJson(
                        id,
                        username,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        new TestData(password, userDataTableId)
                );
        }

        @Override
        public String toString() {
                return "UserJson{" +
                        "id=" + id +
                        ", username='" + username + '\'' +
                        ", firstname='" + firstname + '\'' +
                        ", surname='" + surname + '\'' +
                        ", currency=" + currency +
                        ", photo='" + photo + '\'' +
                        ", photoSmall='" + photoSmall + '\'' +
                        ", friendState=" + friendState +
                        ", testData=" + testData +
                        '}';
        }
}
