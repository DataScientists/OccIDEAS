package org.occideas.interviewfiredrules.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.entity.InterviewFiredRules;
import org.occideas.entity.InterviewFiredRules_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
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

  @Transactional
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

    List<InterviewFiredRules> resultList = sessionFactory.getCurrentSession()
            .createQuery(criteria)
            .getResultList();
    return resultList;
  }

  public void delete(InterviewFiredRules interviewFiredRule) {
    sessionFactory.getCurrentSession().delete(interviewFiredRule);
  }

  public void deleteAllByInterviewId(long interviewId) {
    List<InterviewFiredRules> firedRules = findByInterviewId(interviewId);
    firedRules.stream().forEach(interviewFiredRule->delete(interviewFiredRule));
  }

}
