package org.occideas.interviewanswer.service;

import java.util.List;

import org.occideas.interviewanswer.dao.InterviewAnswerDao;
import org.occideas.mapper.InterviewAnswerMapper;
import org.occideas.vo.InterviewAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewAnswerServiceImpl implements InterviewAnswerService{

	@Autowired
	private InterviewAnswerMapper mapper;
	@Autowired
	private InterviewAnswerDao dao;
	
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

}
