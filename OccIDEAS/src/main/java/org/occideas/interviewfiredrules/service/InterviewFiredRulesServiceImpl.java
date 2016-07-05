package org.occideas.interviewfiredrules.service;

import java.util.List;

import org.occideas.entity.InterviewFiredRules;
import org.occideas.interviewfiredrules.dao.InterviewFiredRulesDao;
import org.occideas.mapper.InterviewFiredRulesMapper;
import org.occideas.vo.InterviewFiredRulesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewFiredRulesServiceImpl implements InterviewFiredRulesService{

	@Autowired
	private InterviewFiredRulesDao dao;
	@Autowired
	private InterviewFiredRulesMapper mapper;
	
	@Override
	public List<InterviewFiredRulesVO> listAll() {
		return null;
	}

	@Override
	public List<InterviewFiredRulesVO> findById(Long id) {
		return null;
	}

	@Override
	public InterviewFiredRulesVO create(InterviewFiredRulesVO vo) {
		InterviewFiredRules entity = dao.save(mapper.convertToInterviewFiredRules(vo));
		return mapper.convertToInterviewFiredRulesVO(entity);
	}

	@Override
	public void update(InterviewFiredRulesVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(InterviewFiredRulesVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<InterviewFiredRulesVO> findByInterviewId(Long interviewId) {
		List<InterviewFiredRules> entity = dao.findByInterviewId(interviewId);
		return mapper.convertToInterviewFiredRulesVOWithRulesList(entity);
	}



}
