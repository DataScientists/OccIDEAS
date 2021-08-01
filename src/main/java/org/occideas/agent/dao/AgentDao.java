package org.occideas.agent.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AgentDao implements IAgentDao {

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
  public long saveOrUpdate(Agent module) {
    sessionFactory.getCurrentSession().saveOrUpdate(module);
    return module.getIdAgent();
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
  public List<Agent> getStudyAgents() {
    final Session session = sessionFactory.getCurrentSession();
    String SELECT_STUDY_AGENTS = "SELECT s.value from SystemProperty s WHERE s.type='studyagent'";
    List<String> studyAgents = session.createQuery(SELECT_STUDY_AGENTS, String.class)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    if(studyAgents.isEmpty()){
      return Collections.emptyList();
    }
    String SELECT_AGENT_IDS = "SELECT a FROM Agent a WHERE a.idAgent in :studyAgents ";
    return session.createQuery(SELECT_AGENT_IDS, Agent.class)
            .setParameter("studyAgents", studyAgents.stream().map(s->Long.valueOf(s)).collect(Collectors.toList()))
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
  }

  @Override
  public List<Long> getStudyAgentIds() {
    final Session session = sessionFactory.getCurrentSession();
    String SELECT_STUDY_AGENTS = "SELECT s.value from SystemProperty s WHERE s.type='studyagent'";
    List<String> studyAgents = session.createQuery(SELECT_STUDY_AGENTS, String.class)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    if(studyAgents.isEmpty()){
      return Collections.emptyList();
    }
    String SELECT_AGENT_IDS = "SELECT a.idAgent FROM AgentInfo a WHERE a.idAgent in :studyAgents ";
    return session.createQuery(SELECT_AGENT_IDS, Long.class)
            .setParameter("studyAgents", studyAgents.stream().map(s->Long.valueOf(s)).collect(Collectors.toList()))
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
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
