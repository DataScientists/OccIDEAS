package org.occideas.selenium.translation;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.occideas.selenium.base.BaseSelenium;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FiredRulesTest  extends BaseSelenium {

	private static final String MODULE_NAME = "test_fired_rules_set_participant_status";
	
	@BeforeClass
	public static void init() {
		openBrowser();
		driver.get(localhost);
		driver.manage().window().maximize();
		loginAsContdev();
	}

	@AfterClass
	public static void cleanup() throws InterruptedException {
		logout();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Thread.sleep(oneSeconds);
		loginAsContdev();
		selectModuleTab();
		deleteModule(MODULE_NAME);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		logout();
		driver.quit();
	}
	
	@Test
	public void testSetParticipantStatusInFiredRulesTab() throws InterruptedException{
		System.out.println("Starting test " + new Object() {
		}.getClass().getEnclosingMethod().getName());
		Thread.sleep(oneSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		selectModuleTab();
		createIntroModule(MODULE_NAME);
		Thread.sleep(oneSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		logout();
		Thread.sleep(oneSeconds);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		loginAsAssessor();
		System.out.println("Ending test " + new Object() {
		}.getClass().getEnclosingMethod().getName());
	}
	
}
