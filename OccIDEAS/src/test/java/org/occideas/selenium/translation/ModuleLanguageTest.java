package org.occideas.selenium.translation;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

//@Ignore
public class ModuleLanguageTest {
	private static final String MODULE_NAME = "test123";
	private static ChromeDriver driver;
 	WebElement element;
	
	@BeforeClass
	public static void openBrowser(){
		ClassLoader loader = ClassLoader.getSystemClassLoader();
        URL path = loader.getResource("chromedriver.exe");
		System.setProperty("webdriver.chrome.driver",path.getPath());
		driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void valid_arabicTranslation(){
		 System.out.println("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
		 driver.get("http://localhost:8080/occideas/");	
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
		driver.findElement(By.xpath("//*[@id='id_']/div/div/input")).sendKeys(MODULE_NAME);
	     driver.findElement(By.xpath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[3]/div/div/input")).sendKeys(MODULE_NAME);
	     driver.findElement(By.xpath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/tbody[1]/tr[2]/td[5]/button[1]")).click();
	}

	private void loginAsAdmin() {
		driver.findElement(By.xpath("//*[@id='lg_username']")).sendKeys("admin");
	     driver.findElement(By.xpath("//*[@id='lg_password']")).sendKeys("admin");
	     driver.findElement(By.xpath("//*[@id='login-form']/div[2]/button")).click();
	}

	private void selectModuleTab() {
		driver.findElement(By.xpath("/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[2]")).click();
	}
}
