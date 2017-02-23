package org.occideas.interviewmanualassessment.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewManualAssessment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewManualAssessmentDao {

	@Autowired
	private SessionFactory sessionFactory;

	public InterviewManualAssessment save(InterviewManualAssessment entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	public List<InterviewManualAssessment> saveList(List<InterviewManualAssessment> entities) {
		for (InterviewManualAssessment entity : entities) {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		}

		return entities;
	}

	public List<InterviewManualAssessment> findByInterviewId(Long interviewId) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewManualAssessment.class);
		if (interviewId != null) {
			crit.add(Restrictions.eq("idinterview", interviewId));
		}
		return crit.list();
	}

}
