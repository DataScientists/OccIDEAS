package org.occideas.selenium.translation;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@Ignore
public class ModuleLanguageTest {
	private static ChromeDriver driver;
 	WebElement element;
	
	@BeforeClass
	public static void openBrowser(){
		URL url = ModuleLanguageTest.class.getResource("chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", "/src/main/resources/chromedriver.exe");
		driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@Test
	public void valid_arabicTranslation(){
		 System.out.println("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
		 driver.get("http://localhost:8080/occideas/");	
	     driver.findElement(By.xpath("//*[@id='lg_username']")).sendKeys("admin");
	     driver.findElement(By.xpath("//*[@id='lg_password']")).sendKeys("admin");
	     driver.findElement(By.xpath("//*[@id='login-form']/div[2]/button")).click();
//	     try{
//			 element = driver.findElement (By.xpath(".//*[@id='account_logout']/a"));
//		 }catch (Exception e){
//			}
//	     Assert.assertNotNull(element);
	     System.out.println("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
	}
}
