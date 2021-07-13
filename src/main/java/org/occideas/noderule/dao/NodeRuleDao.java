package org.occideas.noderule.dao;

import org.hibernate.SessionFactory;
import org.occideas.entity.NodeRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.List;

@Repository
public class NodeRuleDao implements INodeRuleDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public void saveOrUpdate(NodeRule nodeRule) {
    sessionFactory.getCurrentSession().saveOrUpdate(nodeRule);
  }

  @Override
  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void saveBatchNodeRule(List<NodeRule> nrules) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    for (NodeRule rule : nrules) {
      sessionFactory.getCurrentSession().save(rule);
    }
  }

  @Override
  public void save(NodeRule nodeRule) {
    sessionFactory.getCurrentSession().save(nodeRule);
  }

  @Override
  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("truncate table Node_Rule").executeUpdate();
  }

}
