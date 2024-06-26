package org.occideas.module.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.JobModule;
import org.occideas.entity.JobModule_;
import org.occideas.entity.Node;
import org.occideas.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;

@Repository
public class ModuleDao implements IModuleDao {

  private final String GET_NODE_BY_LINK_AND_MOD_ID = "SELECT * FROM Node where idNode " +
    " in (select parent_idNode from Node where link = :link and topNodeId = :modId"
    + " and deleted = 0)";
  private final String GET_NODE_BY_TYPE = "SELECT * from Node where " +
    " type = :type" +
    " and deleted = 0";
  private final String GET_LINKING_QUESTION_BY_MOD_ID = "select * from Node where link "
    + "= :link and topNodeId = :modId and deleted = 0";
  private final String GET_ALL_LINKING_QUESTION_BY_MOD_ID = "SELECT distinct n.* "
    + "FROM Node n, ModuleRule mr "
    + "WHERE n.topNodeId=:modId and n.link>0 and n.deleted=0 and n.link=mr.idModule "
    + "AND mr.idAgent in (select value from SYS_CONFIG where type='studyagent')";
  private final String DISTINCT_NODE_NAME_BY_IDNODE = "select * from Node where " +
    "topNodeId = :idNode and type != 'P_freetext' " +
    "and type not like '%frequency%' " +
    "and deleted = 0 and link = 0 " +
    //        "group by name "+
    "order by name";
  private final String NODE_NAME_BY_IDNODE = "select name from Node where " +
    "topNodeId = :idNode and type != 'P_freetext' " +
    "and type not like '%frequency%' " +
    "and deleted = 0 and link = 0 " +
    //        "group by name "+
    "order by name";
  @Autowired
  private SessionFactory sessionFactory;

  public JobModule save(JobModule module) {
    return (JobModule) sessionFactory.getCurrentSession().save(module);
  }

  //    @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveCopy(JobModule module) {
    sessionFactory.getCurrentSession().save(module);
  }

  public void delete(JobModule module) {
    sessionFactory.getCurrentSession().delete(module);
  }

  public List<JobModule> findByName(String name) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(JobModule.class)
      .add(Restrictions.eq("name", name));
    return crit.list();
  }

  public JobModule get(Long id) {
    return (JobModule) sessionFactory.getCurrentSession().get(JobModule.class, id);
  }

  public JobModule merge(JobModule module) {
    return (JobModule) sessionFactory.getCurrentSession().merge(module);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrUpdate(JobModule module) {
    sessionFactory.getCurrentSession().saveOrUpdate(module);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void saveOrUpdateIgnoreFK(JobModule module) {
    sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
            .executeUpdate();
    sessionFactory.getCurrentSession().saveOrUpdate(module);
  }

  @Override
  public JobModule getModuleByName(String name) {
    final Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<JobModule> criteria = builder.createQuery(JobModule.class);
    Root<JobModule> root = criteria.from(JobModule.class);
    criteria.select(root);
    criteria.where(builder.and(builder.equal(root.get(JobModule_.NAME), name), builder.equal(root.get(JobModule_.DELETED), 0)));

    return sessionFactory.getCurrentSession().createQuery(criteria).getSingleResult();
  }

  @Override
  public List<JobModule> findByNameLength(String name) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(JobModule.class)
            .add(Restrictions.like("name", name, MatchMode.START))
            .add(Restrictions.eq("deleted", 0));
    return crit.list();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public long create(JobModule module) {
    sessionFactory.getCurrentSession().save(module);
    return module.getIdNode();
  }

  @SuppressWarnings("unchecked")
  public List<JobModule> getAll(boolean isIncludeChild) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(JobModule.class);

    //      if(!isIncludeChild){
    //    	  crit.setProjection(Projections.projectionList()
    //			.add(Projections.property("idNode"),"idNode")
    //			.add(Projections.property("name"),"name")
    //			.add(Projections.property("description"),"description"));
    //
    //      }
    crit.add(Restrictions.eq("deleted", 0));
    //      crit.addOrder(Order.asc("name"));

    return crit.list();
  }

  @SuppressWarnings("unchecked")
  public List<JobModule> getAllActive() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(JobModule.class)
      .addOrder(Order.asc("name"))
      .add(Restrictions.eq("deleted", 0))
      .setProjection(Projections.projectionList()
        .add(Projections.property("idNode"), "idNode")
        .add(Projections.property("type"), "type")
        .add(Projections.property("name"), "name")
        .add(Projections.property("description"), "description"))
      .setResultTransformer(Transformers.aliasToBean(JobModule.class));
    return crit.list();
  }

  @SuppressWarnings("unchecked")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Long generateIdNode() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Node.class)
      .addOrder(Order.desc("idNode"))
      .setMaxResults(1)
      .setProjection(Projections.projectionList()
        .add(Projections.property("idNode"), "idNode"))
      .setResultTransformer(Transformers.aliasToBean(Node.class));
    List<Node> list = (List<Node>) crit.list();
    Long idNode = 0l;
    if (list.isEmpty()) {
      Query query = session.createSQLQuery("SELECT AUTO_INCREMENT FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = \'occideas\' AND   TABLE_NAME   = \'Node\'");
      BigInteger id = (BigInteger) query.list().get(0);
      idNode = id.longValue();
    } else {
      idNode = list.get(0).getIdNode();
    }
    return idNode;
  }

  @Override
  public Node getNodeById(Long idNode) {
    return (Node) sessionFactory.getCurrentSession().get(Node.class, idNode);
  }

  @Override
  public String getNodeNameById(Long idNode) {
    sessionFactory.getCurrentSession().get(Node.class, idNode);
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Node.class)
      .add(Restrictions.eq("idNode", idNode))
      .setProjection(Projections.property("name"));
    String name = (String) crit.uniqueResult();
    if (StringUtils.isEmpty(name)) {
      return "";
    } else {
      return name;
    }
  }

  @Override
  public List<? extends Node> getNodeByLinkAndModId(Long link, Long modId) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(GET_NODE_BY_LINK_AND_MOD_ID).addEntity(Node.class);
    sqlQuery.setParameter("modId", String.valueOf(modId));
    sqlQuery.setParameter("link", String.valueOf(link));
    List<Node> list = sqlQuery.list();
    return list;
  }

  @Override
  public List<? extends Node> getNodeByType(String type) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(GET_NODE_BY_TYPE).addEntity(Node.class);
    sqlQuery.setParameter("type", type);
    List<Node> list = sqlQuery.list();
    return list;
  }

  @Override
  public Question getLinkingQuestionByModId(Long link, Long modId) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(GET_LINKING_QUESTION_BY_MOD_ID).addEntity(Node.class);
    sqlQuery.setParameter("modId", String.valueOf(modId));
    sqlQuery.setParameter("link", String.valueOf(link));
    List<Question> list = sqlQuery.list();
    if (!list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public List<Question> getAllLinkingQuestionByModId(Long modId) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(GET_ALL_LINKING_QUESTION_BY_MOD_ID).addEntity(Node.class);
    sqlQuery.setParameter("modId", String.valueOf(modId));
    List<Question> list = sqlQuery.list();
    //if (!list.isEmpty()) {
    return list;
    //}
    //return null;
  }

  @Override
  public List<? extends Node> getDistinctNodeNameByIdNode(String idNode) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(DISTINCT_NODE_NAME_BY_IDNODE).addEntity(Node.class);
    sqlQuery.setParameter("idNode", idNode);
    List<Node> list = sqlQuery.list();
    return list;
  }

  @Override
  public List<String> getNodeNameByIdNode(String idNode) {
    final Session session = sessionFactory.getCurrentSession();
    Query sqlQuery = session.createSQLQuery(NODE_NAME_BY_IDNODE);
    sqlQuery.setParameter("idNode", idNode);
    List<String> list = (List<String>) sqlQuery.list();
    return list;
  }


  @Override
  public List<Question> getChildFrequencyNodes(String idNode) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Question.class)
      .add(Restrictions.eq("parentId", idNode))
      .add(Restrictions.like("type", "frequency", MatchMode.ANYWHERE));
    return crit.list();
  }


  @Override
  public List<Question> getChildLinkNodes(String idNode) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Question.class)
      .add(Restrictions.eq("parentId", idNode))
      .add(Restrictions.like("type", "linked", MatchMode.ANYWHERE));
    return crit.list();
  }

}
