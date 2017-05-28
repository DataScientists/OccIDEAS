package org.occideas.selenium.base;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
	
	protected void createIntroModule(String moduleName) throws InterruptedException {
		waitTillRendered(oneSeconds);
		driver.findElement(
				By.cssSelector("#add_M_IntroModule"))
				.click();
		waitTillRendered(oneSeconds);
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
		driver.findElement(By.xpath("//*[@id='id_']/div/div/input")).sendKeys(moduleName);
		driver.findElement(
				By.xpath("//*[@id='id_']/div/div/input"))
				.clear();
		driver.findElement(
				By.xpath("//*[@id='id_']/div/div/input"))
				.sendKeys(moduleName);
		driver.findElement(
				By.cssSelector("#saveBtn"))
				.click();
		waitTillRendered(oneSeconds);
		driver.findElement(By.cssSelector("#id_"+moduleName+"_openmodule")).click();
		waitTillRendered(oneSeconds);
		Actions action= new Actions(driver);
		action.contextClick(driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content M')]"))).build().perform();
		waitTillRendered(oneSeconds);
		driver.findElement(By.cssSelector("body > div.dropdown.clearfix > ul > li:nth-child(2) > a")).click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/span").click();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").clear();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").sendKeys("test_intromodule_question");
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").sendKeys(Keys.RETURN);
		waitTillRendered(oneSeconds);
		action.contextClick(driver.findElement(
				By.xpath("//div[contains(@class, 'tree-node-content Q')]"))).build().perform();
		waitTillRendered(oneSeconds);
		driver.findElement(By.cssSelector("body > div.dropdown.clearfix > ul > li:nth-child(1) > a")).click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/span").click();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").clear();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").sendKeys("test_intromodule_answer");
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").sendKeys(Keys.RETURN);
		driver.findElementByCssSelector("md-tab-item .glyphicon-remove").click();
		
	}
	
	protected void createModule(String testModuleAssessment) throws InterruptedException {
		selectModuleTab();
		waitTillRendered(twoSeconds);
		driver.findElement(
				By.cssSelector("#add_M_Module"))
		.click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//*[@id='id_']/div/div/input").clear();
		driver.findElementByXPath("//*[@id='id_']/div/div/input").sendKeys(testModuleAssessment);
		driver.findElementByXPath("//*[@id='tab-content-1']/div/div/ng-view/div/div/table/tbody[2]/tr[2]/td[3]/div/div/input").clear();
		driver.findElementByXPath("//*[@id='tab-content-1']/div/div/ng-view/div/div/table/tbody[2]/tr[2]/td[3]/div/div/input").sendKeys(testModuleAssessment);
		driver.findElementByXPath("//*[@id='saveBtn']").click();
		waitTillRendered(oneSeconds);
		driver.findElement(By.cssSelector("#id_"+testModuleAssessment+"_openmodule")).click();
		waitTillRendered(oneSeconds);
		Actions action= new Actions(driver);
		action.contextClick(driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content M')]"))).build().perform();
		waitTillRendered(oneSeconds);
		driver.findElement(By.cssSelector("body > div.dropdown.clearfix > ul > li:nth-child(2) > a")).click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/span").click();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").clear();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").sendKeys("test_module_question");
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").sendKeys(Keys.RETURN);
		waitTillRendered(oneSeconds);
		action.contextClick(driver.findElement(
				By.xpath("//div[contains(@class, 'tree-node-content Q')]"))).build().perform();
		waitTillRendered(oneSeconds);
		driver.findElement(By.cssSelector("body > div.dropdown.clearfix > ul > li:nth-child(1) > a")).click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/span").click();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").clear();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").sendKeys("test_module_answer");
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").sendKeys(Keys.RETURN);
		driver.findElementByCssSelector("md-tab-item .glyphicon-remove").click();
	}
	
	protected void createAJSM(String name,String description) throws InterruptedException {
		selectFragmentsTab();
		waitTillRendered(oneSeconds);
		driver.findElementByCssSelector("#add_F_ajsm").click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[1]/div/div/input")
		.clear();
		driver.findElementByXPath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[1]/div/div/input")
		.sendKeys(name);
		driver.findElementByXPath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[2]/div/div/input")
		.clear();
		driver.findElementByXPath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[2]/div/div/input")
		.sendKeys(description);
		driver.findElementByXPath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[4]/button[1]")
		.click();
		waitTillRendered(oneSeconds);
		driver.findElementByCssSelector("#addFragment_"+name).click();
		waitTillRendered(oneSeconds);
		Actions action= new Actions(driver);
		action.contextClick(driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content F')]"))).build().perform();
		waitTillRendered(oneSeconds);
		driver.findElement(By.cssSelector("body > div.dropdown.clearfix > ul > li:nth-child(2) > a")).click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/span").click();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").clear();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").sendKeys("test_ajsm_question");
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content Q')]/div/input").sendKeys(Keys.RETURN);
		waitTillRendered(oneSeconds);
		action.contextClick(driver.findElement(
				By.xpath("//div[contains(@class, 'tree-node-content Q')]"))).build().perform();
		waitTillRendered(oneSeconds);
		driver.findElement(By.cssSelector("body > div.dropdown.clearfix > ul > li:nth-child(1) > a")).click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/span").click();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").clear();
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").sendKeys("test_ajsm_answer");
		driver.findElementByXPath("//div[contains(@class, 'tree-node-content P')]/div/input").sendKeys(Keys.RETURN);
		driver.findElementByCssSelector("md-tab-item .glyphicon-remove").click();
	}
	
	
	protected static void selectFragmentsTab() throws InterruptedException {
		waitTillRendered(oneSeconds);
		driver.findElement(
				By.xpath(
						"/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[2]"))
		.click();
	}
	
	protected static void deleteAjsm(String testAjsmAssessment) throws InterruptedException {
		selectFragmentsTab();
		waitTillRendered(oneSeconds);
		driver.findElementByCssSelector("#delete_F_ajsm").click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/thead/tr[3]/th[1]/div/input")
		.sendKeys(testAjsmAssessment);
		waitTillRendered(twoSeconds);
		driver.findElementByCssSelector("#delete_"+testAjsmAssessment).click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("/html/body/div[4]/div/div/div[3]/button[1]").click();
		waitTillRendered(oneSeconds);
	}
	
	protected static void waitTillRendered(int seconds) throws InterruptedException{
		Thread.sleep(seconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
}
