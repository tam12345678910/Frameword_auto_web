package pageObject;

import org.openqa.selenium.WebDriver;

public class PageGeneratorManager {

	public static HomePO getHomePage(WebDriver driver) {
		return new HomePO(driver);
	}

	public static LoginPO getLoginPage(WebDriver driver) {
		return new LoginPO(driver);
	}
	
	public static Services getAdminPage(WebDriver driver) {
		return new Services(driver);
	}

	
}
