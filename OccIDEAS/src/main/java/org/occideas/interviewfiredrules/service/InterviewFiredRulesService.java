package org.occideas.interviewfiredrules.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewFiredRulesVO;

public interface InterviewFiredRulesService extends BaseService<InterviewFiredRulesVO>{

	List<InterviewFiredRulesVO> findByInterviewId(Long interviewId);
	
}
