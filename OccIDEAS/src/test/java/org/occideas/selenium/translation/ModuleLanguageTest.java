package org.occideas.selenium.translation;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.occideas.selenium.base.BaseSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModuleLanguageTest extends BaseSelenium {
	private static final String EDIT_ARABIC = "اسمى تانجا";

	@BeforeClass
	public static void init() {
		MODULE_NAME = "selenium_test_language";
		openBrowser();
		driver.get(localhost);
		driver.manage().window().maximize();
		loginAsContdev();
	}

	@AfterClass
	public static void cleanup() throws InterruptedException {
		selectModuleTab();
		deleteModule(MODULE_NAME);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		logout();
		driver.quit();
	}

	@Test
	public void valid1_arabicTranslation() throws InterruptedException {
		System.out.println("Starting test " + new Object() {
		}.getClass().getEnclosingMethod().getName());
		Thread.sleep(oneSeconds);
		selectModuleTab();
		addIntroModule();
		enableLanguageTranslation();
		selectModuleTab();
		selectLanguageFlagBtn();
		selectArabicLanguageFromDialog();
		selectLanguageSummaryTab();
		System.out.println("Ending test " + new Object() {
		}.getClass().getEnclosingMethod().getName());
	}
	
//	@Test
//	public void valid2_enableLanguageTranslation() throws InterruptedException {
//		System.out.println("Starting test " + new Object() {
//		}.getClass().getEnclosingMethod().getName());
//		enableLanguageTranslation();
//		checkLanguageSummaryTabIsOpen();
//		System.out.println("Ending test " + new Object() {
//		}.getClass().getEnclosingMethod().getName());
//	}
	
	private void selectLanguageSummaryTab() throws InterruptedException{
		driver.findElement(By.xpath("/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[5]")).click();
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//*[@id='tab-content-6']/div/div/ng-view/div/div[1]/table/tbody/tr[3]/td[2]/a")).click();
		
		Thread.sleep(twoSeconds);
		String countStr = driver.findElement(By.cssSelector("#count_"+MODULE_NAME)).getText();
		Assert.assertTrue("2/2".equals(countStr));
	}

	private void selectArabicLanguageFromDialog() throws InterruptedException {
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector("#selectLanguageId")).click();
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//*[@id='ui-select-choices-row-0-1']/div/div")).click();
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("/html/body/div[5]/md-dialog/form/md-dialog-actions/button[1]")).click();
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content Q')]/span/div[2]/input")).click();
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content Q')]/span/div[2]/input")).clear();
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content Q')]/span/div[2]/input")).sendKeys("مرحبا");
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content P')]/span/div[2]/input")).click();
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content P')]/span/div[2]/input")).clear();
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content P')]/span/div[2]/input")).sendKeys("أنا بخير, شكرا");
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content M')]/span/input")).click();
		String translateSummaryString = driver.findElement(By.cssSelector("#translatedSummary")).getText();
		Thread.sleep(oneSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Assert.assertTrue("Translated 2/2".equals(translateSummaryString));
		driver.findElement(By.cssSelector("#editTranslationId")).click();
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content P')]/span/div[2]/input")).clear();
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content P')]/span/div[2]/input")).sendKeys(EDIT_ARABIC);
		driver.findElement(By.xpath("//div[contains(@class, 'tree-node-content M')]/span/input")).click();
	}
	
	private void selectLanguageFlagBtn() throws InterruptedException {
		Thread.sleep(twoSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector("#id_"+MODULE_NAME+"_flag")).click();
	}

	private void checkLanguageSummaryTabIsOpen() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebElement languageSummaryTab = driver.findElement(By.xpath(
				"/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[4]"));
		Assert.assertNotNull(languageSummaryTab);
	}

	private void enableLanguageTranslation() throws InterruptedException {
		clickMenubutton();
		Thread.sleep(twoSeconds);
		driver.findElement(By.id("languageId")).click();
		Thread.sleep(twoSeconds);
		driver.findElement(By.cssSelector("#enableTranslation > div.md-container.md-ink-ripple")).click();
		driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		driver.findElement(By.xpath("/html/body/div[5]/md-dialog/form/md-dialog-actions/button[1]")).click();

	}
	
}
