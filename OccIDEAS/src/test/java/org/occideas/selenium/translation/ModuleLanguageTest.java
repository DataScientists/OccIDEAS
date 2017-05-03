package org.occideas.selenium.translation;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.occideas.selenium.base.BaseSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//@Ignore
public class ModuleLanguageTest extends BaseSelenium{
	private static final String MODULE_NAME = "test123";
	
	@BeforeClass
	public static void init(){
		openBrowser();
	}
	
	@AfterClass
	public static void cleanup(){
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		logout();
		driver.close();
	}
	
	@Test
	public void valid_enableLanguageTranslation(){
		System.out.println("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
		driver.get(localhost);
		loginAsAdmin();
		enableLanguageTranslation();
		checkLanguageSummaryTabIsOpen();
		System.out.println("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName()); 
	}

	private void checkLanguageSummaryTabIsOpen() {
		WebElement languageSummaryTab = driver.findElement(By.xpath("/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[8]"));
		Assert.assertNotNull(languageSummaryTab);
	}

	private void enableLanguageTranslation() {
		clickMenubutton();
		WebElement element = (new WebDriverWait(driver, 15))
				   .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='menu_container_0']/md-menu-content/md-menu-item[1]/button")));
		element.click();
		driver.findElement(By.xpath("//*[@id='dialogContent_8']/div/md-content/div/md-checkbox/div[1]")).click();
		driver.findElement(By.xpath("/html/body/div[5]/md-dialog/form/md-dialog-actions/button[1]")).click();
		
	}

	@Ignore
	public void valid_arabicTranslation(){
		 System.out.println("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
		 driver.get(localhost);	
	     loginAsAdmin();
	     selectModuleTab();
	     addIntroModule();
//	     try{
//			 element = driver.findElement (By.xpath(".//*[@id='account_logout']/a"));
//		 }catch (Exception e){
//			}
//	     Assert.assertNotNull(element);
	     System.out.println("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
	}

	private void addIntroModule() {
		driver.findElement(By.xpath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[1]/td/div/button[1]")).click();
		driver.findElement(By.xpath("//*[@id='id_']/div/div/input")).clear();
		driver.findElement(By.xpath("//*[@id='id_']/div/div/input")).sendKeys(MODULE_NAME);
	    driver.findElement(By.xpath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[3]/div/div/input")).clear();
	    driver.findElement(By.xpath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[3]/div/div/input")).sendKeys(MODULE_NAME);
	    driver.findElement(By.xpath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[5]/button[1]")).click();
	}

	private void loginAsAdmin() {
		driver.findElement(By.xpath("//*[@id='lg_username']")).sendKeys("admin");
	     driver.findElement(By.xpath("//*[@id='lg_password']")).sendKeys("admin");
	     driver.findElement(By.xpath("//*[@id='login-form']/div[2]/button")).click();
	}

}
