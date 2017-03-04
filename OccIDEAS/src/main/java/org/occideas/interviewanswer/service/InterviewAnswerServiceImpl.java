package org.occideas.interviewanswer.service;

import java.util.List;

import org.occideas.interviewanswer.dao.IInterviewAnswerDao;
import org.occideas.mapper.InterviewAnswerMapper;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.vo.InterviewAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewAnswerServiceImpl implements InterviewAnswerService{

	@Autowired
	private InterviewAnswerMapper mapper;
	@Autowired
	private IInterviewAnswerDao dao;
	
	@Override
	public List<InterviewAnswerVO> listAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InterviewAnswerVO> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InterviewAnswerVO create(InterviewAnswerVO o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(InterviewAnswerVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(InterviewAnswerVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<InterviewAnswerVO> updateIntA(List<InterviewAnswerVO> o) {
		return mapper.convertToInterviewAnswerVOList(dao.saveOrUpdate(mapper.convertToInterviewAnswerList(o)));
	}
	
    @Override
	public List<InterviewAnswerVO> saveIntervewAnswersAndQueueQuestions(List<InterviewAnswerVO> o) {
		return mapper.convertToInterviewAnswerVOList(dao.saveAnswerAndQueueQuestions(mapper.convertToInterviewAnswerList(o)));
	}

}
