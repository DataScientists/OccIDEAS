package org.occideas.utilities;

import java.math.BigInteger;
import java.util.Comparator;

import org.occideas.entity.InterviewQuestion;

public class QuestionComparator implements Comparator<InterviewQuestion> {

	@Override
	public int compare(InterviewQuestion o1, InterviewQuestion o2) {
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
