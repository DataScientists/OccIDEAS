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
	
	protected static void logout() {
		clickMenubutton();
		driver.findElement(By.xpath("//*[@id='menu_container_0']/md-menu-content/md-menu-item[3]/a")).click();
	}
	
	protected static void clickMenubutton() {
		driver.findElement(By.cssSelector("body > div.container > header > div > div > ul > li > md-menu > button")).click();
	}
	
	protected void selectModuleTab() {
		driver.findElement(By.xpath("/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[2]")).click();
	}
	
}
