package org.occideas.systemproperty.dao;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.PossibleAnswer;
import org.occideas.entity.Question;
import org.occideas.entity.SystemProperty;
import org.occideas.entity.SystemProperty_;
import org.occideas.systemproperty.SystemConfigTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Repository
public class SystemPropertyDao {

  private final String POS_ANS_WITH_STUDY_AGENTS_SQL = "SELECT * FROM Node where idNode in"
          + " (SELECT idNode FROM ModuleRule where idModule=:param "
          + " and idAgent in (select value from SYS_CONFIG where type='" + SystemConfigTypes.studyagent.name()
          + "'))";
  private final String QUESTION_ON_POS_ANS_WITH_STUDY_AGENTS_SQL = "SELECT * FROM Node where idNode in"
          + " (SELECT idNode FROM ModuleRule where idNode=:param "
          + " and idAgent in (select value from SYS_CONFIG where type='" + SystemConfigTypes.studyagent.name()
          + "'))";
  private final String POS_ANS_WITH_AGENT_SQL = "SELECT * FROM Node where idNode in"
    + " (SELECT idNode FROM ModuleRule where idModule=:idModule "
    + " and idAgent = :idAgent)";
  @Autowired
  private SessionFactory sessionFactory;

  public SystemProperty save(SystemProperty sysProp) {
    final Session session = sessionFactory.getCurrentSession();
    session.clear();
    session.saveOrUpdate(sysProp);
    return sysProp;
  }

  public SystemProperty getById(long id) {
    final Session session = sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(SystemProperty.class);
    criteria.add(Restrictions.eq("id", id));

    SystemProperty sysProp = (SystemProperty) criteria.uniqueResult();
    return sysProp;
  }

  public List<SystemProperty> getAll() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(SystemProperty.class);
    return crit.list();
  }

  /**
   * Get all that matches the key
   *
   * @param key
   * @return
   */
  public List<SystemProperty> getAll(String key) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(SystemProperty.class);
    crit.add(Restrictions.eq("id", key));
    return crit.list();
  }

  public void delete(SystemProperty entity) {
    sessionFactory.getCurrentSession().delete(entity);
  }

  public SystemProperty getByName(String name) {
    final Session session = sessionFactory.getCurrentSession();
    Criteria criteria = session.createCriteria(SystemProperty.class);
    criteria.add(Restrictions.eq("name", name));

    SystemProperty sysProp = (SystemProperty) criteria.uniqueResult();
    return sysProp;
  }

  public List<SystemProperty> getByType(String type) {
    final Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<SystemProperty> criteria = builder.createQuery(SystemProperty.class);
    Root<SystemProperty> root = criteria.from(SystemProperty.class);
    criteria.select(root);
    criteria.where(builder.and(
            builder.equal(root.get(SystemProperty_.type), type)
    ));
    List<SystemProperty> resultList = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
    if (resultList.isEmpty()) {
      return Collections.emptyList();
    }
    return resultList;
  }

  public List<PossibleAnswer> getPosAnsWithStudyAgentsByIdMod(long idModule) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(POS_ANS_WITH_STUDY_AGENTS_SQL).
      addEntity(PossibleAnswer.class);
    sqlQuery.setParameter("param", String.valueOf(idModule));
    List<PossibleAnswer> list = sqlQuery.list();
    return list;
  }

  public List<Question> getQuestionWithStudyAgentsByIdPossibleAnswer(long idQuestion) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(QUESTION_ON_POS_ANS_WITH_STUDY_AGENTS_SQL).
      addEntity(Question.class);
    sqlQuery.setParameter("param", String.valueOf(idQuestion));
    List<Question> list = sqlQuery.list();
    return list;
  }

  public List<PossibleAnswer> getPosAnsWithAgentAndIdMod(long idModule, long idAgent) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(POS_ANS_WITH_AGENT_SQL).
      addEntity(PossibleAnswer.class);
    sqlQuery.setParameter("idModule", String.valueOf(idModule));
    sqlQuery.setParameter("idAgent", String.valueOf(idAgent));
    List<PossibleAnswer> list = sqlQuery.list();
    return list;
  }


}
