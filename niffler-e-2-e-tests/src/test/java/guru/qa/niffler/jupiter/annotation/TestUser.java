package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.CategoryJdbcExtension;
import guru.qa.niffler.jupiter.extension.CreateUserJdbcExtension;
import guru.qa.niffler.jupiter.extension.SpendJdbcExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({
        CreateUserJdbcExtension.class,
})
public @interface TestUser {
}
