package com.selenium.AssignmentL2_4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

/*Assignment 4: KSRTC Login functionality and forgot password functionality verification with a user who is not registered with KSTRC. 
Follow the Page Object Model Design pattern and use Data drivern testing */
public class AssignmentFour {

	// Constructor to read config file
	private AssignmentFour() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(propertyFilePath));
			properties = new Properties();
			try {
				properties.load(reader);
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
		}
	}

	// POM for finding the elements by name, id, xpath
	private By phoneNumber = By.xpath("//div[contains(text(), '080-26252625')]");
	private By signIn = By.xpath("//div[contains(text(), 'Commuter')]");
	private By actualEmailId = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::label)[1]");
	private By actualMobileNumber = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::label)[3]");
	private By actualPassword = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::label)[4]");
	private By emailInput = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::input)[1]");
	private By mobileNumberInput = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::input)[3]");
	private By passwordInput = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::input)[4]");
	private By agreeCheckbox = By.xpath("//div[@class='flex tnc-block']/child::div[1]");
	private By loginButton = By.xpath("(//*[text()='Login'])[3]");
	private By errorMessage = By.xpath("//*[text()='OK']/parent::*");
	private By okButton = By.id("okayButton");
	private By forgetPassword = By.xpath("//*[text()='Forgot Password?']");

	private WebDriver driver;
	private String expectedPhoneNumber = "080-26252625";
	private String[] expectedLogin = { "Email ID", "Mobile Number", "Password" };

	private Properties properties;
	private String url;
	private String propertyFilePath = "config.properties";

	@BeforeTest
	public void launchURL() {
		// Step1: Launch URL with Chrome Browser
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		url = readProperty("url");
		driver.get(url);
	}

	// Data provider
		@DataProvider(name = "data")
		public Object[][] dataProviderMethod() throws IOException {
			Object[][] data = util.readFromExcel("data");
			return data;
		}
	
	@Test(dataProvider = "data")
	public void ksrtc(String email, String phone, String pwd) throws InterruptedException {
		driver.manage().window().maximize();
		getWait(phoneNumber);
		// Step1: Verify the phone number 080-26252625 present in the main page
		String welcomeToKSRTC = driver.findElement(phoneNumber).getText().trim();
		String actualPhoneNumber = welcomeToKSRTC.replaceAll("[^0-9-]", "").trim();
		Assert.assertEquals(actualPhoneNumber, expectedPhoneNumber);

		// Step2: Click on Sign In and Verify Login form with User Name and Password is
		// displayed
		getClick(signIn);
		Assert.assertEquals(driver.findElement(actualEmailId).getText().trim(), expectedLogin[0]);
		Assert.assertEquals(driver.findElement(actualMobileNumber).getText().trim(), expectedLogin[1]);
		Assert.assertEquals(driver.findElement(actualPassword).getText().trim(), expectedLogin[2]);
		System.out.println("UserID: " + email + " " + phone + " " + pwd);

		// Step3: Fill the User Name and Password
		driver.findElement(emailInput).sendKeys(email);
		driver.findElement(mobileNumberInput).sendKeys(phone);
		driver.findElement(passwordInput).sendKeys(pwd);
		getClick(agreeCheckbox);

		// Step4: Click on LOGIN button and verify the error message
		getClick(loginButton);
		String actualErrorMessage = getText(errorMessage);
		Assert.assertEquals(actualErrorMessage, getText(errorMessage));
		util.screenshot(driver);
		getClick(okButton);

		// Step5: Click on Forgot Password and verify the error message
		getClick(forgetPassword);

		// Step6: Verify the error message
		actualErrorMessage = getText(errorMessage);
		Assert.assertEquals(actualErrorMessage, getText(errorMessage));
		util.screenshot(driver);
		getClick(okButton);
	}

	@AfterTest
	public void tearDownURL() {
		driver.quit();
	}

	// Function to return value from properties file
	public String readProperty(String input) {
		return properties.getProperty(input);
	}

	// Function to click element
	public void getClick(By element) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		getWait(element);
		if (driver.findElement(element).isEnabled()) {
			driver.findElement(element).click();
		}
	}

	// Explicit wait
	public void getWait(By element) {
		WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
		driverWait.until(ExpectedConditions.elementToBeClickable(driver.findElement(element)));
	}

	// Function to Return Text
	public String getText(By element) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		getWait(element);
		String text = driver.findElement(element).getText().trim();
		return text;
	}
}
