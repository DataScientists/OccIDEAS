package org.occideas.interviewautoassessment.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewAutoAssessment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewAutoAssessmentDao {

	@Autowired
	private SessionFactory sessionFactory;

	public InterviewAutoAssessment save(InterviewAutoAssessment entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
		return entity;
	}

	public List<InterviewAutoAssessment> saveList(List<InterviewAutoAssessment> entities) {
		for (InterviewAutoAssessment entity : entities) {
			sessionFactory.getCurrentSession().saveOrUpdate(entity);
		}

		return entities;
	}

	public List<InterviewAutoAssessment> findByInterviewId(Long interviewId) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewAutoAssessment.class);
		if (interviewId != null) {
			crit.add(Restrictions.eq("idInterview", interviewId));
		}
		return crit.list();
	}

}
