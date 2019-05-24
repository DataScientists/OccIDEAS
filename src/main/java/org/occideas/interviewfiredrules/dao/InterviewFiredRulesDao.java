package org.occideas.interviewfiredrules.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewFiredRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterviewFiredRulesDao {

  @Autowired
  private SessionFactory sessionFactory;

  public InterviewFiredRules save(InterviewFiredRules entity) {
    sessionFactory.getCurrentSession().saveOrUpdate(entity);
    return entity;
  }

  public List<InterviewFiredRules> findByInterviewId(Long interviewId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewFiredRules.class);
    if (interviewId != null) {
      crit.add(Restrictions.eq("idinterview", interviewId));
    }
    return crit.list();
  }


}
