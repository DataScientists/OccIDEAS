package org.occideas.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommonUtilTest {

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
