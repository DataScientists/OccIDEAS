package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.PossibleAnswer;
import org.occideas.vo.PossibleAnswerVO;

public interface PossibleAnswerMapper {

	PossibleAnswer convertToPossibleAnswerExModRule(PossibleAnswerVO answerVO);
	
	List<PossibleAnswer> convertToPossibleAnswerExModRuleList(List<PossibleAnswerVO> answerVO);
	
	PossibleAnswer convertToPossibleAnswer(PossibleAnswerVO answerVO);
	
	List<PossibleAnswer> convertToPossibleAnswerList(List<PossibleAnswerVO> answerVO);

	PossibleAnswerVO convertToPossibleAnswerVO(PossibleAnswer answerEntity, boolean includeChildNode);
	
	PossibleAnswerVO convertToPossibleAnswerWithModuleRuleVO(PossibleAnswer answerEntity);
	
	PossibleAnswerVO convertToInterviewPossibleAnswerVO(PossibleAnswer answerEntity);

	List<PossibleAnswerVO> convertToPossibleAnswerVOList(List<PossibleAnswer> answerEntity, boolean includeChildNodes);
	
	List<PossibleAnswerVO> convertToInterviewPossibleAnswerVOList(List<PossibleAnswer> answerEntity);
	
	PossibleAnswerVO convertToPossibleAnswerVOExModRule(PossibleAnswer answerEntity);

	List<PossibleAnswerVO> convertToPossibleAnswerVOExModRuleList(List<PossibleAnswer> answerEntity);
	
	PossibleAnswerVO convertToPossibleAnswerVOWithFlag(PossibleAnswer answerEntity,
			boolean includeChildNode,
			boolean includeRules);
	
	List<PossibleAnswerVO> convertToPossibleAnswerVOWithFlagList(List<PossibleAnswer> answerEntity,
			boolean includeChildNode,
			boolean includeRules);

	PossibleAnswerVO convertToPossibleAnswerVOExcQuestionAnsChild(PossibleAnswer answerEntity);
}
