package commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

	private Alert alert;
	private Select select;
	private Actions action;
	private WebDriverWait explicitWait;
	private JavascriptExecutor jsExecutor;
	private List<WebElement> elements;

	public static BasePage getBasePage() {

		return new BasePage();
	}

	public void openUrl(WebDriver driver, String pageUrl) {
		driver.get(pageUrl);
	}

	public void getPageTitle(WebDriver driver) {
		driver.getTitle();
	}

	public void getPageUrl(WebDriver driver) {
		driver.getCurrentUrl();
	}

	public void getPageSource(WebDriver driver) {
		driver.getPageSource();
	}
	
	public Set<Cookie> getAllCookies(WebDriver driver) {
		return driver.manage().getCookies();
	}

	public void setAllCookies(WebDriver driver, Set<Cookie> allCookies) {
		for (Cookie cookie : allCookies) {
			driver.manage().addCookie(cookie);
		}
	}

	public Alert waitForAlertPresence(WebDriver driver) {
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
		return explicitWait.until(ExpectedConditions.alertIsPresent());
	}

	public void acceptAlert(WebDriver driver) {
		alert = driver.switchTo().alert();
		alert.accept();
	}

	public void cancelAlert(WebDriver driver) {
		alert = driver.switchTo().alert();
		alert.dismiss();
	}

	public void sendkeyToAlert(WebDriver driver, String value) {
		alert = driver.switchTo().alert();
		alert.sendKeys(value);
	}

	public String getAlertText(WebDriver driver) {
		alert = driver.switchTo().alert();
		return alert.getText();
	}

	public void switchToWindowByID(WebDriver driver, String parentID) {
		Set<String> allWindowsID = driver.getWindowHandles();
		for (String windowID : allWindowsID) {
			if (!windowID.equals(parentID)) {
				driver.switchTo().window(windowID);
				break;
			}
		}
	}

	public void swithToWindowByTitle(WebDriver driver, String expectedWindowTitle) {
		Set<String> allWindowIDs = driver.getWindowHandles();
		for (String windowID : allWindowIDs) {
			driver.switchTo().window(windowID);
			String acctualWindowTitle = driver.getTitle();
			if (acctualWindowTitle.equals(expectedWindowTitle)) {
				break;
			}
		}
	}

	public void closeAllWindowExceptParent(WebDriver driver, String parentID) {
		Set<String> allWindowsID = driver.getWindowHandles();
		for (String windowID : allWindowsID) {
			if (!windowID.equals(parentID)) {
				driver.switchTo().window(windowID);
				driver.close();
				sleepInSecond(1);
			}

			if (driver.getWindowHandles().size() == 1) {
				driver.switchTo().window(parentID);
				break;
			}
		}
	}

	public void sleepInSecond(long second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void sleepInMiliSecond(long milisecond) {
		try {
			Thread.sleep(milisecond);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void backToPage(WebDriver driver) {
		driver.navigate().back();
	}

	public void refreshCurrentPage(WebDriver driver) {
		driver.navigate().refresh();
	}

	public void forwardToPage(WebDriver driver) {
		driver.navigate().forward();
	}

	public WebElement getElement(WebDriver driver, String locator) {
		return driver.findElement(getLocatorBy(locator));
	}

	public List<WebElement> getElements(WebDriver driver, String locator) {
		return driver.findElements(getLocatorBy(locator));
	}
	
	public By getLocatorBy(String locator) {
		By by = null;
		if (locator.startsWith("id=") || locator.startsWith("ID=") || locator.startsWith("Id=")) {
			by = By.id(locator.substring(3));
		} else if (locator.startsWith("class=") || locator.startsWith("CLASS=") || locator.startsWith("Class=")) {
			by = By.className(locator.substring(6));
		}  else if (locator.startsWith("name=") || locator.startsWith("NAME=") || locator.startsWith("Name=")) {
			by = By.name(locator.substring(5));
		} else if (locator.startsWith("css=") || locator.startsWith("CSS=") || locator.startsWith("Css=")) {
			by = By.cssSelector(locator.substring(4));
		} else if (locator.startsWith("xpath=") || locator.startsWith("XPATH=") || locator.startsWith("Xpath=") || locator.startsWith("XPath=")) {
			by = By.xpath(locator.substring(6));
		} else {
			throw new RuntimeException("Locator type is not supported");
		}

		return by;
	}

	public String getDynamicLocator(String locator, String... params) {
		if(locator.startsWith("xpath=") || locator.startsWith("XPATH=") || locator.startsWith("Xpath=") || locator.startsWith("XPath=")) {
			locator = String.format(locator, (Object[]) params);
		}
		return locator;
	}

	public void clickToElement(WebDriver driver, String locator) {
		waitForElementClickable(driver, locator).click();
	}

	public void clickToElement(WebDriver driver, String locator, String... values) {
		waitForElementClickable(driver, locator).click();
	}
	
	public void hightlightElement(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		WebElement element = getElement(driver, locator);
		String originalStyle = element.getAttribute("style");
		String hightlightStyle = "border: 2px solid red; border-style: dashed;";
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", hightlightStyle);
		// sleepInSecond(1);
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
	}

	public void sendKeyBoard(WebDriver driver, String locator, Keys key) {
		action.keyDown(key).perform();
		getElement(driver, locator);
	}

	public void senkeysToElement(WebDriver driver, String locator, String value) {
		getElement(driver, locator).clear();
		getElement(driver, locator).sendKeys(value);
	}

	public void sendkeyToElement(WebDriver driver, String locator, String value, String... params) {
		locator = getDynamicLocator(locator, params);
		hightlightElement(driver, locator);
		getElement(driver, locator).clear();
		getElement(driver, locator).sendKeys(value);
	}

	public int getElementSize(WebDriver driver, String locator) {
		return getElements(driver, locator).size();
	}

	public int getElementSize(WebDriver driver, String locator, String... params) {
		locator = getDynamicLocator(locator, params);
		return getElements(driver, locator).size();
	}

	public void selectDropdownByText(WebDriver driver, String locator, String itemText, String... params) {
		locator = getDynamicLocator(locator, params);
		select = new Select(getElement(driver, locator));
		select.selectByVisibleText(itemText);
	}

	public String getSelectedItemDropdown(WebDriver driver, String locator) {
		select = new Select(getElement(driver, locator));
		return select.getFirstSelectedOption().getText();
	}

	public String getSelectedItemDropdown(WebDriver driver, String locator, String... params) {
		locator = getDynamicLocator(locator, params);
		select = new Select(getElement(driver, locator));
		return select.getFirstSelectedOption().getText();
	}

	public boolean isDropdownMultiple(WebDriver driver, String locator) {
		select = new Select(getElement(driver, locator));
		return select.isMultiple();
	}

	public void selectItemInCustomDropdown(WebDriver driver, String parentLocator, String childItemLocator, String expectedItem) {
		getElement(driver, parentLocator).click();
		sleepInSecond(1);

		explicitWait = new WebDriverWait(driver, 5);
		List<WebElement> allItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getLocatorBy(childItemLocator)));

		for (WebElement item : allItems) {
			if (item.getText().trim().equals(expectedItem)) {
				jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
				sleepInSecond(1);

				item.click();
				sleepInSecond(1);
				break;
			}
		}
	}

	public String getElementAttributeValue(WebDriver driver, String locator, String attributeName) {
		return getElement(driver, locator).getAttribute(attributeName);

	}

	public String getElementAttributeValue(WebDriver driver, String locator, String attributeName, String... values) {
		return getElement(driver, getDynamicLocator(locator, values)).getAttribute(attributeName);

	}

	public String getElementAttribute(WebDriver driver, String locator, String attributeName) {
		return getElement(driver, locator).getAttribute(attributeName);
	}

	public String getElementAttribute(WebDriver driver, String locator, String attributeName, String... params) {
		return getElement(driver, getDynamicLocator(locator, params)).getAttribute(attributeName);
	}

	public String getElementText(WebDriver driver, String locator) {
		return getElement(driver, locator).getText().trim();
	}

	public String getElementText(WebDriver driver, String locator, String... params) {
		return getElement(driver, getDynamicLocator(locator, params)).getText().trim();
	}

	public int countElementSize(WebDriver driver, String locator) {
		return getElements(driver, locator).size();
	}

	public void checkToCheckboxOrRadio(WebDriver driver, String locator) {
		if (!isElementSelected(driver, locator)) {
			getElement(driver, locator).click();
		}
	}

	public void checkToCheckboxOrRadio(WebDriver driver, String locator, String... params) {
		locator = getDynamicLocator(locator, params);
		hightlightElement(driver, locator);
		if (!isElementSelected(driver, locator)) {
			if (driver.toString().contains("internet explorer")) {
				clickToElementByJS(driver, locator);
			} else {
				getElement(driver, locator).click();
			}
		}
	}

	public void uncheckToCheckbox(WebDriver driver, String locator) {
		hightlightElement(driver, locator);
		if (isElementSelected(driver, locator)) {
			if (driver.toString().contains("internet explorer")) {
				clickToElementByJS(driver, locator);
			} else {
				getElement(driver, locator).click();
			}
		}
	}

	public boolean isElementDisplayed(WebDriver driver, String locator) {
		hightlightElement(driver, locator);
		try {
			// Displayed: Visible on UI + In DOM
			// Undisplayed: Invisible on UI + In DOM
			return getElement(driver, locator).isDisplayed();
		} catch (Exception e) {
			// Undisplayed: Invisible on UI + Not in DOM
			return false;
		}
	}
	
	public void overideImpliciWait(WebDriver driver, long timeInSecond) {
		driver.manage().timeouts().implicitlyWait(timeInSecond, TimeUnit.SECONDS);
	}
	
	public boolean isElementDisplayed(WebDriver driver, String locator, String... params) {
		return getElement(driver, getDynamicLocator(locator, params)).isDisplayed();
	}

	public boolean isElementEnabled(WebDriver driver, String locator) {
		return getElement(driver, locator).isEnabled();
	}

	public boolean isElementSelected(WebDriver driver, String locator) {
		return getElement(driver, locator).isSelected();
	}

	public boolean isElementSelected(WebDriver driver, String locator, String... values) {
		return getElement(driver, getDynamicLocator(locator, values)).isSelected();
	}

	public WebDriver switchToIframeByElement(WebDriver driver, String locator) {
		return driver.switchTo().frame(getElement(driver, locator));
	}

	public WebDriver switchToDefaultContent(WebDriver driver) {
		return driver.switchTo().defaultContent();
	}

	public void hoverToElement(WebDriver driver, String locator) {
		action = new Actions(driver);
		action.moveToElement(getElement(driver, locator)).perform();
	}

	public void doubleClickToElement(WebDriver driver, String locator) {
		action = new Actions(driver);
		action.doubleClick(getElement(driver, locator)).perform();
	}

	public void rightClickToElement(WebDriver driver, String locator) {
		action = new Actions(driver);
		action.contextClick(getElement(driver, locator)).perform();

	}

	public void dragAndDropElement(WebDriver driver, String sourceLocator, String targetLocator) {
		action = new Actions(driver);
		action.dragAndDrop(getElement(driver, sourceLocator), getElement(driver, targetLocator)).perform();
	}

	public void pressKeyToElement(WebDriver driver, String locator, Keys key) {
		action = new Actions(driver);
		action.sendKeys(getElement(driver, locator), key).perform();

	}
	public void hightlightElement(WebDriver driver, String locator, String... params) {
		jsExecutor = (JavascriptExecutor) driver;
		WebElement element = getElement(driver, getDynamicLocator(locator, params));
		String originalStyle = element.getAttribute("style");
		String hightlightStyle = "border: 2px solid red; border-style: dashed;";
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", hightlightStyle);
		sleepInSecond(1);
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
	}

	public Object executeForBrowser(WebDriver driver, String javaScript) {
		jsExecutor = (JavascriptExecutor) driver;
		return jsExecutor.executeScript(javaScript);
	}

	public String getInnerText(WebDriver driver) {
		jsExecutor = (JavascriptExecutor) driver;
		return (String) jsExecutor.executeScript("return document.documentElement.innerText;");
	}

	public boolean areExpectedTextInInnerText(WebDriver driver, String textExpected) {
		jsExecutor = (JavascriptExecutor) driver;
		String textActual = (String) jsExecutor
				.executeScript("return document.documentElement.innerText.match('" + textExpected + "')[0]");
		return textActual.equals(textExpected);
	}

	public void scrollToBottomPage(WebDriver driver) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
	}

	public void navigateToUrlByJS(WebDriver driver, String url) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("window.location = '" + url + "'");
	}

	public void highlightElement(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		WebElement element = getElement(driver, locator);
		String originalStyle = element.getAttribute("style");
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
				"border: 2px solid red; border-style: dashed;");
		sleepInSecond(1);
		jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style",
				originalStyle);
	}

	public void clickToElementByJS(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].click();", getElement(driver, locator));
	}

	public void scrollToElement(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", getElement(driver, locator));
		sleepInSecond(2);
	}

	public void sendkeyToElementByJS(WebDriver driver, String locator, String value) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].setAttribute('value', '" + value + "')", getElement(driver, locator));
	}

	public void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
		jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].removeAttribute('" + attributeRemove + "');",
				getElement(driver, locator));
	}

	public boolean areJQueryAndJSLoadedSuccess(WebDriver driver) {
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
		jsExecutor = (JavascriptExecutor) driver;

		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long) jsExecutor.executeScript("return jQuery.active") == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return jsExecutor.executeScript("return document.readyState").toString().equals("complete");
			}
		};

		return explicitWait.until(jQueryLoad) && explicitWait.until(jsLoad);
	}

	public String getElementValidationMessage(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", getElement(driver, locator));
	}

	public boolean isImageLoaded(WebDriver driver, String locator) {
		jsExecutor = (JavascriptExecutor) driver;
		boolean status = (boolean) jsExecutor.executeScript(
				"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
				getElement(driver, locator));
		if (status) {
			return true;
		} else {
			return false;
		}
	}

	public void waitForElementVisible(WebDriver driver, String locator) {
		sleepInSecond(GlobalConstants.getGlobalConstants().getShortTimeout());
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getLocatorBy(locator)));

	}

	public void waitForElementVisible(WebDriver driver, String locator, String... values) {
		sleepInSecond(GlobalConstants.getGlobalConstants().getShortTimeout());
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
		explicitWait
				.until(ExpectedConditions.visibilityOfElementLocated(getLocatorBy(getDynamicLocator(locator, values))));

	}

	public void waitForAllElementVisible(WebDriver driver, String locator) {
		sleepInSecond(GlobalConstants.getGlobalConstants().getShortTimeout());
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getLocatorBy(locator)));

	}

	public void waitForAllElementVisible(WebDriver driver, String locator, String... values) {
		sleepInSecond(GlobalConstants.getGlobalConstants().getShortTimeout());
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
		explicitWait.until(
				ExpectedConditions.visibilityOfAllElementsLocatedBy(getLocatorBy(getDynamicLocator(locator, values))));

	}

	public WebElement waitForElementClickable(WebDriver driver, String locator) {
		sleepInSecond(GlobalConstants.getGlobalConstants().getShortTimeout());
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
		return explicitWait.until(ExpectedConditions.elementToBeClickable(getLocatorBy(locator)));

	}

	public void waitForElementClickable(WebDriver driver, String locator, String... values) {
		sleepInSecond(GlobalConstants.getGlobalConstants().getShortTimeout());
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
		explicitWait.until(ExpectedConditions.elementToBeClickable(getLocatorBy(getDynamicLocator(locator, values))));

	}

	public void waitForElementInvisible(WebDriver driver, String locator) {
		sleepInSecond(GlobalConstants.getGlobalConstants().getShortTimeout());
		explicitWait = new WebDriverWait(driver, GlobalConstants.getGlobalConstants().getShortTimeout());
		overideImpliciWait(driver, GlobalConstants.getGlobalConstants().getShortTimeout());
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getLocatorBy(locator)));
		overideImpliciWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
	}
	
	public void waitForPageLoad(WebDriver driver) throws TimeoutException {
		waitForPageLoad(driver, GlobalConstants.getGlobalConstants().getLongTimeout());
	}
	
	public void waitForPageLoad(WebDriver driver, long timeout) throws TimeoutException {
		ExpectedCondition<Boolean> expectation = waitDriver -> {
			assert waitDriver != null;
			return ((JavascriptExecutor) waitDriver)
					.executeScript("return document.readyState").toString().equalsIgnoreCase("complete");
		};
		sleepInSecond(GlobalConstants.getGlobalConstants().getShortTimeout());
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.until(expectation);
	}

	public boolean isElementUndisplayed(WebDriver driver, String locator) {

		overideImpliciWait(driver, GlobalConstants.getGlobalConstants().getShortTimeout());
		elements = getElements(driver, locator);
		overideImpliciWait(driver, GlobalConstants.getGlobalConstants().getLongTimeout());

		if (elements.size() == 0) {
			System.out.println("Element not in DOM");
			System.out.println("End time = " + new Date().toString());
			return true;
		} else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
			System.out.println("Element not in DOM but not visible/ displayed");
			System.out.println("End time = " + new Date().toString());
			return true;

		} else {

			System.out.println("Element not in DOM and visible");
			return false;
		}

	}

	public void uploadFileByPanelID(WebDriver driver, String panelID, String... fileNames) {
		String filePath = GlobalConstants.getGlobalConstants().getUploadFolderPath();
		String fullFileName = "";
		for (String file : fileNames) {
			fullFileName = fullFileName + filePath + file + "\n";
		}
		fullFileName = fullFileName.trim();

		getElement(driver, getDynamicLocator("", panelID)).sendKeys(fullFileName);
	}

	public void uploadMultipleWebDriver(WebDriver driver, String... fileNames) {
		String filePath = GlobalConstants.getGlobalConstants().getUploadFolderPath();
		String fullFileName = "";
		for (String file : fileNames) {
			fullFileName = fullFileName + filePath + file + "\n";
		}
		fullFileName = fullFileName.trim();

		getElement(driver, getDynamicLocator("?")).sendKeys(fullFileName);
	}

	public boolean isDataStringSortedAscending(WebDriver driver, String locator) {
		ArrayList<String> arrayList = new ArrayList<String>();

		List<WebElement> elementList = driver.findElements(By.xpath(locator));

		for (WebElement element : elementList) {
			arrayList.add(element.getText());
		}

		System.out.println("-------------Dữ liệu trên UI -------------");
		for (String name : arrayList) {
			System.out.println(name);
		}

		ArrayList<String> sortedList = new ArrayList<String>();

		for (String child : arrayList) {
			sortedList.add(child);
		}

		Collections.sort(sortedList);
		System.out.println("-------------Dữ liệu đã SORT ASC trong code -------------");
		for (String name : arrayList) {
			System.out.println(name);
		}

		return sortedList.equals(arrayList);
	}

	public boolean isDataStringSortedDescending(WebDriver driver, String locator) {
		ArrayList<String> arrayList = new ArrayList<>();

		List<WebElement> elementList = driver.findElements(By.xpath(locator));

		for (WebElement element : elementList) {
			arrayList.add(element.getText());
		}

		System.out.println("-------------Dữ liệu trên UI -------------");
		for (String name : arrayList) {
			System.out.println(name);
		}

		ArrayList<String> sortedList = new ArrayList<String>();

		for (String child : arrayList) {
			sortedList.add(child);
		}

		Collections.sort(sortedList);
		System.out.println("-------------Dữ liệu đã SORT ASC trong code -------------");
		for (String name : arrayList) {
			System.out.println(name);
		}

		Collections.reverse(sortedList);
		System.out.println("-------------Dữ liệu đã SORT DESC trong code -------------");
		for (String name : sortedList) {
			System.out.println(name);
		}

		return sortedList.equals(arrayList);
	}

	public boolean isDataFloatSortedAscending(WebDriver driver, String locator) {
		ArrayList<Float> arrayList = new ArrayList<Float>();

		List<WebElement> elementList = driver.findElements(By.xpath(locator));

		for (WebElement element : elementList) {
			arrayList.add(Float.parseFloat(element.getText().replace("$", "").trim()));
		}

		System.out.println("-------------Dữ liệu trên UI -------------");
		for (Float name : arrayList) {
			System.out.println(name);
		}

		ArrayList<Float> sortedList = new ArrayList<Float>();

		for (Float child : arrayList) {
			sortedList.add(child);
		}

		Collections.sort(sortedList);
		System.out.println("-------------Dữ liệu đã SORT ASC trong code -------------");
		for (Float name : arrayList) {
			System.out.println(name);
		}

		return sortedList.equals(arrayList);
	}

	public boolean isDataFloatSortedDescending(WebDriver driver, String locator) {
		ArrayList<Float> arrayList = new ArrayList<Float>();

		List<WebElement> elementList = driver.findElements(By.xpath(locator));

		for (WebElement element : elementList) {
			arrayList.add(Float.parseFloat(element.getText().replace("$", "").trim()));
		}

		System.out.println("-------------Dữ liệu trên UI -------------");
		for (Float name : arrayList) {
			System.out.println(name);
		}

		ArrayList<Float> sortedList = new ArrayList<Float>();

		for (Float child : arrayList) {
			sortedList.add(child);
		}

		Collections.sort(sortedList);
		System.out.println("-------------Dữ liệu đã SORT ASC trong code -------------");
		for (Float name : arrayList) {
			System.out.println(name);
		}

		Collections.reverse(arrayList);
		System.out.println("-------------Dữ liệu đã SORT DESC trong code -------------");
		for (Float name : arrayList) {
			System.out.println(name);
		}

		return sortedList.equals(arrayList);
	}
	
	
//	public void clickToRadioButtonByID(WebDriver driver, String radioID) {
//		waitForElementClickable(driver, RegisterPUI.DYNAMIC_RADIO_BUTTON_BY_ID, radioID);
//		clickToElement(driver, RegisterPUI.DYNAMIC_RADIO_BUTTON_BY_ID, radioID);
//	}
//	
//	public void selectDropDownByName(WebDriver driver, String dropdownName, String itemValue) {
//		waitForElementVisible(driver, RegisterPUI.DYNAMIC_DROPDOWN_BY_NAME, dropdownName);
//		selectDropdownByText(driver, RegisterPUI.DYNAMIC_DROPDOWN_BY_NAME, itemValue, dropdownName);
//	}
//	
//	public String getErrorMessageById(WebDriver driver, String filedID) {
//		waitForElementVisible(driver, LoginPUI.DYNAMIC_ERROR_MESSAGE_BY_ID, filedID);
//		return getElementText(driver, LoginPUI.DYNAMIC_ERROR_MESSAGE_BY_ID, filedID);
//	}
	
//	public void openMenuPageByName(WebDriver driver, String pageName) {
//		waitForElementClickable(driver, BankGuruPUI.DYNAMIC_MENU_LINK, pageName);
//		clickToElement(driver, BankGuruPUI.DYNAMIC_MENU_LINK, pageName);
//	}
	
	
	protected String headerXpath = "//th";
	protected String rowXpath = "//tbody/tr";
	protected String columnXpath = "//td";
	
	
	public List<WebElement> getHeaderElements(WebDriver driver, String locator) {
		return getElements(driver, locator + headerXpath);
	}
	
	public List<String> getAllHeaderNames(WebDriver driver, String locator) {
		List<String> names = new ArrayList<>();
		for (WebElement header : getHeaderElements(driver, locator)) {
			names.add(header.getText());
		}
		return names;
	}
	
	public List<WebElement> getAllRows(WebDriver driver, String locator) {
		return getElements(driver, locator + rowXpath);
	}
	
	public WebElement getRowByIndex(WebDriver driver, int rowIndex, String locator) {
		return getAllRows(driver, locator).get(rowIndex);
	}
	
	public List<WebElement> getAllColumnsInRow(WebDriver driver, int rowIndex, String locator) {
		return getElements(driver, getRowByIndex(driver, rowIndex, locator) + rowXpath);
	}
	
	public WebElement getCell(WebDriver driver, int rowIndex, int colIndex, String locator) {
		return getAllColumnsInRow(driver ,rowIndex, locator).get(colIndex);
	}
	
	public String getCellValue(WebDriver driver, int rowIndex, int colIndex, String locator) {
		return getCell(driver, rowIndex, colIndex, locator).getText();
	}
	
	public String getCellValueByHeaderName(WebDriver driver, int rowIndex, String headerName, String locator) {
		int colIndex = getAllHeaderNames(driver, locator).indexOf(headerName);
		return getCellValue(driver, rowIndex, colIndex, locator);
	}
	
	public List<String> getAllRowValueByHeaderName(WebDriver driver, String headerName, String locator) {
		List<String> values = new ArrayList<>();
		for(int i = 0; i < getAllRows(driver, locator).size(); i++) {
			String header = getCellValueByHeaderName(driver, i, headerName, locator);
			values.add(header);
		}
		return values;
	}
	
	public List<WebElement> getAllRowByHeaderName(WebDriver driver, String headerName, String locator) {
		List<WebElement> rows = new ArrayList<>();
		int colIndex = getAllHeaderNames(driver, locator).indexOf(headerName);
		for(int i = 0; i < getAllRows(driver, locator).size(); i++) {
			WebElement cell = getCell(driver, i, colIndex, locator);
			rows.add(cell);
		}
		return rows;
	}
	
	public String getCellValueByHeaderName(WebDriver driver , WebElement row, String headerName, String locator) {
		int colIndex = getAllHeaderNames(driver, locator).indexOf(headerName);
		WebElement cell = row.findElements(By.xpath(columnXpath)).get(colIndex);
		return cell.getText().trim();
	}
	
	public WebElement getCellByHeader(WebDriver driver, int rowIndex, String headerName, String locator) {
		int colIndex = getAllHeaderNames(driver, locator).indexOf(headerName);
		return getCell(driver, rowIndex, colIndex, locator);
	}
}
