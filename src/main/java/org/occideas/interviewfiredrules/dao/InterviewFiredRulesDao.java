package org.occideas.interviewfiredrules.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewFiredRules;
import org.occideas.entity.InterviewFiredRules_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class InterviewFiredRulesDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public InterviewFiredRules save(InterviewFiredRules entity) {
    sessionFactory.getCurrentSession().saveOrUpdate(entity);
    return entity;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<InterviewFiredRules> saveAll(List<InterviewFiredRules> interviewFiredRules) {
     interviewFiredRules.stream()
            .forEach(ir -> sessionFactory.getCurrentSession().save(ir));
     return interviewFiredRules;
  }

  public List<InterviewFiredRules> findByInterviewId(Long interviewId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewFiredRules.class);
    if (interviewId != null) {
      crit.add(Restrictions.eq("idinterview", interviewId));
    }
    return crit.list();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<InterviewFiredRules> findByInterviewIdWithRules(Long interviewId) {
    final Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<InterviewFiredRules> criteria = builder.createQuery(InterviewFiredRules.class);
    Root<InterviewFiredRules> root = criteria.from(InterviewFiredRules.class);
    root.fetch(InterviewFiredRules_.RULES);
    criteria.select(root);
    if (interviewId != null) {
      criteria.where(builder.equal(root.get(InterviewFiredRules_.id), interviewId));
    }

    return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
  }

  public void delete(InterviewFiredRules interviewFiredRule) {
    sessionFactory.getCurrentSession().delete(interviewFiredRule);
  }

  public void deleteAllByInterviewId(long interviewId) {
    List<InterviewFiredRules> firedRules = findByInterviewId(interviewId);
    firedRules.stream().forEach(interviewFiredRule->delete(interviewFiredRule));
  }

}
