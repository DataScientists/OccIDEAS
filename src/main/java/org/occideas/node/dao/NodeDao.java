package org.occideas.node.dao;

import org.hibernate.SessionFactory;
import org.occideas.entity.Node;
import org.occideas.entity.NodePlain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class NodeDao implements INodeDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public Node getNode(Long id) {
    Node node = (Node) sessionFactory.getCurrentSession().get(Node.class, id);
    node.getChildNodes();
    return node;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveBatchNodes(List<Node> nodes) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    for (Node node : nodes) {
      sessionFactory.getCurrentSession().save(node);
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    sessionFactory.getCurrentSession().createSQLQuery(
      "truncate table Node").executeUpdate();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveBatchNodesPlain(List<NodePlain> nodes) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    for (NodePlain node : nodes) {
      sessionFactory.getCurrentSession().save(node);
    }
  }

}
