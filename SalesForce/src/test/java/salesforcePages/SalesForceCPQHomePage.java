package salesforcePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.salesforce.util.Page;

public class SalesForceCPQHomePage extends Page{
	private WebDriverWait wait;
	
	@FindBy(xpath = "//span[contains(@class,'appName')]//span[text()='Salesforce CPQ']")
    private WebElement headerText ;
	
    /**
     * Tabs available on Main navigation
     */
    public enum TabNames {
        HOME("Home"), ACCOUNTS("Accounts"), OPPORTUNITIES("Opportunities"), QUOTES("Quotes"), Orders(
                "Orders"), PRODUCTS("Products"), PRODUCT_RULES("Product Rules");

        private final String element;

        TabNames(String name) {
            this.element = name;
        }

        public String getName() {
            return element;
        }
    }
	
	public SalesForceCPQHomePage(WebDriver webDriver) {
		super(webDriver);
		wait = new WebDriverWait(webDriver, GlobalTimeOut);
		wait.until(ExpectedConditions.visibilityOf(headerText));
	}
	
	/**
	 * To click on the tab link on the main navigation bar
	 */
	public void clickTabLink(String tabName){
		String tabXpath = "//one-app-nav-bar-item-root[contains(.,'"+tabName+"')]//a[@href]";
		wait = new WebDriverWait(webDriver, GlobalTimeOut);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(tabXpath)));
		jsClick(By.xpath(tabXpath));
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
