package org.occideas.participantdetails.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.ParticipantDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParticipantDetailsDao {

  @Autowired
  private SessionFactory sessionFactory;

  public ParticipantDetails save(ParticipantDetails details) {
    return (ParticipantDetails) sessionFactory.getCurrentSession().save(details);
  }


  public void delete(ParticipantDetails details) {
    sessionFactory.getCurrentSession().delete(details);
  }

  public ParticipantDetails get(Long id) {
    return (ParticipantDetails) sessionFactory.getCurrentSession().get(ParticipantDetails.class, id);
  }

  public ParticipantDetails merge(ParticipantDetails details) {
    return (ParticipantDetails) sessionFactory.getCurrentSession().merge(details);
  }

  public void saveOrUpdate(ParticipantDetails details) {
    sessionFactory.getCurrentSession().saveOrUpdate(details);
  }

  @SuppressWarnings("unchecked")
  public List<ParticipantDetails> getAll() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ParticipantDetails.class)
      .setProjection(Projections.projectionList()
        .add(Projections.property("idNote"), "idNote")
        .add(Projections.property("text"), "text")
        .add(Projections.property("type"), "type"))
      .setResultTransformer(Transformers.aliasToBean(ParticipantDetails.class));
    return crit.list();
  }

  @SuppressWarnings("unchecked")
  public List<ParticipantDetails> getAllActive() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ParticipantDetails.class)
      .add(Restrictions.eq("deleted", 0))
      .setProjection(Projections.projectionList()
        .add(Projections.property("idNote"), "idNote")
        .add(Projections.property("text"), "text")
        .add(Projections.property("type"), "type"))
      .setResultTransformer(Transformers.aliasToBean(ParticipantDetails.class));
    return crit.list();
  }

  @SuppressWarnings("unchecked")
  public List<ParticipantDetails> getListByInterview(long interviewId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ParticipantDetails.class)
      .add(Restrictions.eq("interviewId", interviewId))
      .add(Restrictions.eq("deleted", 0))
      .add(Restrictions.ne("type", "System"))
      .add(Restrictions.isNotNull("text"));
    return crit.list();
  }

}
