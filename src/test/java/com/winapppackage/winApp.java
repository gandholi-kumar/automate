package com.winapppackage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

public class winApp {

	private static WindowsDriver<?> driver = null;

	@BeforeClass
	public void setUp() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("app", "C:\\Users\\User\\Desktop\\publish\\WindowsAppForm.exe");
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("deviceName", "WindowsPC");
		try {
			driver = new WindowsDriver<>(new URL("http://127.0.0.1:4723"), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Test
	public void testFillFormAndHandlePopup() {
		Random random = new Random();

		// Generate random first and last names
		String[] firstNames = { "John", "Jane", "Alex", "Chris", "Sam", "Taylor", "Jordan", "Morgan" };
		String[] lastNames = { "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis" };
		String randomFirstName = firstNames[random.nextInt(firstNames.length)];
		String randomLastName = lastNames[random.nextInt(lastNames.length)];

		// Generate random DOB (between 1980-01-01 and 2005-12-31)
		int startYear = 1980;
		int endYear = 2005;
		int year = random.nextInt(endYear - startYear + 1) + startYear;
		int month = random.nextInt(12) + 1;
		int day = random.nextInt(28) + 1; // To avoid invalid dates
		LocalDate dob = LocalDate.of(year, month, day);
		String dobString = dob.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

		// Randomly select gender
		boolean isMale = random.nextBoolean();

		// Randomly select interests
		boolean selectMusic = random.nextBoolean();
		boolean selectReading = random.nextBoolean();
		if (!selectMusic && !selectReading) {
			// Ensure at least one interest is selected
			selectMusic = true;
		}

		// Fill First Name
		WindowsElement firstName = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("textBoxFirstName"));
		firstName.clear();
		firstName.sendKeys(randomFirstName);

		// Fill Last Name
		WindowsElement lastName = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("textBoxLastName"));
		lastName.clear();
		lastName.sendKeys(randomLastName);

		// Select DOB using calendar
		WindowsElement dobPicker = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("dateTimePickerDOB"));
		dobPicker.click();
		dobPicker.sendKeys(dobString);

		// Select Gender (e.g., RadioButton)
		if (isMale) {
			WindowsElement genderMale = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("radioButtonMale"));
			genderMale.click();
		} else {
			WindowsElement genderFemale = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("radioButtonFemale"));
			genderFemale.click();
		}

		// Select Interests (e.g., CheckBox)
		if (selectMusic) {
			WindowsElement music = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("checkBoxMusic"));
			music.click();
		}
		if (selectReading) {
			WindowsElement reading = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("checkBoxReading"));
			reading.click();
		}

		// Click Submit
		WindowsElement submitButton = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("buttonSubmit"));
		submitButton.click();

		// Handle popup: Click Clear
		WindowsElement clearButton = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("buttonCancel"));
		clearButton.click();
	}

	@Test(dependsOnMethods = { "testFillFormAndHandlePopup" })
	public void testClickFirstRowIfExists() {
		try {
			WindowsElement firstRowButton = (WindowsElement) driver
					.findElement(MobileBy.xpath("//Button[@Name=' Row 0']"));
			firstRowButton.click();

			// Handle popup: Click Clear
			WindowsElement clearButton = (WindowsElement) driver.findElement(MobileBy.AccessibilityId("buttonCancel"));
			clearButton.click();
		} catch (Exception e) {
			System.out.println("No rows found in the table.");
		}
	}

	@Test(dependsOnMethods = { "testClickFirstRowIfExists" })
	public void testDeleteFirstRowIfExists() {
		try {
			WindowsElement firstRowImage = (WindowsElement) driver
					.findElement(MobileBy.xpath("//Image[@Name=' Row 0']"));
			firstRowImage.click();
		} catch (Exception e) {
			System.out.println("No rows found in the table to delete.");
		}
	}

	@AfterSuite
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}