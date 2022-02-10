package pageObject;

import org.openqa.selenium.WebDriver;

import commons.BasePage;
import pageUIs.BasePageUI;
import pageUIs.LoginPUI;

public class LoginPO extends BasePage {

	WebDriver driver;

	public LoginPO(WebDriver driver) {
		this.driver = driver;
	}

	public void enterToUserName(String userName) {
		waitForAllElementVisible(driver, LoginPUI.USERNAME_TEXT_BOX);		
		senkeysToElement(driver, LoginPUI.USERNAME_TEXT_BOX, userName);
	}

	public void enterToPassWord(String pass) {
		waitForAllElementVisible(driver, LoginPUI.PASS_TEXT_BOX);		
		senkeysToElement(driver, LoginPUI.PASS_TEXT_BOX, pass);		
	}

	public HomePO clickToLoginButton() {
			waitForElementClickable(driver, LoginPUI.LOGIN_BUTTON);
			clickToElement(driver, LoginPUI.LOGIN_BUTTON);
			return PageGeneratorManager.getHomePage(driver);
	}

	public HomePO loginToSystem(WebDriver driver, String userName, String password) {
		waitForElementVisible(driver, BasePageUI.USER_LOGIN_TEXTBOX);
		sendkeyToElement(driver, BasePageUI.USER_LOGIN_TEXTBOX, userName);
		sendkeyToElement(driver, BasePageUI.PASSWORD_LOGIN_TEXTBOX, password);
		waitForElementClickable(driver, BasePageUI.LOGIN_BUTTON);
		clickToElement(driver, BasePageUI.LOGIN_BUTTON);

		if (driver.toString().contains("internet explorer")) {
			sleepInSecond(3);
		}

		return PageGeneratorManager.getHomePage(driver);
	}
}

