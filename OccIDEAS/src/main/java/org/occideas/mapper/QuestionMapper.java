package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.Question;
import org.occideas.vo.QuestionVO;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

	QuestionVO convertToQuestionVO(Question question);
	
	QuestionVO convertToInterviewQuestionVO(Question question);

	List<QuestionVO> convertToQuestionVOList(List<Question> questionList);

	List<QuestionVO> convertToInterviewQuestionVOList(List<Question> questionList);

	Question convertToQuestion(QuestionVO questionVO);
	
	List<Question> convertToQuestionList(List<QuestionVO> questionVO);
	
	QuestionVO convertToQuestionVOReducedDetails(Question question);
	
	List<QuestionVO> convertToQuestionVOReducedDetailsList(List<Question> questionList);
	
	List<QuestionVO> convertToQuestionVOExcludeChildsList(List<Question> questionList);
	
	QuestionVO convertToQuestionVOExcludeChilds(Question question);
	
	QuestionVO convertToQuestionWithModRulesReduced(Question question);

	List<QuestionVO> convertToQuestionWithFlagsVOList(List<Question> questionEntity, boolean includeChildNodes,
			boolean includeRules);

	QuestionVO convertToQuestionWithFlagsVO(Question question, boolean includeChildnodes, boolean includeRules);

    QuestionVO convertToQuestionVOOnly(Question question);

    List<QuestionVO> convertToQuestionVOOnlyList(List<Question> questionEntity);
	
}
