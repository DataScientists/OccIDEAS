package org.occideas.selenium.translation;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.occideas.selenium.base.BaseSelenium;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssessmentTest  extends BaseSelenium {

	private static final String TEST_AJSM_ASSESSMENT = "testAjsmAssessment";
	private static final String TEST_MODULE_ASSESSMENT = "testModAssessment";
	private static final String TEST_INTRO_MODULE_ASSESSMENT = "testIntroModAssessment";

	@BeforeClass
	public static void init() {
		openBrowser();
		driver.get(localhost);
		driver.manage().window().maximize();
	}
	
	@Test
	public void testValidFiredRules() throws InterruptedException{
		System.out.println("Starting test " + new Object() {
		}.getClass().getEnclosingMethod().getName());
		Thread.sleep(oneSeconds);
		loginAsContdev();
		createAJSM(TEST_AJSM_ASSESSMENT,TEST_AJSM_ASSESSMENT);
		createModule(TEST_MODULE_ASSESSMENT);
		createIntroModule(TEST_INTRO_MODULE_ASSESSMENT);
		linkAJSMtoModule(TEST_INTRO_MODULE_ASSESSMENT,TEST_AJSM_ASSESSMENT,TEST_AJSM_ASSESSMENT);
		linkModuletoIntroModule(TEST_INTRO_MODULE_ASSESSMENT,TEST_MODULE_ASSESSMENT);
	}
	
	
	private void linkModuletoIntroModule(String testIntroModuleAssessment, String testModuleAssessment) {
		// TODO Auto-generated method stub
		
	}

	private void linkAJSMtoModule(String testIntroModuleAssessment, String testAjsmAssessment,
			String testAjsmAssessment2) {
			
	}

	@AfterClass
	public static void cleanup() throws InterruptedException {
//		logout();
		waitTillRendered(oneSeconds);
//		loginAsContdev();
		selectModuleTab();
		deleteModule(TEST_MODULE_ASSESSMENT);
		deleteAjsm(TEST_AJSM_ASSESSMENT);
		deleteModule(TEST_INTRO_MODULE_ASSESSMENT);
		waitTillRendered(oneSeconds);
		logout();
		driver.quit();
	}
	
}
