package salesforcePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.salesforce.util.Page;



public class SalesForceHomePage extends Page {
	private WebDriverWait wait;
//	protected WebDriver driver;
	
	@FindBy(xpath = "//a//span[@class='title slds-truncate'][text()='Home']")
    private WebElement homeText ;
	
	@FindBy(css = "div[class='appLauncher slds-context-bar__icon-action'] div[class='slds-icon-waffle']")
    private WebElement appLauncher ;
	
	@FindBy(css = "div[class='panel-content scrollable'] input[class='slds-input']")
    private WebElement searchAppInput;
	

	
	public SalesForceHomePage(WebDriver webDriver) {
		super(webDriver);
		wait = new WebDriverWait(webDriver, GlobalTimeOut);
		wait.until(ExpectedConditions.visibilityOf(homeText));
		
	}
	
	public void searchAndSelectApp(String appName){
		appLauncher.click();           
        syncBlocker();
        waitForEndOfAllAjaxes();
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        webDriver.switchTo().activeElement().sendKeys(appName);
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        webDriver.findElement(By.xpath("//div[@class='al-menu-dropdown-list']//one-app-launcher-menu-item[@class='slds-dropdown__item']//a//b[text()='"+appName+"']")).click();
        syncBlocker();
        waitForEndOfAllAjaxes();
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
