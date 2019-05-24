package org.occideas.interviewautoassessment.service;

import java.util.List;

import org.occideas.entity.InterviewAutoAssessment;
import org.occideas.genericnode.dao.GenericNodeDao;
import org.occideas.interviewautoassessment.dao.InterviewAutoAssessmentDao;
import org.occideas.mapper.GenericNodeMapper;
import org.occideas.mapper.InterviewAutoAssessmentMapper;
import org.occideas.vo.GenericNodeVO;
import org.occideas.vo.InterviewAutoAssessmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewAutoAssessmentServiceImpl implements InterviewAutoAssessmentService{

	@Autowired
	private InterviewAutoAssessmentDao dao;
	@Autowired
	private InterviewAutoAssessmentMapper mapper;
	@Autowired
	private GenericNodeMapper genNodeMapper;
	@Autowired
	private GenericNodeDao genNodeDao;
	
	@Override
	public List<InterviewAutoAssessmentVO> listAll() {
		return null;
	}

	@Override
	public List<InterviewAutoAssessmentVO> findById(Long id) {
		return null;
	}

	@Override
	public InterviewAutoAssessmentVO create(InterviewAutoAssessmentVO vo) {
		InterviewAutoAssessment entity = dao.save(mapper.convertToInterviewAutoAssessment(vo));
		return mapper.convertToInterviewAutoAssessmentVO(entity);
	}
	@Override
	public List<InterviewAutoAssessmentVO> updateAutoAssessments(List<InterviewAutoAssessmentVO> o) {
		return mapper.convertToInterviewAutoAssessmentVOList(dao.saveList(mapper.convertToInterviewAutoAssessmentList(o)));
	}

	@Override
	public void update(InterviewAutoAssessmentVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(InterviewAutoAssessmentVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<InterviewAutoAssessmentVO> findByInterviewId(Long interviewId) {
		List<InterviewAutoAssessment> entity = dao.findByInterviewId(interviewId);
		return mapper.convertToInterviewAutoAssessmentVOWithRulesList(entity);
	}

	@Override
	public GenericNodeVO findNodeById(long idNode) {
		return genNodeMapper.convertToGenNodeVO(genNodeDao.get(idNode));
	}



}
