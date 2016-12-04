package org.occideas.utilities;

import java.math.BigInteger;
import java.util.Comparator;

import org.occideas.vo.InterviewQuestionVO;

public class QuestionComparator implements Comparator<InterviewQuestionVO> {

	@Override
	public int compare(InterviewQuestionVO o1, InterviewQuestionVO o2) {
		StringBuilder sbO1 = new StringBuilder();
		for (char c : o1.getNumber().toCharArray()) {
			sbO1.append((int) c);
		}
		BigInteger o1Int = new BigInteger(sbO1.toString());

		StringBuilder sbO2 = new StringBuilder();
		for (char c : o2.getNumber().toCharArray()) {
			sbO2.append((int) c);
		}
		BigInteger o2Int = new BigInteger(sbO2.toString());
		if (o1Int.intValue() == o2Int.intValue()) {
			return 0;
		}
		return o1Int.intValue() < o2Int.intValue() ? -1 : 1;
	}
}
