package org.occideas.assessment.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.AssessmentAnswerSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentDao implements IAssessmentDao{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<AssessmentAnswerSummary> getAnswerSummaryByName(Long answerId, String name) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(AssessmentAnswerSummary.class)
				.add(Restrictions.eq("answerId", answerId))
				.add(Restrictions.eq("name", name));
		return crit.list();
	}


	
}
