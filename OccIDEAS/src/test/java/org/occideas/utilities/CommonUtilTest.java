package org.occideas.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommonUtilTest {

	@Test
	public void getNextQuestionByCurrentNumberTest(){
		assertTrue("2".equals(CommonUtil.getNextQuestionByCurrentNumber("1")));
		assertTrue("1A1".equals(CommonUtil.getNextQuestionByCurrentNumber("1A")));
		assertTrue("1A2".equals(CommonUtil.getNextQuestionByCurrentNumber("1A1")));
		assertTrue("1A1A1".equals(CommonUtil.getNextQuestionByCurrentNumber("1A1A")));
		assertTrue("1A1B1".equals(CommonUtil.getNextQuestionByCurrentNumber("1A1B")));
	}
	
}
