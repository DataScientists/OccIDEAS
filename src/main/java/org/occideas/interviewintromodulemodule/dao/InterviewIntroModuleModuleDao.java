package org.occideas.interviewintromodulemodule.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.InterviewIntroModuleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InterviewIntroModuleModuleDao {

  @Autowired
  private SessionFactory sessionFactory;

  public List<InterviewIntroModuleModule> getInterviewIntroModByModId(long idModule) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewIntroModuleModule.class)
      .add(Restrictions.eq("idModule", idModule));
    return crit.list();
  }

  public List<InterviewIntroModuleModule> getModulesByInterviewId(long idInterview) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewIntroModuleModule.class)
      .add(Restrictions.eq("interviewId", idInterview));
    return crit.list();
  }

  public List<InterviewIntroModuleModule> getDistinctModules() {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(InterviewIntroModuleModule.class)
      .setProjection(
        Projections.distinct(Projections.projectionList()
          .add(Projections.property("idModule"), "idModule")
          .add(Projections.property("interviewModuleName"), "interviewModuleName")))
      .setResultTransformer(Transformers.aliasToBean(
        InterviewIntroModuleModule.class));
    List list = crit.list();
    return list;
  }

  public List<InterviewIntroModuleModule> getDistinctModulesByTopModule() {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(InterviewIntroModuleModule.class)
      .setProjection(
        Projections.distinct(Projections.projectionList()
          .add(Projections.property("idModule"), "idModule")
          .add(Projections.property("interviewModuleName"), "interviewModuleName")))
      .setResultTransformer(Transformers.aliasToBean(
        InterviewIntroModuleModule.class));
    List list = crit.list();
    return list;
  }

  public List<InterviewIntroModuleModule> getInterviewByModuleId(long idModule) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewIntroModuleModule.class)
      .add(Restrictions.eq("idModule", idModule));
    return crit.list();
  }

  public List<InterviewIntroModuleModule> findNonIntroById(Long id, String value) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(InterviewIntroModuleModule.class)
      .add(Restrictions.eq("interviewId", id))
      .add(Restrictions.ne("idModule", Long.valueOf(value)));
    return crit.list();
  }
}
