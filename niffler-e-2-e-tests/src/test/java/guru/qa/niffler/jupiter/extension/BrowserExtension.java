package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.utilities.Driver;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class BrowserExtension implements TestExecutionExceptionHandler,
        AfterEachCallback,
        LifecycleMethodExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenShot();
        throw throwable;
    }

    private void doScreenShot() {
        if (Driver.getDriver() != null) {
            Allure.addAttachment(
                    "Screenshot of the test end",
                    new ByteArrayInputStream(
                            Objects.requireNonNull(
                                    ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES)
                            )
                    )
            );
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (Driver.getDriver() != null) {
            Driver.closeDriver();
        }
    }
}

