package salesforcePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.salesforce.configuration.TestBase;
import com.salesforce.util.Page;

public class LoginPage extends Page {
	

	private WebDriverWait wait;
//	public WebDriver driver;
	
	@FindBy(css = "div#theloginform input#username")
    private WebElement userName ;
	
	@FindBy(css = "div#theloginform input#password")
    private WebElement password ;
	
	@FindBy(css = "div#theloginform input#Login")
    private WebElement logIn ;

	public LoginPage(WebDriver driver) {
        super(driver);
        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void setCredential(String usernameValue, String passValue) throws InterruptedException{
		userName.sendKeys(usernameValue);
		password.sendKeys(passValue);
	}
	
	public SalesForceHomePage clicOnLogInButton(){
        logIn.click(); 
        syncBlocker();
        return new SalesForceHomePage(getWebDriver());
}


}
