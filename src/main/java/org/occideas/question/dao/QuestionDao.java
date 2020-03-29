package org.occideas.question.dao;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Module;
import org.occideas.entity.Node;
import org.occideas.entity.NodesAgent;
import org.occideas.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class QuestionDao implements IQuestionDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private BaseDao baseDao;
  private String getNodesWithAgentSQL = " select concat(r.idRule, ':',n.idNode, ':',a.idAgent)"
    + " as primaryKey, r.idRule,n.idNode,a.idAgent from Node_Rule n,AgentInfo a, Rule r"
    + " where n.idRule = r.idRule" + " and a.idAgent = r.agentId" + " and r.deleted = 0" + " and a.deleted = 0"
    + " and a.idAgent = :param";

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrUpdate(Question question) {
    sessionFactory.getCurrentSession().saveOrUpdate(question);
  }

  @Override
  public Question get(long id) {
    return baseDao.get(Question.class, id);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrUpdateIgnoreFK(Question question) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
      .executeUpdate();
    sessionFactory.getCurrentSession().saveOrUpdate(question);
  }

  @Override
  public Question getQuestionByLinkIdAndTopId(long linkId, long topId) {
    CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
    CriteriaQuery<Question> criteria = builder.createQuery(Question.class);
    Root<Question> root = criteria.from(Question.class);
    final CriteriaQuery<Question> select = criteria.select(root);
    select.where(builder.equal(root.get("link"), linkId),
            builder.equal(root.get("topNodeId"), topId),
            builder.equal(root.get("deleted"), 0));
    Query<Question> query = sessionFactory.getCurrentSession().createQuery(criteria);
    return query.getSingleResult();
  }

  public Question getQuestionByModuleIdAndNumber(String parentId, String number) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Question.class).add(Restrictions.eq("parentId", parentId))
      .add(Restrictions.eq("number", number)).add(Restrictions.eq("deleted", 0));
    if (!crit.list().isEmpty()) {
      return (Question) crit.list().get(0);
    }
    return null;

  }

  public List<Question> getQuestionsByParentId(String parentId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Question.class).add(Restrictions.eq("parentId", parentId))
      .add(Restrictions.eq("deleted", 0));
    if (!crit.list().isEmpty()) {
      return crit.list();
    }
    return null;
  }

  public List<Question> getAllMultipleQuestions() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Question.class).add(Restrictions.eq("type", "Q_multiple"))
      .add(Restrictions.eq("deleted", 0));
    if (!crit.list().isEmpty()) {
      return crit.list();
    }
    return null;
  }

  public Module getModuleByParentId(Long idNode) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Module.class).add(Restrictions.eq("idNode", idNode))
      .add(Restrictions.eq("deleted", 0));
    if (!crit.list().isEmpty()) {
      return (Module) crit.list().get(0);
    }
    return null;
  }

  public Question findMultipleQuestion(long questionId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Question.class).add(Restrictions.eq("type", "Q_multiple"))
      .add(Restrictions.eq("idNode", questionId)).add(Restrictions.eq("deleted", 0));
    List list = crit.list();
    if (!list.isEmpty()) {
      return (Question) list.get(0);
    }
    return null;
  }

  public Node getTopModuleByTopNodeId(long topNodeId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Node.class).add(Restrictions.eq("idNode", topNodeId));
    List list = crit.list();
    if (!list.isEmpty()) {
      return (Node) list.get(0);
    }
    return null;
  }

  public List<NodesAgent> getNodesWithAgent(long agentId) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getNodesWithAgentSQL).addEntity(NodesAgent.class);
    sqlQuery.setParameter("param", agentId);
    List<NodesAgent> list = sqlQuery.list();
    return list;
  }

  @Override
  public Question get(Class<Question> type, Long id) {
    return baseDao.get(type, id);
  }

}
