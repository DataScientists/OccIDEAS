package org.occideas.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.occideas.entity.Question;
import org.occideas.vo.QuestionVO;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

	QuestionVO convertToQuestionVO(Question question);

	List<QuestionVO> convertToQuestionVOList(List<Question> questionList);

	Question convertToQuestion(QuestionVO questionVO);
	
	List<Question> convertToQuestionList(List<QuestionVO> questionVO);
	
}
