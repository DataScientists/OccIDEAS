package org.occideas.interviewmanualassessment.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.occideas.entity.InterviewManualAssessment;
import org.occideas.entity.InterviewManualAssessment_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<InterviewManualAssessment> criteria = builder.createQuery(InterviewManualAssessment.class);
    Root<InterviewManualAssessment> root = criteria.from(InterviewManualAssessment.class);
    criteria.select(root);
    if (interviewId != null) {
      criteria.where(builder.equal(root.get(InterviewManualAssessment_.ID_INTERVIEW), interviewId));
    }

    return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
  }

}
