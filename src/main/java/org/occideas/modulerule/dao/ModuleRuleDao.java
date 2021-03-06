package org.occideas.modulerule.dao;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.ModuleRule;
import org.occideas.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ModuleRuleDao {

  @Autowired
  private SessionFactory sessionFactory;

  public ModuleRule save(ModuleRule module) {
    return (ModuleRule) sessionFactory.getCurrentSession().save(module);
  }

  public void delete(ModuleRule module) {
    sessionFactory.getCurrentSession().delete(module);
  }

  public ModuleRule get(Long id) {
    return (ModuleRule) sessionFactory.getCurrentSession().get(ModuleRule.class, id);
  }

  public Number getRuleCountById(Long id) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ModuleRule.class);
    crit.add(Restrictions.eq("idModule", id));
    ArrayList<Rule> rules = new ArrayList<Rule>();
    for (Object o : crit.list()) {
      ModuleRule mr = (ModuleRule) o;
      if (!rules.contains(mr.getRule())) {
        rules.add(mr.getRule());
      }
    }
    return rules.size();
  }

  public ModuleRule merge(ModuleRule module) {
    return (ModuleRule) sessionFactory.getCurrentSession().merge(module);
  }

  public void saveOrUpdate(ModuleRule module) {
    sessionFactory.getCurrentSession().saveOrUpdate(module);
  }

  @SuppressWarnings("unchecked")
  public List<ModuleRule> getAll() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ModuleRule.class);
    return crit.list();
  }

  @SuppressWarnings("unchecked")
  public List<ModuleRule> getRulesByModule(Long moduleId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ModuleRule.class)
      .add(Restrictions.eq("idModule", moduleId));
    return crit.list();
  }

  @SuppressWarnings("unchecked")
  public List<ModuleRule> getRulesByModuleAndAgent(Long moduleId, Long agentId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ModuleRule.class)
      .add(Restrictions.eq("idModule", moduleId))
      .add(Restrictions.eq("idAgent", agentId));
    return crit.list();
  }

  @SuppressWarnings("unchecked")
  public List<ModuleRule> getRulesByIdNode(Long idNode) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ModuleRule.class)
      .add(Restrictions.eq("idNode", idNode));
    return crit.list();
  }

  @SuppressWarnings("unchecked")
  public List<ModuleRule> getRulesByAgent(Long agentId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ModuleRule.class)
      .add(Restrictions.eq("idAgent", agentId));
    return crit.list();
  }
}
