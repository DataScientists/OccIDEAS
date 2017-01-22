package org.occideas.assessment.service;

import java.util.List;

import javax.transaction.Transactional;

import org.occideas.assessment.dao.AssessmentDao;
import org.occideas.entity.AssessmentAnswerSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService{

	@Autowired
	private AssessmentDao dao;
	
	@Override
	public List<AssessmentAnswerSummary> getAnswerSummaryByName(Long answerId, String name) {
		return dao.getAnswerSummaryByName(answerId, name);
	}

}
