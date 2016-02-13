package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.PossibleAnswer;
import org.occideas.vo.PossibleAnswerVO;

public interface PossibleAnswerMapper {

	PossibleAnswerVO convertToPossibleAnswerVO(PossibleAnswer answerEntity);
	
	List<PossibleAnswerVO> convertToPossibleAnswerVOList(List<PossibleAnswer> answerEntity);

	PossibleAnswer convertToPossibleAnswer(PossibleAnswerVO answerVO);
	
	List<PossibleAnswer> convertToPossibleAnswerList(List<PossibleAnswerVO> answerVO);
}
