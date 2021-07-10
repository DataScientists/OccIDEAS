package org.occideas.interviewfiredrules.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Agent;
import org.occideas.entity.InterviewFiredRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InterviewFiredRulesDao {

  @Autowired
  private SessionFactory sessionFactory;

  public InterviewFiredRules save(InterviewFiredRules entity) {
    sessionFactory.getCurrentSession().saveOrUpdate(entity);
    return entity;
  }

  public List<InterviewFiredRules> saveAll(List<InterviewFiredRules> interviewFiredRules) {
    return interviewFiredRules.stream()
            .map(interviewFiredRule ->  save(interviewFiredRule))
            .collect(Collectors.toList());
  }

  public List<InterviewFiredRules> findByInterviewId(Long interviewId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewFiredRules.class);
    if (interviewId != null) {
      crit.add(Restrictions.eq("idinterview", interviewId));
    }
    return crit.list();
  }

  public void delete(InterviewFiredRules interviewFiredRule) {
    sessionFactory.getCurrentSession().delete(interviewFiredRule);
  }

  public void deleteAllByInterviewId(long interviewId) {
    List<InterviewFiredRules> firedRules = findByInterviewId(interviewId);
    firedRules.stream().forEach(interviewFiredRule->delete(interviewFiredRule));
  }

}
