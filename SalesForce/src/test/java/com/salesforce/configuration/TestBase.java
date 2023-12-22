package com.salesforce.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

public abstract class TestBase {

	protected static Properties con;
	public static String downloadLocation, insertIntoCRM;
	public static String usernameValue, passValue;
	static File file;
	public static int GlobalTime;
	public static String globalTimevalue;
	
	
	public static ThreadLocal<WebDriver> staticDriver = new ThreadLocal<WebDriver>() {

		@Override
		protected WebDriver initialValue() {

			return initDriver();
		}
	};

	public WebDriver driver = null;

	@BeforeClass(alwaysRun = true)
	public void initializeClass() throws Exception {
		initProperties();
		driver = staticDriver.get();
		login();
//		
//	        System.out.print(GlobalTime);
//	        if (!globalTimevalue.equals("")) {
//	        	GlobalTime = Integer.parseInt(ScrapBaseTest.globalTimevalue);
//	        }
//	        else {
//	        	GlobalTime = 60;
//	        }
//		
	}

	@AfterSuite
	public void tearDown() {
		System.out.println("Script execution is End");
		driver.quit();
	}

	private void login() throws InterruptedException {
    
		driver.navigate().to(con.getProperty("url"));
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		Thread.sleep(300);
	}
	

	public static void initProperties() throws Exception {
		FileInputStream input = null;
		file = new File("");
		System.out.println(file.getAbsolutePath());
		input = new FileInputStream(file.getAbsolutePath()
				+ "\\config.properties");

		try {
			con = new Properties();
			con.load(input);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static WebDriver initDriver() {

		WebDriver localDriver = null;
		System.out.println(con.getProperty("url"));
		try {
			
			String browser = con.getProperty("browser");
			usernameValue = con.getProperty("username");
			passValue = con.getProperty("password");
			globalTimevalue = con.getProperty("timeout");
			
			String envBrowser = System.getenv("medsolis.testng.browser");
			if (envBrowser != null)
				browser = envBrowser;

			if (browser.equalsIgnoreCase("firefox")) {
				System.setProperty("webdriver.gecko.driver",
						file.getAbsolutePath() + "/geckodriver.exe");
				ProfilesIni profile = new ProfilesIni();
				FirefoxProfile myprofile = profile.getProfile("default");
				localDriver = new FirefoxDriver(myprofile);

			}

			if (browser.equalsIgnoreCase("IE")) {
				System.setProperty("webdriver.ie.driver",
						file.getAbsolutePath() + "/IEDriverServer.exe");
				// driver=new InternetExplorerDriver();
				DesiredCapabilities ieCapabilities = DesiredCapabilities
						.internetExplorer();
				ieCapabilities.setCapability("nativeEvents", false);
				ieCapabilities.setCapability("enableNativeEvents", true);
				ieCapabilities.setCapability("requireWindowFocus", true);
				localDriver = new InternetExplorerDriver(ieCapabilities);
			}
			if (browser.equalsIgnoreCase("chrome")) {
				System.out.println(System.getProperty("user.home"));
//				Proxy proxy = new Proxy();
//				proxy.setHttpProxy("rdc-proxy.server.com:8080");
//				proxy.setSslProxy("rdc-proxy.server.com:8080");
//				DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//				capabilities.setCapability("proxy", proxy);
//				ChromeOptions options = new ChromeOptions();
//				
//				capabilities.setCapability(ChromeOptions.CAPABILITY, options);

				System.setProperty("webdriver.chrome.driver",
						file.getAbsolutePath() + "/chromedriver.exe");
				localDriver = new ChromeDriver();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localDriver;
	}
	
	@SuppressWarnings("static-access")
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws Exception {

		try {
			DateFormat df = new SimpleDateFormat("yyyy_MMM_dd_hh_mm_ss");
			Date d = new Date();
			String resultDateTime = df.format(d);
			Reporter.setCurrentTestResult(result);

			if (result.isSuccess()) {
				Reporter.log("Test: "
						+ result.getTestContext().getCurrentXmlTest().getName()
						+ "_" + result.getName(), result.SUCCESS);
				return;
			}

			Reporter.log("Test: "
					+ result.getTestContext().getCurrentXmlTest().getName()
					+ "_" + result.getName(), result.FAILURE);

			File f = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			File outputDir = new File(result.getTestContext()
					.getOutputDirectory());
			File saved;

			String fileName = result.getInstanceName().toString() + "_"
					+ result.getName() + ".png";
			saved = new File(outputDir.getParent() + "\\html", fileName.trim());
			FileUtils.copyFile(f, new File("ScreenShot/"
					+ result.getInstanceName().toString() + "_"
					+ result.getName().toString().trim() + "_" + resultDateTime
					+ ".png"));
			FileUtils.copyFile(f, saved);
			System.setProperty("org.uncommons.reportng.escape-output", "false");

			Reporter.log("<a href=\"" + saved.getName()
					+ "\"><p align=\"left\">Error screenshot at "
					+ resultDateTime + "</p>");
			Reporter.log("<p><img width=\"1024\" src=\"" + saved.getName()
					+ "\" alt=\"screenshot at " + f.getName()
					+ "\"/></p></a><br />");

		} catch (Exception ex) {
			Reporter.log("ERROR: Take snapshot: " + ex.getMessage());
		}
	}

	
	/*public void cookieRead() throws InterruptedException{
		System.setProperty("webdriver.chrome.driver","G:///chromedriver.exe");					
		driver=new ChromeDriver();        
		driver.get("http://demo.guru99.com/test/cookie/selenium_aut.php");
			
        // Input Email id and Password If you are already Register		
		setCredential();
        		
        // create file named Cookies to store Login Information		
        File file = new File("Cookies.data");							
        try		
        {	  
            // Delete old file if exists
			file.delete();		
            file.createNewFile();			
            FileWriter fileWrite = new FileWriter(file);							
            BufferedWriter Bwrite = new BufferedWriter(fileWrite);							
            // loop for getting the cookie information 		
            	
            // loop for getting the cookie information 		
            for(Cookie ck : driver.manage().getCookies())							
            {			
                Bwrite.write((ck.getName()+";"+ck.getValue()+";"+ck.getDomain()+";"+ck.getPath()+";"+ck.getExpiry()+";"+ck.isSecure()));																									
                Bwrite.newLine();             
            }			
            Bwrite.close();			
            fileWrite.close();	
            
        }
        catch(Exception ex)					
        {		
            ex.printStackTrace();			
        }		
    }*/
}
	

