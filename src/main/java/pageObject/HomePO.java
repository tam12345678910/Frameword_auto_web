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
		hoverToElement(driver, HomePUI.HOVER_SERVICES);
		waitForAllElementVisible(driver, HomePUI.THANH_TOAN_HOA_DON_LINK);
		clickToElement(driver, HomePUI.THANH_TOAN_HOA_DON_LINK);
		return new Services(driver);
	}

}
