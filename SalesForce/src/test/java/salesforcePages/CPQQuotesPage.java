package salesforcePages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.salesforce.util.Page;

public class CPQQuotesPage extends Page{
	private WebDriverWait wait;
	
	@FindBy(xpath = "//li[contains(@class,'slds-button') ]//a//div[text()='New']")
    private WebElement newButton;
	
	@FindBy(xpath = "//button[text()='Save']")
    private WebElement saveButton;
	
	@FindBy(xpath = "//span[text()='Delete']")
    private WebElement deleteButton;
	
	@FindBy(css = "span.toastMessage")
    private WebElement toastMessage;
	
	public CPQQuotesPage(WebDriver webDriver) {
		super(webDriver);
		wait = new WebDriverWait(webDriver, GlobalTimeOut);
		wait.until(ExpectedConditions.visibilityOf(newButton));
	}
	
	/**
	 * To click on the New button for adding new quote
	 */
	public void clickNewButton(){
		newButton.click();
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * To click on the check box for adding new quote
	 */
	public void clickCheckbox(String lblName){
		String xpathCheckBox = "//lightning-primitive-input-checkbox[contains(.,'"+lblName+"')]//input";
		webDriver.findElement(By.xpath(xpathCheckBox)).click();
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * To select the option from the dropdown
	 */
	public void selectDropdown(String lblName, String option){
		String xpathTypeDropdown = "//lightning-combobox[contains(.,'"+lblName+"')]//button[contains(@class,'slds-combobox__input')]";
		webDriver.findElement(By.xpath(xpathTypeDropdown)).click();
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
		String xpathOption = "//lightning-base-combobox-item//span[contains(@title,'"+option+"')]";
		webDriver.findElement(By.xpath(xpathOption)).click();
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * To select the option from the combobox
	 */
	public void selectCombobox(String lblName, String option){
		String xpathCombobox = "//lightning-grouped-combobox[contains(.,'"+lblName+"')]//lightning-base-combobox//input";
		scrollIntoView(webDriver.findElement(By.xpath(xpathCombobox)));
		webDriver.findElement(By.xpath(xpathCombobox)).click();
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
        webDriver.findElement(By.xpath(xpathCombobox)).sendKeys(option);
		String xpathOption = "//lightning-base-combobox-item//span[contains(@title,'"+option+"')]";
		webDriver.findElement(By.xpath(xpathOption)).click();
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * To enter the text into the textfield
	 */
	public void enterTextField(String lblName, String text){
		String xpathTextfield = "//lightning-primitive-input-simple[contains(.,'"+lblName+"')]//input";
		webDriver.findElement(By.xpath(xpathTextfield)).sendKeys(text);
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
	}
	
	/**
	 * To click on the save button
	 */
	public void clickSaveButton(){
		saveButton.click();
		//syncBlocker();
		waitForEndOfAllAjaxes();
	}
	
	/**
	 * To get the toast message
	 */
	public String getToastmessage(){
		return toastMessage.getText().trim();
	}
	
	/**
	 * To click on action button of the given quote id
	 */
	public void clickActionBtnOnQuoteId(String quoteId,String action){
		String xpathActionBtn = "//tr[contains(.,'"+quoteId+"')]//td//span[contains(@class,'slds-icon-utility-down')]";
		webDriver.findElement(By.xpath(xpathActionBtn)).click();
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
		String xpathOption = "//div[contains(@class,'forceActionsDropDownMenuList')]//ul/li[contains(.,'"+action+"')]";
		webDriver.findElement(By.xpath(xpathOption)).click();
		syncBlocker();
		waitForEndOfAllAjaxes();
        try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
        }
	
	/**
	 * To click on the save button
	 */
	public void clickDeleteBtnDelQuoteModal(){
		deleteButton.click();
		//syncBlocker();
		waitForEndOfAllAjaxes();
	}
	
	
	String primaryCheckBox = "//lightning-primitive-input-checkbox[contains(.,'Primary')]//input";
	String orderedCheckBox = "//lightning-primitive-input-checkbox[contains(.,'Ordered')]//input";
	String ordrByQuoteLnGrpCheckBox = "//lightning-primitive-input-checkbox[contains(.,'Order By Quote Line Group')]//input";
	String prntLnItemsCheckBox = "//lightning-primitive-input-checkbox[contains(.,'Print Line Items')]//input";
	String grpLnItemsCheckBox = "//lightning-primitive-input-checkbox[contains(.,'Group Line Items')]//input";
	String watrmarkShownCheckBox = "//lightning-primitive-input-checkbox[contains(.,'Watermark Shown')]//input";
	
	String TypeCmbBox = "//lightning-combobox[contains(.,'Type')]//button[contains(@class,'slds-combobox__input')]";
	String StatusCmbBox = "//lightning-combobox[contains(.,'Status')]//button[contains(@class,'slds-combobox__input')]";
	String ContractingMethodCmbBox = "//lightning-combobox[contains(.,'Contracting Method')]//button[contains(@class,'slds-combobox__input')]";
	String GenerateContractedPriceCmbBox = "//lightning-combobox[contains(.,'Generate Contracted Price')]//button[contains(@class,'slds-combobox__input')]";
	String OrderByCmbBox = "//lightning-combobox[contains(.,'Order By')]//button[contains(@class,'slds-combobox__input')]";
	String PaymentTermsCmbBox = "//lightning-combobox[contains(.,'Payment Terms')]//button[contains(@class,'slds-combobox__input')]";
	String BillingFrequencyCmbBox = "//lightning-combobox[contains(.,'Billing Frequency')]//button[contains(@class,'slds-combobox__input')]";

}
