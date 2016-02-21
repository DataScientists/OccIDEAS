package org.occideas.question.service;

import org.occideas.base.service.BaseService;
import org.occideas.entity.Node;
import org.occideas.vo.QuestionVO;

public interface QuestionService extends BaseService<QuestionVO> {
    public QuestionVO getNextQuestion(long interviewId, long idNode);
}
