package com.salesforce.testscripts;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.salesforce.configuration.TestBase;
import com.salesforce.util.Page;

import salesforcePages.CPQQuotesPage;
import salesforcePages.LoginPage;
import salesforcePages.SalesForceCPQHomePage;
import salesforcePages.SalesForceHomePage;
import salesforceTestData.TestDataMasterModal;

public class CreateQuoteForm extends TestBase {

	private WebDriver webDriver;
	private String quoteId;

	@BeforeClass
	public void init() {
		webDriver = staticDriver.get();
	}

	@Test
	public void addAndRemoveQuote() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		LoginPage login = new LoginPage(webDriver);
		login.setCredential(Page.getLoginUsername(), Page.getLoginPassword());
		SalesForceHomePage salesForceHomePage = login.clicOnLogInButton();
		salesForceHomePage.searchAndSelectApp(TestDataMasterModal.salesforceCPQ);
		SalesForceCPQHomePage salesForceCPQHomePage = new  SalesForceCPQHomePage(webDriver);
		salesForceCPQHomePage.clickTabLink(SalesForceCPQHomePage.TabNames.QUOTES.getName());
		CPQQuotesPage cpqQuotesPage =new CPQQuotesPage(webDriver);
		cpqQuotesPage.clickNewButton();
		//cpqQuotesPage.clickCheckbox("Primary");
		cpqQuotesPage.selectDropdown("Type","Quote");
		cpqQuotesPage.selectDropdown("Status","Approved");
		cpqQuotesPage.selectCombobox("Opportunity","test opp");
		cpqQuotesPage.enterTextField("Referral Code","234567");
		//cpqQuotesPage.selectCombobox("Account","Testing123");
		cpqQuotesPage.clickSaveButton();
		String msg = cpqQuotesPage.getToastmessage();
		softAssert.assertTrue(msg.contains("was created."), "Quote id :"+quoteId+" was created.");
		quoteId = msg.split("\"")[1];
		salesForceCPQHomePage.clickTabLink(SalesForceCPQHomePage.TabNames.QUOTES.getName());
		cpqQuotesPage =new CPQQuotesPage(webDriver);
		cpqQuotesPage.clickActionBtnOnQuoteId(quoteId, "Delete");
		cpqQuotesPage.clickDeleteBtnDelQuoteModal();
		softAssert.assertTrue(cpqQuotesPage.getToastmessage().contains("was deleted."), "Quote id :"+quoteId+" was deleted.");
	}
}
