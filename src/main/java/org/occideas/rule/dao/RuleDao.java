package org.occideas.rule.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Rule;
import org.occideas.entity.RulePlain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class RuleDao implements IRuleDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Long save(Rule rule) {
    return (Long) sessionFactory.getCurrentSession().save(rule);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void delete(Rule rule) {
    rule.setDeleted(1);
    sessionFactory.getCurrentSession().saveOrUpdate(rule);
  }

  @Override
  public void deleteAll(List<Long> ruleIds){
    final Session session = sessionFactory.getCurrentSession();
    Query<Rule> query =session.createQuery("update Rule r set r.deleted=1 where r.idRule IN (:ruleIds)");
    query.setParameterList("ruleIds", ruleIds);
    query.executeUpdate();
  }

  @Override
  public Rule get(Long id) {
    return (Rule) sessionFactory.getCurrentSession().get(Rule.class, id);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public Rule merge(Rule rule) {
    return (Rule) sessionFactory.getCurrentSession().merge(rule);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void saveOrUpdate(Rule rule) {
    sessionFactory.getCurrentSession().saveOrUpdate(rule);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveBatchRule(List<RulePlain> rules) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    for (RulePlain rule : rules) {
      sessionFactory.getCurrentSession().save(rule);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Rule> getAll() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Rule.class)
      .add(Restrictions.eq("deleted", 0))
      .setProjection(Projections.projectionList()
        .add(Projections.property("agentId"), "agentId")
        .add(Projections.property("agent"), "agent"))
      .setResultTransformer(Transformers.aliasToBean(Rule.class));
    return crit.list();
  }

  @Override
  public List<Rule> findByAgentId(long agentId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Rule.class)
      .add(Restrictions.eq("deleted", 0))
      .add(Restrictions.eq("agentId", agentId));
    return crit.list();
  }

  @Override
  @SuppressWarnings("unchecked")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Long getMaxRuleId() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Rule.class)
      .addOrder(Order.desc("idRule"))
      .setMaxResults(1)
      .setProjection(Projections.projectionList()
        .add(Projections.property("idRule"), "idRule"));
    Long maxRuleId = (Long) crit.uniqueResult();
    if (maxRuleId == null) {
      Rule tempRule = new Rule();
      maxRuleId = save(tempRule);
      delete(tempRule);
    }
    return maxRuleId;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    sessionFactory.getCurrentSession().createSQLQuery("truncate table Rule").executeUpdate();
  }

}
