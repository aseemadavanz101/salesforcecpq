package com.salesforce.util;

import java.util.ArrayList;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonObjectRepository {

	private WebDriver driver;

	private WebDriverWait wait;

	public static String defaultWindow;

	public CommonObjectRepository(WebDriver driver) {

		this.driver = driver;
		PageFactory.initElements(driver, this);
		AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(driver, 15);
		PageFactory.initElements(factory, this);
		wait = new WebDriverWait(driver, 30);

	}

	/**
	 * Method to switch to the tab and move driver object focus on newly open tab
	 * 
	 * @param tab
	 */
	public void switchToNewTab(int tab) {
		defaultWindow = driver.getWindowHandle();
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tab));
		waitForSecondToLoad();
	}

	public void swtichToMainWindow() {
		driver.close();
		driver.switchTo().window(defaultWindow);
	}

	/**
	 * Method for hard core wait , not use until need very important
	 */
	public void waitForSecondToLoad() {
		try {
			Thread.sleep(5000);
			
			JavascriptExecutor js = (JavascriptExecutor)driver;
			js.executeScript("return document.readyState").toString().equals("complete");
			js.executeScript("return jQuery.active == 0");
//			WebDriverWait wait = new WebDriverWait(driver, 30);
//			
//            wait.until(new ExpectedCondition<Boolean>() {
//                @Override
//                public Boolean apply(org.openqa.selenium.WebDriver driver) {
//                    return (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0") ;
//                }
//              
//            });
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
