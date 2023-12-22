package com.salesforce.util;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.salesforce.configuration.TestBase;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.By.cssSelector;
import static org.testng.Assert.assertTrue;

public class Page {

	protected static Properties con;
	private static String logInUsername;
	private static String logInPassword;
	public static int GlobalTimeOut = TestBase.GlobalTime;
	private static final String BLOCKER = "blocker";
	protected static final String PARENTTRAYCONTENT = "[id*=trayContent]";
	@FindBy(id = "navigation-dialog")
	private WebElement modalWindow;

	@FindBy(id = "encounter-detailed-save-btn")
	@CacheLookup
	private WebElement saveEncBtn;

	@FindBy(id = "navigation-save")
	private WebElement saveButton;

	@FindBy(id = "navigation-no-save")
	@CacheLookup
	private WebElement dontSave;

	@FindBy(id = "navigation-cancel")
	@CacheLookup
	private WebElement cancelButton;
	public WebDriver webDriver;

	public Page(WebDriver webDriver) {
		this.webDriver = webDriver;
		PageFactory.initElements(webDriver, this);
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public String getTitle() {
		return webDriver.getTitle();
	}

	protected void Log(String x) {
		System.out.println(x);
	}

	protected void changeWaitTime(int newWaitTime) {
		try {
			webDriver.manage().timeouts().wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void changeWaitTimeGUI(int newWaitTime) {
		try {
			Thread.sleep(newWaitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void syncBlocker() {
		syncBlocker(GlobalTimeOut);
	}

	protected void syncBlocker(int delay) {
		// changeWaitTime(5);
		WebDriverWait wait = new WebDriverWait(webDriver, delay);
		List<WebElement> blockerElement = webDriver.findElements(By.id(BLOCKER));
		if (blockerElement.size() != 0) {
			wait.until(ExpectedConditions.stalenessOf(blockerElement.get(0)));
		}

	}

	public void syncBlockerInLoop(int delay) {
		for (int i = 0; i < 4; i++) {
			try {
				syncBlocker(delay);
				break;
			} catch (TimeoutException e) {
				String url = webDriver.getCurrentUrl();
			}
		}
	}

	public static String getLoginUsername() {
		if (logInUsername == null || logInUsername.isEmpty()) {
			logInUsername = TestBase.usernameValue;
		}
		return logInUsername;
	}

	public static String getLoginPassword() {
		if (logInPassword == null || logInPassword.isEmpty()) {
			logInPassword = TestBase.passValue;
		}
		return logInPassword;
	}

	/*
	 * Wait time logic when blocker remains in DOM protected void
	 * syncBlocker(int delay){ changeWaitTime(3); WebDriverWait wait = new
	 * WebDriverWait(webDriver, delay); final List<WebElement> blockerElement =
	 * webDriver.findElements(By.id(BLOCKER)); if (blockerElement.size()!=0) {
	 * wait.until(new ExpectedCondition<Boolean>() {
	 * 
	 * @Override public Boolean apply(WebDriver webDriver) { try {
	 * Thread.sleep(2000); return (!blockerElement.get(0).isDisplayed()); }
	 * catch (Exception ex) { return true; }
	 * 
	 * } });//ExpectedConditions.stalenessOf(blockerElement.get(0))); }
	 * changeWaitTime(GlobalTimeOut); }
	 */

	public Page refreshPage() {
		syncBlockerInLoop(GlobalTimeOut);
		webDriver.navigate().refresh();
		syncBlockerInLoop(GlobalTimeOut);

		return this;
	}

	protected String selectOptionsByIndex(WebElement element, int option) {
		if (isNotEmpty(option)) {
			Select select = new Select(element);
			select.selectByIndex(option);
			return select.getFirstSelectedOption().getText();
		}
		return null;

	}

	public String selectOptionsByValue(WebElement element, String option) {
		Select select = new Select(element);
		if (isNotEmpty(option)) {
			select.selectByValue(option);
		}
		return select.getFirstSelectedOption().getText();
	}

	public String selectOptionsByVisibleText(WebElement element, String visibleText) {
		String getSelectedOption = null;
		if (isNotEmpty(visibleText)) {
			Select select = new Select(element);
			select.selectByVisibleText(visibleText);
			getSelectedOption = select.getFirstSelectedOption().getText();
		}
		return getSelectedOption;
	}

	// Common items for pages
	@SuppressWarnings("unchecked")
	public void selectOptions(WebElement element, Object options, boolean clear) {
		if (isNotEmpty(options)) {
			Select select = new Select(element);
			if (select.isMultiple()) {
				if (clear) {
					select.deselectAll();
				}
				for (String option : (List<String>) options) {
					select.selectByVisibleText(((String) options).trim());
				}
			} else {
				select.selectByVisibleText(((String) options).trim());
			}
		} else {
			throw new IndexOutOfBoundsException("Options is not correct!");
		}
	}

	@SuppressWarnings("unchecked")
	public void selectOptionsS2(WebElement element, Object options, boolean clear) {
		if (isNotEmpty(options)) {
			if (options instanceof List) {
				if (((List) options).isEmpty()) {
					throw new IndexOutOfBoundsException("The list of options that should be "
							+ "selected is empty. It should contain at least one value.");
				}
				if (clear) {
					List<WebElement> closeButtons = element.findElements(By.className("select2-search-choice-close"));
					for (WebElement el : closeButtons) {
						el.click();
					}
					element.click();
				}
				for (String option : (List<String>) options) {
					setOptionInSelect2(element, option);
				}
			} else {
				setOptionInSelect2(element, (String) options);
			}
		} else {
			throw new IndexOutOfBoundsException("Options is not correct!");
		}
	}

	private void setOptionInSelect2(WebElement element, String option) {
		element.click();
		WebElement dropdown = webDriver.findElement(By.id("select2-drop"));
		List<WebElement> selectOptions = dropdown.findElements(By.tagName("li"));
		for (WebElement opt : selectOptions) {
			if (opt.getText().trim().contains(option)) {
				opt.click();
				return;
			}
		}
		throw new IndexOutOfBoundsException("Option '" + option + "' is not found!");
	}

	private void setOptionInSelect2Reg(WebElement element, String option) {
		// element.click();
		List<WebElement> selectOptions = webDriver
				.findElements(By.cssSelector(".select2-drop-active li.select2-result-selectable"));
		for (WebElement opt : selectOptions) {
			if (opt.getText().trim().contains(option)) {
				opt.click();
				return;
			}
		}
		throw new IndexOutOfBoundsException("Option '" + option + "' is not found!");
	}

	public String getSelectedOptionS2(WebElement dropDown) {
		WebElement option = dropDown.findElement(By.cssSelector(".select2-chosen"));
		return option.getText();
	}

	public List<String> getSelectedOptionsS2(WebElement multiDropDown) {
		List<String> selectedOptions = new ArrayList<>();
		List<WebElement> optionList = multiDropDown.findElements(By.cssSelector(".select2-search-choice"));
		for (WebElement option : optionList) {
			selectedOptions.add(option.getText());
		}
		return selectedOptions;
	}

	public List<String> getOptionsS2(WebElement multiDropDown) {
		multiDropDown.click();
		List<String> options = new ArrayList<>();
		List<WebElement> optionList = webDriver.findElements(By.cssSelector(".select2-results-dept-1 div"));
		for (WebElement option : optionList) {
			options.add(option.getText());
		}
		options.remove("(Select one)");
		WebElement clickedDropDown = webDriver.findElement(By.id("select2-drop-mask"));
		clickedDropDown.click();
		return options;
	}

	public List<String> getOptionsS2Reg(WebElement multiDropDown) {
		multiDropDown.click();
		List<String> options = new ArrayList<>();
		List<WebElement> optionList = webDriver
				.findElements(By.cssSelector(".select2-drop-active li.select2-result-selectable"));
		for (WebElement option : optionList) {
			options.add(option.getText());
		}
		options.remove("(Select one)");
		WebElement clickedDropDown = webDriver.findElement(By.id("select2-drop-mask"));
		// clickedDropDown.click();
		return options;
	}

	public String selectOptionS2RegRandom(WebElement multiDropDown) {
		Random rand = new Random();
		List<String> options = getOptionsS2Reg(multiDropDown);
		String randomOption = options.get(rand.nextInt(options.size()));
		// webDriver.findElement(By.tagName("body")).click();
		setOptionInSelect2Reg(multiDropDown, randomOption);
		return randomOption;
	}

	public String setRandomItemFromDropDown(WebElement dropDown) {
		int numOptions = getOptionAmount(dropDown);
		if (numOptions == 1) {
			return null;
		}
		return selectOptionsByIndex(dropDown, new Random().nextInt(numOptions - 1) + 1);
	}

	protected boolean isNotEmpty(Object data) {
		return ((data != null) && (data != "") && (data != "null"));
	}

	protected List<String> getOptions(WebElement element) {
		List<WebElement> webArray = new Select(element).getOptions();
		List<String> result = new ArrayList<>();
		for (WebElement obj : webArray) {
			result.add(obj.getText());
		}
		return result;
	}

	protected String getAttributeOfSelectedOption(WebElement element, String attribute) {
		return new Select(element).getFirstSelectedOption().getAttribute(attribute);
	}

	public String getAttributeOfTextBox(WebElement webElement, String attribute) {
		return webElement.getAttribute(attribute).trim();
	}

	protected String getSelectedOption(WebElement element) {
		String retVal = new Select(element).getFirstSelectedOption().getText();
		return retVal;
	}

	public List<String> getSelectedOptions(WebElement element) {
		List<WebElement> webArray = new Select(element).getAllSelectedOptions();
		List<String> result = new ArrayList<>();
		for (WebElement obj : webArray) {
			result.add(obj.getText());
		}
		return result;
	}

	protected void fillInMask(WebElement element, String text, boolean clear, boolean plusClick) {
		if (clear) {
			Actions builder = new Actions(webDriver);
			builder.keyDown(Keys.CONTROL);
			builder.build().perform();
			element.sendKeys("a");
			builder.keyUp(Keys.CONTROL);
			builder.build().perform();
			element.clear();
		}
		if (isNotEmpty(text)) {
			Actions builder = new Actions(webDriver);
			builder.keyDown(Keys.CONTROL);
			builder.build().perform();
			element.sendKeys("a");
			builder.keyUp(Keys.CONTROL);
			builder.build().perform();
			element.sendKeys(text);
		}
		if (plusClick) {
			// headerContainer.click();
			element.sendKeys(Keys.TAB);
		}
		element.click();
	}

	protected void fillInEditBox(WebElement element, String text, boolean clear) {
		syncBlocker();
		if (clear && (text != null)) {
			// syncBlocker();
			element.clear();
		}
		if (isNotEmpty(text)) {
			element.sendKeys(text);
		}
		// this if provide to us possibility to delete all context from edit
		// box, method .clear does NOT work on LT page
		if (text == null) {
			clearTextField(element);
		}
		// element.sendKeys(Keys.TAB); //Commented because affects searches
	}

	public void clearTextField(WebElement element) {
		Actions builder = new Actions(webDriver);
		builder.keyDown(Keys.CONTROL);
		builder.build().perform();
		element.sendKeys("a");
		builder.keyUp(Keys.CONTROL);
		builder.build().perform();
		element.sendKeys(Keys.DELETE);
	}

	protected void fillInEditBoxJS(String CSSSelector, String text) {
		syncBlocker();
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("$('" + CSSSelector + "').val('" + text + "').focus();");
	}

	protected void fillInEditBoxJSTrigger(String CSSSelector, String text) {
		syncBlocker();
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("$('" + CSSSelector + "').val('" + text + "').trigger(\"change\")");
	}

	protected void fillInEditBoxJS(WebElement element, String text) {
		syncBlocker();
		((JavascriptExecutor) webDriver).executeScript("arguments[0].value='" + text + "'", element);
	}

	protected void fillInEditBoxJSTriggerForListByIndex(String CSSSelector, String text, String indx) {
		syncBlocker();
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("$('" + CSSSelector + "').eq('" + indx + "').val('" + text + "').trigger(\"change\")");
	}

	/**
	 * Method checks element is displayed or not.
	 * 
	 * @param element
	 *            checked web element
	 * @param delay
	 *            time for waiting for visibility of @element
	 * @return true if element is displayed; false if not.
	 */
	public boolean isElementDisplayed(WebElement element, int delay) {
		try {
			webDriver.manage().timeouts().implicitlyWait(delay, TimeUnit.MILLISECONDS);
			return element.isDisplayed();
		} catch (org.openqa.selenium.NoSuchElementException e) {
			webDriver.manage().timeouts().implicitlyWait(delay, TimeUnit.MILLISECONDS);
			return false;
		} catch (StaleElementReferenceException e) {
			webDriver.manage().timeouts().implicitlyWait(delay, TimeUnit.MILLISECONDS);
			return false;
		} catch (TimeoutException e) {
			webDriver.manage().timeouts().implicitlyWait(delay, TimeUnit.MILLISECONDS);
			return false;
		}
	}

	/**
	 * Method checks element is displayed or not. Delay is by default
	 * 
	 * @param element
	 *            is checked web element
	 * @return true if element is displayed; false if not.
	 */
	public boolean isElementDisplayed(WebElement element) {
		return isElementDisplayed(element, GlobalTimeOut);
	}

	/**
	 * Method checks element is displayed or not. Delay is by default
	 * 
	 * @param element
	 *            is checked web element
	 * @param message
	 *            prints the log
	 * @return true if element is displayed; false if not.
	 */
	public boolean isElementDisplayed(WebElement element, String message) {
		if (isElementDisplayed(element, GlobalTimeOut)) {
			Reporter.log(message + " is Present");
			return true;
		} else {
			Reporter.log(message + " is not Present");
			return false;
		}
	}

	/**
	 * Method checks element present or not.
	 * 
	 * @param selector
	 *            CSS selector of searched element
	 * @return true if element is displayed; false if not.
	 */
	public boolean isElementPresentByCSS(String selector) {
		webDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		if (webDriver.findElements(By.cssSelector(selector)).size() > 0) {
			webDriver.manage().timeouts().implicitlyWait(GlobalTimeOut, TimeUnit.SECONDS);
			return true;
		}
		webDriver.manage().timeouts().implicitlyWait(GlobalTimeOut, TimeUnit.SECONDS);
		return false;
	}

	/**
	 * Method wait for all Ajax Calls end or for GlobalTimeOut seconds
	 */
	public void waitForEndOfAllAjaxes() {
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, GlobalTimeOut);
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(org.openqa.selenium.WebDriver driver) {
					return (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
				}
			});
		} catch (Exception e) {
			System.out.println("waitForEndOfAllAjaxes failed: " + e);
		}
	}

	/**
	 * Checks that element is exist default time-out 1 sec
	 * 
	 * @param element
	 *            - checked element
	 * @return true if element exist
	 */
	public boolean isExist(WebElement element) {
		changeWaitTime(3);
		try {
			element.getSize(); // will invoke findElement
			return true;
		} catch (Exception exception) {
			return false;
		} finally {
			changeWaitTime(GlobalTimeOut);
		}
	}

	/**
	 * Checks that element/s is exist default time-out 1 sec
	 * 
	 * @param elements
	 *            - locator of checked element/s of class By
	 * @return true if element/s exist
	 */
	public boolean isExists(By elements) {
		boolean result = true;
		changeWaitTime(1);
		if (webDriver.findElements(elements).size() <= 0) {
			result = false;
		}
		changeWaitTime(GlobalTimeOut);
		return result;
	}

	public void scrollToElement(WebElement element) {
		int elementPosition = element.getLocation().getY();
		String js = String.format("window.scrollBy(0,%s)", elementPosition); // +
																				// 100);
		((JavascriptExecutor) webDriver).executeScript(js);
	}

	public void scrollIntoView(WebElement element) {
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(false);", element);
	}

	public void scrollDownVertically() {
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
	}

	/**
	 * Checks page title string and expected string equivalency.
	 * 
	 * @param title
	 *            - Web Element with title text
	 * @param titleContent
	 *            - Expected page title string.
	 */
	public void verifyTitle(WebElement title, String titleContent) {
		assertTrue(title.getText().contains(titleContent),
				"Wrong page title.\nExpected: " + titleContent + "\nActual: " + title.getText());
	}

	public void clearFieldWithControlADel(WebElement element) {
		element.sendKeys(Keys.CONTROL, Keys.DELETE);
	}

	public String returnElementValue(String cssSelector) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		String query = "return $('" + cssSelector + "').val()";
		return (String) js.executeScript(query);
	}

	public void goBack() {
		webDriver.navigate().back();
	}

	protected int getOptionAmount(WebElement element) {
		Select select = new Select(element);
		return select.getOptions().size();
	}

	/**
	 * Gets text from web-element and compares with text-string
	 * 
	 * @param title
	 *            - web-element to check
	 * @param titleText
	 *            - text to compare with
	 */
	public void checkTitle(WebElement title, String titleText) {
		WebDriverWait wait = new WebDriverWait(webDriver, GlobalTimeOut);
		wait.until(ExpectedConditions.visibilityOf(title));
		if (!title.getText().trim().toLowerCase().contains(titleText.trim().toLowerCase())) {
			throw (new IndexOutOfBoundsException(
					"Wrong page. Expected title: " + titleText + " Actual Title: " + title.getText()));
		}
	}

	public boolean isCheckboxSelected(WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		return (Boolean) js.executeScript("return $(arguments[0]).is(':checked')", element);
	}

	public void selectCheckbox(WebElement option) {
		if (!isCheckboxSelected(option)) {
			option.click();
		}
	}

	public void deselectCheckbox(WebElement option) {
		if (isCheckboxSelected(option)) {
			option.click();
		}
	}

	/**
	 * CHecks or unchecks checkboxes
	 * 
	 * @param check
	 *            Boolean: if true - method will check given CheckBoxes, if
	 *            false - uncheck
	 * @param elements
	 *            List of checkbox elements
	 */
	public void findCheckBoxesAndCheck(Boolean check, WebElement... elements) {
		for (WebElement elem : elements) {
			scrollIntoView(elem);

			if (check) {
				if (!elem.isSelected()) {
					elem.click();
				}
			} else {
				if (elem.isSelected()) {
					elem.click();
				}
			}
		}
	}

	public void navigationReporter(String pageNavigatedTo) {
		Reporter.log("<br><b><font color='blue'> INFO:</b></font> Page navigated to :" + pageNavigatedTo + "<br>");
	}

	/**
	 * Compare set can be used for String, int types -- if there are no
	 * duplicate values
	 * 
	 * @param actualList
	 *            - First list; actual set
	 * @param expectedList
	 *            - Second list to compare to; expected set
	 * @param <T>
	 *            - String, int etc. Both parameters should have some type
	 * @return - boolean value, true-same, false-different
	 */
	public <T> boolean compareSets(HashSet<T> actualList, HashSet<T> expectedList) {
		Reporter.log("#### Comparing two sets ####");
		if ((actualList != null && expectedList != null) && (actualList.size() == expectedList.size())) {
			expectedList.removeAll(actualList);
			if (!expectedList.isEmpty()) {
				Reporter.log("The following elements did not compare: " + expectedList.toString());
				return false;
			} else {
				Reporter.log("Both lists contain same values.");
				return true;
			}
		}
		Reporter.log("Both lists are not of same size.");
		return false;
	}

	public boolean clickSaveIfExist() {
		if (modalWindow.isDisplayed()) {
			saveButton.click();
			return true;
		}
		return false;
	}

	public boolean clickNoIfExist() {
		if (isExist(modalWindow) && modalWindow.isDisplayed()) {
			dontSave.click();
			return true;
		}
		return false;
	}

	public <T, U> boolean compareMaps(HashMap<T, U> actualList, HashMap<T, U> expectedList) {
		Reporter.log("#### Comparing two Maps ####");
		try {
			for (T k : expectedList.keySet()) {
				if (!actualList.get(k).equals(expectedList.get(k))) {
					Reporter.log("actualValue: " + actualList.get(k));
					Reporter.log("expectedValue: " + expectedList.get(k));
					Reporter.log("The two map values do not match");
					return false;
				}
			}
			for (T y : actualList.keySet()) {
				if (!expectedList.containsKey(y)) {
					Reporter.log("Key " + y + " not present in expected List");
					Reporter.log("Keys in the two maps do not match");
					return false;
				}
			}
		} catch (NullPointerException np) {
			Reporter.log("Null Keys or Values Present");
			return false;
		}
		Reporter.log("Both maps contain same keys and values.");
		return true;
	}

	public boolean clickCancelIfExist() {
		if (isExist(modalWindow)) {
			cancelButton.click();
			Reporter.log("Clicked cancel button.");
			return true;
		}
		Reporter.log("Cancel button not displayed");
		return false;
	}

	public String returnElementValueInClipBoard(WebElement element) throws Exception {
		Actions builder = new Actions(webDriver);
		builder.keyDown(Keys.CONTROL);
		builder.build().perform();
		element.sendKeys("a");
		builder.keyUp(Keys.CONTROL);
		builder.build().perform();
		builder.keyDown(Keys.CONTROL);
		element.sendKeys("c");
		builder.keyUp(Keys.CONTROL);
		builder.build().perform();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		return clipboard.getData(DataFlavor.stringFlavor).toString();
	}

	public void fillEditBoxUsingWebElement(WebElement element, String text) {
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].value=arguments[1]", element, text);
		js.executeScript("$(arguments[0]).change();", element);
	}

	/**
	 * Checks that element is clickable or not
	 * 
	 * @param element
	 *            - checked element
	 * @return true if element is clickable or else false
	 */
	public boolean isClickable(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(webDriver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String setOptionInListBox(WebElement element) {
		String optionText = "";
		Random rand = new Random();
		List<WebElement> selectOptions = element.findElements(By.tagName("li"));
		WebElement randomOption = selectOptions.get(rand.nextInt(selectOptions.size()));
		optionText = randomOption.getText().trim();
		randomOption.click();
		return optionText;
	}

	public void clickSaveEncBtn() {
		saveEncBtn.click();
	}

	/**
	 * Method checks element displayed or not
	 * 
	 * @param driver
	 * @param by
	 * @return
	 */
	public boolean isElementDisplayed(WebDriver driver, By by) {
		try {
			boolean isdisplayed = driver.findElement(by).isDisplayed();
			return isdisplayed;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * This method return the css of the given element
	 * 
	 * @param element
	 * @return
	 */
	public String getCSSOfGivenElement(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) webDriver;
		String script = "var s = '';" + "var o = getComputedStyle(arguments[0]);" + "for(var i = 0; i < o.length; i++){"
				+ "s+=o[i] + ':' + o.getPropertyValue(o[i])+';';}" + "return s;";
		String css = executor.executeScript(script, element).toString();
		return css;
	}

	/**
	 *
	 * @return True if JavaScript Alert is present on the page otherwise false
	 */
	public boolean isAlertPresent() {
		try {
			webDriver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException ex) {
			return false;
		}
	}

	// Method to accept alert for Chrome and Edge browsers
	public void acceptAlertBrowserSpecific() {
		Page page = new Page(webDriver);
		if (isAlertPresent()) {
			webDriver.switchTo().alert().accept();
			Reporter.log("Accepted Alert");
		}
	}

	/**
	 * To click the TAB key
	 */
	public void clickTABKey() {
		webDriver.switchTo().activeElement().sendKeys(Keys.TAB);
		Reporter.log("Click the TAB Key");
	}

	public void jsClick(By locator) {
		jsClick(webDriver.findElement(locator));
	}

	public void jsClick(WebElement elem) {
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", elem);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			;
			/* ignore */ }
	}

}
