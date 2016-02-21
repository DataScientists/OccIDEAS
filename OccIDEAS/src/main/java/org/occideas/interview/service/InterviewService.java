package org.occideas.interview.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.QuestionVO;

public interface InterviewService extends BaseService<InterviewVO> {
    public void saveAnswer(InterviewVO interviewVO);
}
