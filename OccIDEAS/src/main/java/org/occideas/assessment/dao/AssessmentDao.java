package org.occideas.assessment.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentDao implements IAssessmentDao{

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private PageUtil<AssessmentAnswerSummary> pageUtil;

	@Override
	public List<AssessmentAnswerSummary> getAnswerSummaryByName(AssessmentAnswerSummaryFilterVO filter) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(AssessmentAnswerSummary.class);
		if(filter.getAnswerId() != null){
			crit.add(Restrictions.eq("answerId", filter.getAnswerId()));
		}
		if(filter.getName() != null){
			crit.add(Restrictions.eq("name", filter.getName()));
		}
		crit.setFirstResult(pageUtil.calculatePageIndex(filter.getPageNumber(), 
				filter.getSize()));
		crit.setMaxResults(filter.getSize());
		return crit.list();
	}
	
	@Override
	public Long getAnswerSummaryByNameTotalCount(AssessmentAnswerSummaryFilterVO filter) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(AssessmentAnswerSummary.class);
		if(filter.getAnswerId() != null){
			crit.add(Restrictions.eq("answerId", filter.getAnswerId()));
		}
		if(filter.getName() != null){
			crit.add(Restrictions.eq("name", filter.getName()));
		}
		crit.setProjection(Projections.rowCount());
		return (Long) crit.uniqueResult();
	}
}