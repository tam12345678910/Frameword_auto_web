package factoryBrowser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.Architecture;

public class IEDriverManager implements BrowserFactory {

	@Override
	public WebDriver getBrowserDriver() {
		if (!IS_OS_WINDOWS) {
			throw new BrowserNotSupportedException("IE is not supported on " + System.getProperty("os.name"));
		}

		WebDriverManager.iedriver().architecture(Architecture.X32).setup();
		InternetExplorerOptions options = new InternetExplorerOptions();
		options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, "true");
		options.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, "true");
		options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, "false");
		options.setCapability("ignoreProtectedModeSettings", "true");
		options.setCapability("ignoreZoomSetting", "true");
		options.setCapability("requireWindowFocus", "true");
		options.setCapability("enableElementCacheCleanup", "true");
		return new InternetExplorerDriver(options);
	}

}
