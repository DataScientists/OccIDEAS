package org.occideas.interviewanswer.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewAnswer;
import org.occideas.interviewanswer.dao.IInterviewAnswerDao;
import org.occideas.mapper.InterviewAnswerMapper;
import org.occideas.mapper.InterviewQuestionMapper;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewAnswerServiceImpl implements InterviewAnswerService{

	@Autowired
	private InterviewAnswerMapper mapper;
	@Autowired
	private InterviewQuestionMapper qmapper;
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
    
    @Override
   	public List<InterviewQuestionVO> saveIntervewAnswersAndGetChildQuestion(List<InterviewAnswerVO> o) {
   		return qmapper.convertToInterviewQuestionVOList(dao.saveIntervewAnswersAndGetChildQuestion(mapper.convertToInterviewAnswerList(o)));
   	}

	@Override
	public List<InterviewAnswerVO> findByInterviewId(Long id) {
		List<InterviewAnswer> modules = dao.findByInterviewId(id);
		ArrayList<InterviewAnswerVO> modulesVO = new ArrayList<InterviewAnswerVO>();
		modulesVO.addAll(mapper.convertToInterviewAnswerVOList(modules));
		return modulesVO;
	}

}
