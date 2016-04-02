package org.occideas.utilities;

import java.util.List;

public class CommonUtil {

	public static boolean isListEmpty(List<? extends Object> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static String getNextQuestionByCurrentNumber(String number) {
		StringBuilder sb = new StringBuilder(number);
		String lastLetter = sb.substring(sb.length() - 1);
		if(isInteger(lastLetter)){
			sb.replace(sb.length() - 1, sb.length(), 
					String.valueOf(Integer.parseInt(lastLetter) + 1));
		}else{
			sb.append("1");
		}
		return sb.toString();
	}

	private static boolean isInteger(String lastLetter) {
		try {
			Integer.parseInt(lastLetter);
			return true;
		} catch (NumberFormatException exception) {
			return false;
		}
	}

}
