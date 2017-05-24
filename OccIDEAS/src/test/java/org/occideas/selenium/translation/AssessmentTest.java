package org.occideas.selenium.translation;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.occideas.selenium.base.BaseSelenium;
import org.openqa.selenium.By;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssessmentTest  extends BaseSelenium {

	private static final String TEST_AJSM_ASSESSMENT = "testAjsmAssessment";
	private static final String TEST_MODULE_ASSESSMENT = "testModAssessment";

	@BeforeClass
	public static void init() {
		MODULE_NAME = "testValidFiredRules";
		openBrowser();
		driver.get(localhost);
		driver.manage().window().maximize();
	}
	
	@AfterClass
	public static void cleanup() throws InterruptedException {
//		logout();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(oneSeconds);
//		loginAsContdev();
		selectModuleTab();
		deleteModule(TEST_MODULE_ASSESSMENT);
		deleteAjsm(TEST_AJSM_ASSESSMENT);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		logout();
		driver.quit();
	}
	
	private static void deleteAjsm(String testAjsmAssessment) throws InterruptedException {
		selectFragmentsTab();
		waitTillRendered(oneSeconds);
		driver.findElementByCssSelector("#delete_F_ajsm").click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//*[@id='tab-content-2']/div/div/ng-view/div/div/table/thead/tr[3]/th[1]/div/input")
		.sendKeys(TEST_AJSM_ASSESSMENT);
		waitTillRendered(twoSeconds);
		driver.findElementByCssSelector("#delete_"+testAjsmAssessment).click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("/html/body/div[4]/div/div/div[3]/button[1]").click();
		waitTillRendered(oneSeconds);
	}

	@Test
	public void testValidFiredRules() throws InterruptedException{
		System.out.println("Starting test " + new Object() {
		}.getClass().getEnclosingMethod().getName());
		Thread.sleep(oneSeconds);
		loginAsContdev();
		createAJSM(TEST_AJSM_ASSESSMENT,TEST_AJSM_ASSESSMENT);
		createModule(TEST_MODULE_ASSESSMENT);
	}

	private void createModule(String testModuleAssessment) throws InterruptedException {
		selectModuleTab();
		waitTillRendered(twoSeconds);
		driver.findElement(
				By.cssSelector("#add_M_Module"))
				.click();
		waitTillRendered(oneSeconds);
		driver.findElementByXPath("//*[@id='id_']/div/div/input").clear();
		driver.findElementByXPath("//*[@id='id_']/div/div/input").sendKeys(testModuleAssessment);
		driver.findElementByXPath("//*[@id='tab-content-1']/div/div/ng-view/div/div/table/tbody[2]/tr[2]/td[3]/div/div/input").clear();
		driver.findElementByXPath("//*[@id='tab-content-1']/div/div/ng-view/div/div/table/tbody[2]/tr[2]/td[3]/div/div/input").sendKeys(TEST_MODULE_ASSESSMENT);
		driver.findElementByXPath("//*[@id='saveBtn']").click();
	}

	private void createAJSM(String name,String description) throws InterruptedException {
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
		
	}

	private static void selectFragmentsTab() throws InterruptedException {
		waitTillRendered(oneSeconds);
		driver.findElement(
				By.xpath(
		"/html/body/div[2]/div/div/md-content/md-tabs/md-tabs-wrapper/md-tabs-canvas/md-pagination-wrapper/md-tab-item[2]"))
				.click();
	}
	
	
}
