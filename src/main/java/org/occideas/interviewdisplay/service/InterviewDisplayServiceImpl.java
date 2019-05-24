package org.occideas.interviewdisplay.service;

import java.util.Date;
import java.util.List;

import org.occideas.interviewdisplay.dao.InterviewDisplayAnswerDao;
import org.occideas.interviewdisplay.dao.InterviewDisplayDao;
import org.occideas.mapper.InterviewDisplayAnswerMapper;
import org.occideas.mapper.InterviewDisplayMapper;
import org.occideas.vo.InterviewDisplayAnswerVO;
import org.occideas.vo.InterviewDisplayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewDisplayServiceImpl implements InterviewDisplayService{

	@Autowired
	private InterviewDisplayMapper mapper;
	
	@Autowired
	private InterviewDisplayAnswerMapper ansMapper;
	
	@Autowired
	private InterviewDisplayDao dao;
	
	@Autowired
	private InterviewDisplayAnswerDao displayAnswerDao;

	@Override
	public List<InterviewDisplayVO> listAll() {
		return mapper.convertToInterviewDisplayVOList(dao.getAll());
	}

	@Override
	public List<InterviewDisplayVO> findById(Long id) {
		return mapper.convertToInterviewDisplayVOList(dao.findByInterviewId(id));
	}

	@Override
	public InterviewDisplayVO create(InterviewDisplayVO vo) {
		Date date = new Date();
		if(vo.getLastUpdated() == null){
			vo.setLastUpdated(date);
		}
		return mapper.convertToInterviewDisplayVO(
				dao.saveOrUpdate(mapper.convertToInterviewDisplay(vo)));
	}

	@Override
	public void update(InterviewDisplayVO o) {
		
	}

	@Override
	public void delete(InterviewDisplayVO o) {
		
	}

	@Override
	public List<InterviewDisplayVO> updateList(List<InterviewDisplayVO> list) {
		return mapper.convertToInterviewDisplayVOList(
				dao.updateList(mapper.convertToInterviewDisplayList(list)));
	}

	@Override
	public List<InterviewDisplayAnswerVO> updateDisplayAnswerList(List<InterviewDisplayAnswerVO> list) {
		return ansMapper.convertToInterviewDisplayAnswerVOList(displayAnswerDao.
				updateList(ansMapper.convertToInterviewDisplayAnswerList(list)));
	}
	

}
