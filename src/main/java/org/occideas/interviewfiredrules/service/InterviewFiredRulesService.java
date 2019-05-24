package org.occideas.interviewfiredrules.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.GenericNodeVO;
import org.occideas.vo.InterviewFiredRulesVO;

import java.util.List;

public interface InterviewFiredRulesService extends BaseService<InterviewFiredRulesVO> {

  List<InterviewFiredRulesVO> findByInterviewId(Long interviewId);

  GenericNodeVO findNodeById(long idNode);

}
