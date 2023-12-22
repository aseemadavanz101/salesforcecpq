package com.salesforce.testscripts;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import salesforcePages.LoginPage;
import salesforcePages.SalesForceCPQHomePage;
import salesforcePages.SalesForceHomePage;
import salesforceTestData.TestDataMasterModal;

import com.salesforce.configuration.TestBase;
import com.salesforce.util.Page;

public class CreateOpportunityForm extends TestBase {

	private WebDriver webDriver;

	@BeforeClass
	public void init() {
		webDriver = staticDriver.get();
	}

	@Test
	public void createOpportunityForm() throws Exception {
		LoginPage login = new LoginPage(webDriver);
		login.setCredential(Page.getLoginUsername(), Page.getLoginPassword());
		SalesForceHomePage salesForceHomePage = login.clicOnLogInButton();
		salesForceHomePage.searchAndSelectApp(TestDataMasterModal.salesforceCPQ);
		SalesForceCPQHomePage salesForceCPQHomePage = new  SalesForceCPQHomePage(webDriver);
	}

}
