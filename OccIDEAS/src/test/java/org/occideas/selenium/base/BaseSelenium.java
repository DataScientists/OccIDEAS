package org.occideas.selenium.base;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public abstract class BaseSelenium {

	protected static ChromeDriver driver;
	protected static String localhost = "http://localhost:8080/occideas/";
	private String usernameAdmin = "admin";
	private String passwordAdmin = "admin";
	private static final String usernameContdev = "contdev";
	private static final String passwordContdev = "vedtnoc";
	private static final String usernameAssessor = "assessor";
	private static final String passwordAssessor = "rossessa";
	protected static final int oneSeconds = 1000;
	protected static final int twoSeconds = 2000;
	protected static final int threeSeconds = 3000;
	protected static String MODULE_NAME = null;

	protected static void loginAsAssessor() {
		driver.findElement(By.xpath("//*[@id='lg_username']")).sendKeys(usernameAssessor);
		driver.findElement(By.xpath("//*[@id='lg_password']")).sendKeys(passwordAssessor);
		driver.findElement(By.xpath("//*[@id='login-form']/div[2]/button")).click();
	}
	
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
	
	protected static void deleteModule(String modulename) throws InterruptedException {
		driver.findElementByXPath("//*[@id='tab-content-1']/div/div/ng-view/div/div/table/thead/tr[3]/th[2]/div/input")
		.sendKeys(modulename);
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(
				By.cssSelector("#toggleDeleteModuleId"))
				.click();
		Thread.sleep(threeSeconds);
		WebElement inputElement = driver.findElement(
				By.cssSelector("#id_"+modulename+"_delete"));
		if(inputElement != null){
			inputElement.click();
			Thread.sleep(twoSeconds);
			driver.findElement(By.cssSelector("div.modal-footer > button.btn.btn-primary")).click();
			Thread.sleep(twoSeconds);
		}
	}
	
	protected void addIntroModule() throws InterruptedException {
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.findElement(
				By.cssSelector("#addModule"))
				.click();
		Thread.sleep(threeSeconds);
		WebElement inputElement = driver.findElement(
				By.xpath("//*[@id='id_']/div/div/input"));
		if(inputElement == null){
			System.out.println("inputElement is null");
			driver.findElement(
					By.cssSelector("#addModule"))
					.click();
			Thread.sleep(threeSeconds);
		}
		driver.findElement(By.xpath("//*[@id='id_']/div/div/input")).clear();
		driver.findElement(By.xpath("//*[@id='id_']/div/div/input")).sendKeys(MODULE_NAME);
		driver.findElement(
				By.xpath("//*[@id='id_']/div/div/input"))
				.clear();
		driver.findElement(
				By.xpath("//*[@id='id_']/div/div/input"))
				.sendKeys(MODULE_NAME);
		driver.findElement(
				By.cssSelector("#saveBtn"))
				.click();
		Thread.sleep(twoSeconds);
		driver.findElement(By.cssSelector("#id_"+MODULE_NAME+"_openmodule")).click();
		Thread.sleep(twoSeconds);
		Actions action= new Actions(driver);
		action.contextClick(driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content M')]"))).build().perform();
		Thread.sleep(twoSeconds);
		driver.findElement(By.cssSelector("body > div.dropdown.clearfix > ul > li:nth-child(2) > a")).click();
		Thread.sleep(twoSeconds);
		action.contextClick(driver.findElement(
				By.xpath("//div[contains(@class, 'tree-node-content Q')]"))).build().perform();
		Thread.sleep(twoSeconds);
		driver.findElement(By.cssSelector("body > div.dropdown.clearfix > ul > li:nth-child(1) > a")).click();
		
	}
	
	protected static void waitTillRendered(int seconds) throws InterruptedException{
		Thread.sleep(seconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
}
