package admin;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import commons.BaseTest;
import commons.GlobalConstants;
import pageObject.HomePO;
import pageObject.LoginPO;
import pageObject.PageGeneratorManager;
import pageObject.Services;
import pageUIs.ServicesPUI;

public class demo extends BaseTest {
	
	WebDriver driver;
	LoginPO loginPO;
	HomePO homePO;
	Services adminPO;

	@Parameters({ "browser", "url" })
	@BeforeClass(alwaysRun = true)
	public void beforeClass(String browserName, String appUrl) {
		driver = getBrowserDriver(browserName, appUrl);
		log.info("Pre-Condition - Step 02: Login with Admin role");
		loginPO = PageGeneratorManager.getLoginPage(driver);
		homePO = loginPO.loginToSystem(driver, "Admin", "admin123");
	}
	
	@Test(groups = "smoke")
	public void TC_01() {
		log.info("HomePage - Step 01: getHomePage");
		homePO = PageGeneratorManager.getHomePage(driver);
		
		log.info("HomePage - Step 01: Click to Register Link");
		Services servicesPO = homePO.navigateAdminPage();
		
		List<String> headerName = GlobalConstants.getGlobalConstants().getADMIN_CLOUNM();
		
		List<String> expectedHeaderName = servicesPO.getAllHeaderNames(driver, ServicesPUI.TABLE_ADMIN);
		
		log.info("HomePage - Step 01: Click to Register Link");
		verifyEquals(headerName, expectedHeaderName);
		
	}
	
	@Test(groups = "regresstion")
	public void TC_02() {
		log.info("HomePage - Step 01: getHomePage");
		homePO = PageGeneratorManager.getHomePage(driver);
		
		log.info("HomePage - Step 01: Click to Register Link");
		Services servicesPO = homePO.navigateAdminPage();
		
		List<String> headerName = GlobalConstants.getGlobalConstants().getADMIN_CLOUNM();
		
		List<String> expectedHeaderName = servicesPO.getAllHeaderNames(driver, ServicesPUI.TABLE_ADMIN);
		
		List<WebElement> lstTest = servicesPO.getAllRows(driver, ServicesPUI.TABLE_ADMIN);
		
		String demo = lstTest.get(2).getText();
		WebElement test = servicesPO.getRowByIndex(driver, 3, ServicesPUI.TABLE_ADMIN);
//		
		String demotest = test.getText();
		
		List<WebElement> test1 = servicesPO.getAllColumnsInRow(driver, 3, ServicesPUI.TABLE_ADMIN);
		String demo999 = test1.get(6).getText();
		
		WebElement test3 = servicesPO.getCell(driver, 2, 3, ServicesPUI.TABLE_ADMIN);
		String demo123 = test3.getText();
		List<String> getAllRow = servicesPO.getAllRowValueByHeaderName(driver, "Employee Name", ServicesPUI.TABLE_ADMIN);
		
		String test14 = servicesPO.getCellValueByHeaderName(driver, 3, "Employee Name", ServicesPUI.TABLE_ADMIN);
		log.info("HomePage - Step 01: Click to Register Link");
		verifyEquals(headerName, expectedHeaderName);
		
	}
	
	@Test(groups = "smoke")
	public void TC_03() {
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
