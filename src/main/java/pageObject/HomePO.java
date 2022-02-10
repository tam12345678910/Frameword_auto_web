package pageObject;

import org.openqa.selenium.WebDriver;

import commons.BasePage;
import pageUIs.HomePUI;

public class HomePO extends BasePage {

	WebDriver driver;

	public HomePO(WebDriver driver) {
		this.driver = driver;
	}

	public Services navigateAdminPage() {
		waitForElementClickable(driver, HomePUI.ADMIN_PAGE);
		clickToElement(driver, HomePUI.ADMIN_PAGE);
		return PageGeneratorManager.getAdminPage(driver);
	}

}
