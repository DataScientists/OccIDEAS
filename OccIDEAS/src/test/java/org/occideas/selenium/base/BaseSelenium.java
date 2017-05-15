package org.occideas.selenium.base;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseSelenium {

	protected static ChromeDriver driver;
	protected static String localhost = "http://localhost:8080/occideas/";
	private String usernameAdmin = "admin";
	private String passwordAdmin = "admin";
	private static final String usernameContdev = "contdev";
	private static final String passwordContdev = "vedtnoc";
	protected static final int twoSeconds = 2000;
	protected static final int threeSeconds = 3000;

	
	protected void loginAsAdmin() {
		driver.findElement(By.xpath("//*[@id='lg_username']")).sendKeys(usernameAdmin);
		driver.findElement(By.xpath("//*[@id='lg_password']")).sendKeys(passwordAdmin);
		driver.findElement(By.xpath("//*[@id='login-form']/div[2]/button")).click();
	}
	
	protected static void loginAsContdev() {
		driver.findElement(By.xpath("//*[@id='lg_username']")).sendKeys(usernameContdev);
		driver.findElement(By.xpath("//*[@id='lg_password']")).sendKeys(passwordContdev);
		driver.findElement(By.xpath("//*[@id='login-form']/div[2]/button")).click();
	}
	
	protected static void openBrowser(){
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		URL path = null;
		if(SystemUtils.IS_OS_WINDOWS){
			path = loader.getResource("chromedriver.exe");
		}else{
			path = loader.getResource("chromedriver");
		}
		System.setProperty("webdriver.chrome.driver",path.getPath());
		driver = new ChromeDriver(setChromeOptions());
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	private static ChromeOptions setChromeOptions() {
		ChromeOptions copts = new ChromeOptions();
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		copts.setExperimentalOption("prefs", prefs);
		return copts;
	}
	
	protected static void logout() throws InterruptedException {
		clickMenubutton();
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//*[@id='menu_container_0']/md-menu-content/md-menu-item[3]/a")).click();
	}
	
	protected static void clickMenubutton() {
		driver.findElement(By.cssSelector("body > div.container > header > div > div > ul > li > md-menu > button")).click();
	}
	
	protected static void selectModuleTab() throws InterruptedException {
		if(driver.findElements(By.xpath("/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-prev-button")).size() > 0){
			driver.findElement(By.xpath("/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-prev-button")).click();
			Thread.sleep(twoSeconds);
		}
		driver.findElement(By.xpath("/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[1]")).click();
	}
	
}
