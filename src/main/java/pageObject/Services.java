package pageObject;

import java.util.List;

import org.openqa.selenium.WebDriver;

import commons.BasePage;
import pageUIs.ServicesPUI;

public class Services extends BasePage {

	WebDriver driver;
	
	public String headerStatus = "Employee Name";
	
	public Services(WebDriver driver) {
		this.driver = driver;
	}
	
	public List<String> getListStatus() {
		
		List<String> test = getAllRowValueByHeaderName(driver, headerStatus, ServicesPUI.TABLE_ADMIN);
		return test;
		
	}

	public String actualMess() {
		waitForElementVisible(driver, ServicesPUI.THANH_TOAN_HOA_DON_DISPLAY);
		return getElementText(driver, ServicesPUI.THANH_TOAN_HOA_DON_DISPLAY);
	}
}
