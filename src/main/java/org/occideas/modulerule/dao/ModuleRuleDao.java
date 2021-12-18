package org.occideas.modulerule.dao;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.QueryHints;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.ModuleRule;
import org.occideas.entity.ModuleRule_;
import org.occideas.entity.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

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
  
  @Transactional(propagation = Propagation.REQUIRES_NEW)
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

  public List<ModuleRule> getRulesByIdNode(Long idNode) {
    final Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<ModuleRule> criteria = builder.createQuery(ModuleRule.class);
    Root<ModuleRule> root = criteria.from(ModuleRule.class);
    criteria.select(root);
    if (idNode != null) {
      criteria.where(builder.equal(root.get(ModuleRule_.ID_NODE), idNode));
    }

    return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<Rule> getRulesByUniqueAnswers(Set<Long> answers) {
    if(answers.isEmpty()){
      return Collections.EMPTY_LIST;
    }
    final Session session = sessionFactory.getCurrentSession();
    String JOIN_RULE = "select mr from ModuleRule mr " +
            " left join fetch mr.rule" +
            " where mr.idNode in :answers";
    List<ModuleRule> moduleRules = session.createQuery(JOIN_RULE, ModuleRule.class)
            .setParameter("answers", answers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();

    return moduleRules.stream().map(ModuleRule::getRule).filter(Objects::nonNull).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  public List<ModuleRule> getRulesByAgent(Long agentId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ModuleRule.class)
      .add(Restrictions.eq("idAgent", agentId));
    return crit.list();
  }
}
