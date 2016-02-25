package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.PossibleAnswer;
import org.occideas.vo.PossibleAnswerVO;

public interface PossibleAnswerMapper {

	PossibleAnswer convertToPossibleAnswer(PossibleAnswerVO answerVO);
	
	List<PossibleAnswer> convertToPossibleAnswerList(List<PossibleAnswerVO> answerVO);

	PossibleAnswerVO convertToPossibleAnswerVO(PossibleAnswer answerEntity, boolean includeChildNode);

	List<PossibleAnswerVO> convertToPossibleAnswerVOList(List<PossibleAnswer> answerEntity, boolean includeChildNodes);
	
	PossibleAnswerVO convertToPossibleAnswerVOExModRule(PossibleAnswer answerEntity);

	List<PossibleAnswerVO> convertToPossibleAnswerVOExModRuleList(List<PossibleAnswer> answerEntity);
}
