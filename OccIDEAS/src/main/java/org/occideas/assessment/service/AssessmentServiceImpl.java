package org.occideas.assessment.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.occideas.assessment.dao.AssessmentDao;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;
import org.occideas.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AssessmentServiceImpl implements AssessmentService{

	@Autowired
	private AssessmentDao dao;

	@Autowired
	private PageUtil<AssessmentAnswerSummary> pageUtilIntMod;
	
	@Override
	public PageVO<AssessmentAnswerSummary> getAnswerSummaryByName(AssessmentAnswerSummaryFilterVO filter) {
		List<AssessmentAnswerSummary> list = dao.getAnswerSummaryByName(filter);
		PageVO<AssessmentAnswerSummary> page = pageUtilIntMod.populatePage(list, filter.getPageNumber(), filter.getSize());
		page.setTotalSize(dao.getAnswerSummaryByNameTotalCount(filter).intValue());
		return page;
	}
	
	

}
