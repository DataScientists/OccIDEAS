package org.occideas.vo;

import java.util.Comparator;

public class InterviewQuestionComparator implements Comparator<InterviewQuestionVO>{

	@Override
	public int compare(InterviewQuestionVO o1, InterviewQuestionVO o2) {
		if(o2.getNumber() == null){
			return 1;
		}
		
		if(o1.getNumber() == null){
			return -1;
		}
		
		if(computeCharacters(o1.getNumber()) < computeCharacters(o2.getNumber())){
			return -1;
		}else if(computeCharacters(o1.getNumber()) > computeCharacters(o2.getNumber())){
			return 1;
		}
		return 0;
	}
	
	private int computeCharacters(String number){
		int result = 0;
		char[] charArr = number.toCharArray();
		for(int i = 0;i < charArr.length;i++){
			result = result + Character.getNumericValue(charArr[i]);
		}
		return result;
	}

}
