package org.occideas.question.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.QuestionVO;

public interface QuestionService extends BaseService<QuestionVO> {

	QuestionVO determineNextQuestionByCurrentNumber(String moduleId,String nodeNumber);

	ModuleVO getQuestionWithParentId(Long idNode);

	List<QuestionVO> getQuestionsWithSingleChildLevel(Long Id);

	List<QuestionVO> getQuestionsWithParentId(String parentId);
	
	List<QuestionVO> getAllMultipleQuestions();
	
	void updateWithIndependentTransaction(QuestionVO o);
}
