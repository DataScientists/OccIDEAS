package org.occideas.utilities;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonUtilTest {

	@Test
	public void test(){
		
		DecimalFormat df = new DecimalFormat("#.0");
		System.out.println(df.format(100.0));
		System.out.println(df.format(100.49212));
		
		int questionSize = 2500; //60 mins
		long elapsedTime = 30000; //half minute
		int count = 100; // 100 qs in 30 secs
		
		float msPerInterview = (float) (elapsedTime/count);
		System.out.println("msPerInterview " + msPerInterview);
		double estDuration = questionSize * msPerInterview;
		System.out.println("estDuration " + estDuration/60/1000);
		System.out.println("% " + (float) 1200/questionSize * 100);		
		
	}
	@Test
	public void getNextQuestionByCurrentNumberTest(){
		assertTrue("2".equals(CommonUtil.getNextQuestionByCurrentNumber("1")));
		assertTrue("10".equals(CommonUtil.getNextQuestionByCurrentNumber("9")));
		assertTrue("20".equals(CommonUtil.getNextQuestionByCurrentNumber("19")));
		assertTrue("101".equals(CommonUtil.getNextQuestionByCurrentNumber("100")));
		assertTrue("1A20".equals(CommonUtil.getNextQuestionByCurrentNumber("1A19")));
		assertTrue("21A20".equals(CommonUtil.getNextQuestionByCurrentNumber("21A19")));
		assertTrue("1A1".equals(CommonUtil.getNextQuestionByCurrentNumber("1A")));
		assertTrue("1A2".equals(CommonUtil.getNextQuestionByCurrentNumber("1A1")));
		assertTrue("1A1A1".equals(CommonUtil.getNextQuestionByCurrentNumber("1A1A")));
		assertTrue("1A1B1".equals(CommonUtil.getNextQuestionByCurrentNumber("1A1B")));
		assertTrue("1ZA2".equals(CommonUtil.getNextQuestionByCurrentNumber("1ZA1")));
		assertTrue("1ZA20".equals(CommonUtil.getNextQuestionByCurrentNumber("1ZA19")));
		assertTrue("1ZA1".equals(CommonUtil.getNextQuestionByCurrentNumber("1ZA")));
	}
	
	
}
