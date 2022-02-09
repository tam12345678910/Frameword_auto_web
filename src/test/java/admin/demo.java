package admin;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import commons.BaseTest;
import pageObject.HomePO;
import pageObject.LoginPO;
import pageObject.PageGeneratorManager;
import pageObject.Services;

public class demo extends BaseTest {
	
	WebDriver driver;
	LoginPO loginPO;
	HomePO homePO;
	Services adminPO;

	@Parameters({ "browser", "url" })
	@BeforeClass(alwaysRun = true)
	public void beforeClass(String browserName, String appUrl) {
		driver = getBrowserDriver(browserName, appUrl);
	}
	
	@Test(groups = "regresstion")
	public void TC_01() {
		log.info("HomePage - Step 01: getHomePage");
		homePO = PageGeneratorManager.getHomePage(driver);
		
		log.info("HomePage - Step 01: Click to Register Link");
		Services servicesPO = homePO.navigateAdminPage();
		
		log.info("HomePage - Step 01: Click to Register Link");
		verifyEquals(servicesPO.actualMess(), "Thanh toán hóa đơn");
		
	}
	
	@Test(groups = "smoke")
	public void TC_02() {
		log.info("HomePage - Step 01: getHomePage");
		homePO = PageGeneratorManager.getHomePage(driver);
		
		log.info("HomePage - Step 01: Click to Register Link");
		Services servicesPO = homePO.navigateAdminPage();
		
		log.info("HomePage - Step 01: Click to Register Link");
		verifyEquals(servicesPO.actualMess(), "Thanh toán hóa đơn");
		
	}
	
	
	@Parameters({ "browser" })
	@AfterClass(alwaysRun = true)
	public void cleanBrowser(String browserName) {
		log.info("Post-Condition: Close browser '" + browserName + "'");
		cleanDriverInstance();
	}
}
