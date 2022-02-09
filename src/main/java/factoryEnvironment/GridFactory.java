package factoryEnvironment;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import factoryBrowser.BrowserList;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.Architecture;

public class GridFactory implements EnvironmentFactory {
	private WebDriver driver;
	private String browserName;
	private String ipAddress;
	private String portNumber;

	public GridFactory(String browserName, String ipAddress, String portNumber) {
		this.browserName = browserName;
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
	}

	@Override
	public WebDriver createDriver() {
		BrowserList browser = BrowserList.valueOf(browserName.toUpperCase());
		DesiredCapabilities capability = null;

		if (browser == BrowserList.FIREFOX) {
			WebDriverManager.firefoxdriver().setup();
			capability = DesiredCapabilities.firefox();
			capability.setBrowserName("firefox");
			capability.setPlatform(Platform.ANY);

			FirefoxOptions options = new FirefoxOptions();
			options.merge(capability);

		} else if (browser == BrowserList.CHROME) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			capability = DesiredCapabilities.chrome();
			capability.setBrowserName("chrome");
			capability.setPlatform(Platform.ANY);
			options.merge(capability);
		} else if (browser == BrowserList.OPERA) {
			WebDriverManager.operadriver().setup();
			driver = new OperaDriver();
		} else if (browser == BrowserList.EDGE_CHROMIUM) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browser == BrowserList.COC_COC) {
			WebDriverManager.chromedriver().driverVersion("91.0.4472.101").setup();
			ChromeOptions options = new ChromeOptions();
			options.setBinary("C:\\Program Files (x86)\\CocCoc\\Browser\\Application\\browser.exe");
			driver = new ChromeDriver(options);
		} else if (browser == BrowserList.EDGE_CHROMIUM) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browser == BrowserList.EDGE_LEGACY) {
			driver = new EdgeDriver();
		} else if (browser == BrowserList.IE) {
			WebDriverManager.iedriver().architecture(Architecture.X64).driverVersion("3.141.59").setup();
			driver = new InternetExplorerDriver();
		} else if (browser == BrowserList.SAFARI) {
			driver = new SafariDriver();
		} else if (browser == BrowserList.H_CHROME) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.setHeadless(true);
			options.addArguments("window-size=1920x1080");
			driver = new ChromeDriver(options);
		} else if (browser == BrowserList.H_FIREFOX) {
			WebDriverManager.firefoxdriver().setup();
			FirefoxOptions options = new FirefoxOptions();
			options.setHeadless(true);
			options.addArguments("window-size=1920x1080");
			driver = new FirefoxDriver(options);
		} else {
			throw new RuntimeException("Please enter correct browser name!");
		}

		try {
			driver = new RemoteWebDriver(new URL(String.format("http://%s:%s/wd/hub", ipAddress, portNumber)), capability);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return driver;
	}
}
