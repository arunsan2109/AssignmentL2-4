 package com.selenium.AssignmentL2_4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
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
	private AssignmentFour(){
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
	private By emailId = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::label)[1]");
	private By mobileNumber = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::label)[3]");
	private By password = By.xpath("(//div[@class='payee-info-inputs']/child::div/child::label)[4]");

	private WebDriver driver;
	private String expectedPhoneNumber = "080-26252625";
	private String[] expectedLogin = {"Email ID", "Mobile Number", "Password"};
	
	private Properties properties;
	private String url;
	private String propertyFilePath= "config.properties";

	@BeforeTest
	public void launchURL() {
		// Step1: Launch URL with Chrome Browser
		WebDriverManager.edgedriver().setup();
		driver = new EdgeDriver();
		url = properties.getProperty("url");
		driver.get(url);
	}
	
	
	@DataProvider(name="data-provider")
	public Object[][] dataProviderMethod(){
		return new Object[][] {
			{"email@gmail.com", "7989985522", "pwd"}
		};
	}

	@Test(dataProvider="data-provider")
	public void ksrtc(String email, String phone, String pwd) throws InterruptedException {
		driver.manage().window().maximize();
		getWait(phoneNumber);
		// Step1: Verify the phone number 080-26252625 present in the main page
		String welcomeToKSRTC = driver.findElement(phoneNumber).getText().trim();
		String actualPhoneNumber = welcomeToKSRTC.replaceAll("[^0-9-]", "").trim();	
		Assert.assertEquals(actualPhoneNumber, expectedPhoneNumber);
		
		// Step2: Click on Sign In and Verify Login form with User Name and Password is displayed
		getClick(signIn);
		Assert.assertEquals(driver.findElement(emailId).getText().trim(), expectedLogin[0]);
		Assert.assertEquals(driver.findElement(mobileNumber).getText().trim(), expectedLogin[1]);
		Assert.assertEquals(driver.findElement(password).getText().trim(), expectedLogin[2]);
		System.out.println("UserID: " +email +" " +phone +" " +pwd);
		Thread.sleep(5000);
	}

	@AfterTest
	public void tearDownURL() {
		driver.quit();
	}
	

	// Function to click element
	public void getClick(By element) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		getWait(element);
		if (driver.findElement(element).isEnabled()) {
			driver.findElement(element).click();
		}
	}

	// Explicit wait
	public void getWait(By element) {
		WebDriverWait driverWait = new WebDriverWait(driver, 20);
		driverWait.until(ExpectedConditions.elementToBeClickable(driver.findElement(element)));
	}

	// Function to Return Text
	public String getText(By element) {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		getWait(element);
		String text = driver.findElement(element).getText().trim();
		return text;
	}
}
