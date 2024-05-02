package guru.qa.niffler.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import java.time.Duration;

public class Driver {

    private Driver(){}

    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver getDriver(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*", "--window-size=1920,1080", "--disable-gpu");
        if(driverPool.get() == null){

            String browserType="";


            if(System.getProperty("browser")!=null){
                browserType=System.getProperty("browser");
            }else {
                browserType = ConfigurationReader.getProperty("browser");
            }

            switch (browserType){

                case "chrome":
                    driverPool.set(new ChromeDriver(chromeOptions));
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
                case "firefox":
                    driverPool.set(new FirefoxDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
            }

        }

        return driverPool.get();

    }

    public static void closeDriver(){
        if (driverPool.get()!=null){

            driverPool.get().quit();

            driverPool.remove();
        }
    }

}
