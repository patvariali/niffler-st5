package guru.qa.niffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface User {
    UserType userType();
    enum UserType {
        INVITATION_SENT, INVITATION_RECEIVED, WITH_FRIENDS, COMMON
    }
}
