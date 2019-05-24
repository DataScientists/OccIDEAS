package org.occideas.interviewmanualassessment.service;

import java.util.List;

import org.occideas.entity.InterviewManualAssessment;
import org.occideas.genericnode.dao.GenericNodeDao;
import org.occideas.interviewmanualassessment.dao.InterviewManualAssessmentDao;
import org.occideas.mapper.GenericNodeMapper;
import org.occideas.mapper.InterviewManualAssessmentMapper;
import org.occideas.vo.GenericNodeVO;
import org.occideas.vo.InterviewManualAssessmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewManualAssessmentServiceImpl implements InterviewManualAssessmentService{

	@Autowired
	private InterviewManualAssessmentDao dao;
	@Autowired
	private InterviewManualAssessmentMapper mapper;
	@Autowired
	private GenericNodeMapper genNodeMapper;
	@Autowired
	private GenericNodeDao genNodeDao;
	
	@Override
	public List<InterviewManualAssessmentVO> listAll() {
		return null;
	}

	@Override
	public List<InterviewManualAssessmentVO> findById(Long id) {
		return null;
	}

	@Override
	public InterviewManualAssessmentVO create(InterviewManualAssessmentVO vo) {
		InterviewManualAssessment entity = dao.save(mapper.convertToInterviewManualAssessment(vo));
		return mapper.convertToInterviewManualAssessmentVO(entity);
	}
	@Override
	public List<InterviewManualAssessmentVO> updateManualAssessments(List<InterviewManualAssessmentVO> o) {
		return mapper.convertToInterviewManualAssessmentVOList(dao.saveList(mapper.convertToInterviewManualAssessmentList(o)));
	}

	@Override
	public void update(InterviewManualAssessmentVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(InterviewManualAssessmentVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<InterviewManualAssessmentVO> findByInterviewId(Long interviewId) {
		List<InterviewManualAssessment> entity = dao.findByInterviewId(interviewId);
		return mapper.convertToInterviewManualAssessmentVOWithRulesList(entity);
	}

	@Override
	public GenericNodeVO findNodeById(long idNode) {
		return genNodeMapper.convertToGenNodeVO(genNodeDao.get(idNode));
	}



}
