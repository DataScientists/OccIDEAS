package org.occideas.question.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.QuestionVO;

public interface QuestionService extends BaseService<QuestionVO> {

	QuestionVO determineNextQuestionByCurrentNumber(String moduleId,String nodeNumber);

	ModuleVO getQuestionWithParentId(Long idNode);
	
	
}
