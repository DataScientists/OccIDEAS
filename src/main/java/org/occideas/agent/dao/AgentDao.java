package org.occideas.agent.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Agent;
import org.occideas.entity.AgentGroup;
import org.occideas.entity.AgentPlain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AgentDao implements IAgentDao {

  private final String STUDY_AGENTS_SQL = "SELECT * FROM AgentInfo "
    + " WHERE idAgent in (SELECT value from SYS_CONFIG WHERE type='studyagent')";
  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public Agent save(Agent module) {
    return (Agent) sessionFactory.getCurrentSession().save(module);
  }

  @Override
  public void delete(Agent module) {
    sessionFactory.getCurrentSession().delete(module);
  }

  @Override
  public Agent get(Long id) {
    return (Agent) sessionFactory.getCurrentSession().get(Agent.class, id);
  }

  @Override
  public Agent merge(Agent module) {
    return (Agent) sessionFactory.getCurrentSession().merge(module);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrUpdate(Agent module) {
    sessionFactory.getCurrentSession().saveOrUpdate(module);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveBatchAgents(List<Agent> agents) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    for (Agent agent : agents) {
      sessionFactory.getCurrentSession().save(agent);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Agent> getAll() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Agent.class)
      .setProjection(Projections.projectionList()
        .add(Projections.property("idAgent"), "idAgent")
        .add(Projections.property("name"), "name")
        .add(Projections.property("description"), "description"))
      .setResultTransformer(Transformers.aliasToBean(Agent.class));
    return crit.list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Agent> getAllActive() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Agent.class)
      .addOrder(Order.asc("name"))
      .add(Restrictions.eq("deleted", 0))
      .setProjection(Projections.projectionList()
        .add(Projections.property("idAgent"), "idAgent")
        .add(Projections.property("agentGroup"), "agentGroup")
        .add(Projections.property("name"), "name")
        .add(Projections.property("description"), "description"))
      .setResultTransformer(Transformers.aliasToBean(Agent.class));
    return crit.list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Agent> getStudyAgents() {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(STUDY_AGENTS_SQL).addEntity(Agent.class);

    List<Agent> list = sqlQuery.list();
    return list;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Long saveAgentGroup(AgentGroup group) {
    return (Long) sessionFactory.getCurrentSession().save(group);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("truncate table AgentInfo").executeUpdate();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveBatchAgentsPlain(List<AgentPlain> copyAgentInfoPlainFromDB) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    for (AgentPlain agent : copyAgentInfoPlainFromDB) {
      sessionFactory.getCurrentSession().save(agent);
    }
  }
}
