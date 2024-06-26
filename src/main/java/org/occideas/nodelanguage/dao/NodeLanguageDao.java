package org.occideas.nodelanguage.dao;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.util.List;

@Repository
public class NodeLanguageDao implements INodeLanguageDao {

  String getTotalUntranslatedModuleSQL = "select count(distinct n1.name) from " + "Node n1 where n1.link = 0 "
    + "and n1.type not like '%frequency%' " + "and n1.type != 'P_freetext' " + "and n1.deleted = 0 "
    + "and n1.topNodeId in (select idNode from Node " + "where node_discriminator = 'M' and type in ('M_Module','M_IntroModule') and deleted = 0 "
    + "order by name) ";
  String getUntranslatedModulesSQL = "select * from Node where " + "node_discriminator = 'M' and type in ('M_Module','M_IntroModule') and deleted = 0 "
    + "and idNode in " + "(select idNode from NodeNodeLanguageMod where flag = :param)";
  String getTotalModuleCountSQL = "select count(1) from Node where " + "node_discriminator = 'M' "
    + "and type in ('M_Module','M_IntroModule') and deleted = 0 ";
  String getTotalFragCountSQL = "select count(1) from Node where " + "node_discriminator = 'F' " + "and deleted = 0 ";
  String getUntranslatedFragmentsSQL = "select * from Node where " + "node_discriminator = 'F' " + "and deleted = 0 "
    + "and idNode in " + "(select idNode from NodeNodeLanguageFrag where flag = :param)";
  String getTotalUntranslatedFragmentSQL = "select count(distinct n1.name) from " + "Node n1 where n1.link = 0 "
    + "and n1.type not like '%frequency%' " + "and n1.type != 'P_freetext' " + "and n1.deleted = 0 "
    + "and n1.topNodeId in (select idNode from Node " + "where node_discriminator = 'F' " + "and deleted = 0 "
    + "order by name) ";
  String getLanguageModBreakdownSQL = " select idNode,name," +
    " (select nf.current from NodeNodeLanguageMod nf" +
    " where nf.idNode = n.idNode and nf.flag = :param) as current," +
    " (select count(distinct n1.name) from" +
    " Node n1 " +
    " where n1.link = 0" +
    " and n1.type not like '%frequency%'" +
    " and n1.type != 'P_freetext'" +
    " and n1.deleted = 0" +
    " and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end) as total from Node n" +
    " where node_discriminator = 'M' and type in ('M_Module','M_IntroModule')";
  String getModulesIdNodeSQL = " select *" +
    " from Node " +
    " where node_discriminator = 'M' and type in ('M_Module','M_IntroModule') AND deleted=0 ORDER BY name";
  String getFragmentIdNodeSQL = " select *" +
    " from Node " +
    " where node_discriminator = 'F' and type in "
    + "('F_template','F_ajsm') AND deleted=0 ORDER BY name";
  String getLanguageFragBreakdownSQL = " select idNode,name," +
    " (select nf.current from NodeNodeLanguageFrag nf" +
    " where nf.idNode = n.idNode and nf.flag = :param) as current," +
    " (select count(distinct n1.name) from" +
    " Node n1 " +
    " where n1.link = 0" +
    " and n1.type not like '%frequency%'" +
    " and n1.type != 'P_freetext'" +
    " and n1.deleted = 0" +
    " and case n.topNodeId when 0 then n1.topNodeId = n.idNode else n1.topNodeId = n.topNodeId end) as total from Node n" +
    " where  node_discriminator = 'F'";
  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public void addLanguage(Language entity) {
    sessionFactory.getCurrentSession().saveOrUpdate(entity);
  }

  @Override
  public List<Language> getAllLanguage() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Language.class);
    return crit.list();
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public void save(NodeLanguage entity) {
    sessionFactory.getCurrentSession().saveOrUpdate(entity);
  }

  @Override
  public void batchSave(List<NodeLanguage> entityList) {
    for (NodeLanguage nl : entityList) {
      NodeLanguage nodeLanguageByWordAndLanguage = getNodeLanguageByWordAndLanguage(nl.getWord(), nl.getLanguageId());
      if (nodeLanguageByWordAndLanguage != null) {
        nl.setId(nodeLanguageByWordAndLanguage.getId());
      }
      sessionFactory.getCurrentSession().merge(nl);
    }
  }

  @Override
  public void saveTranslate(Translate entity) {
    sessionFactory.getCurrentSession().saveOrUpdate(entity);
  }

  @Override
  public void delete(NodeLanguage entity) {
    sessionFactory.getCurrentSession().delete(entity);
  }

  @Override
  public NodeLanguage get(Long id) {
    return (NodeLanguage) sessionFactory.getCurrentSession().get(NodeLanguage.class, id);
  }

  @Override
  public List<NodeLanguage> getNodeLanguageById(Long id) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(NodeLanguage.class).add(Restrictions.eq("languageId", id));
    return crit.list();
  }

  @Override
  public List<String> getNodeLanguageWordsByIdOrderByWord(Long id) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(NodeLanguage.class).
      add(Restrictions.eq("languageId", id)).
      addOrder(Order.asc("word"))
      .setProjection(Projections.projectionList().
        add(Projections.property("word"), "word"));
    return crit.list();
  }

  @Override
  public List<NodeLanguage> getNodeLanguageByIdandLanguage(Long id, Long languageid) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(NodeLanguage.class).add(Restrictions.eq("languageId", id))
      .add(Restrictions.eq("languageId", languageid));
    return crit.list();
  }

  @Override
  public List<NodeLanguage> getNodesByLanguage(String language) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(NodeLanguage.class).add(Restrictions.eq("language", language));
    return crit.list();
  }

  @Override
  public NodeLanguage getNodesByLanguageAndWord(Long language, String word) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(NodeLanguage.class).add(Restrictions.eq("languageId", language))
      .add(Restrictions.eq("word", word));
    List list = crit.list();
    if (!list.isEmpty()) {
      return (NodeLanguage) list.get(0);
    }

    return null;
  }

  @Override
  public List<Long> getDistinctNodeLanguageId() {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(NodeLanguage.class).setProjection(Projections
      .distinct(Projections.projectionList().add(Projections.property("languageId"), "languageId")));
    List list = crit.list();
    return list;
  }

  @Override
  public List<Language> getDistinctLanguage(List<Long> ids) {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(Language.class).add(Restrictions.in("id", ids));
    List list = crit.list();
    return list;
  }

  @Override
  public Language getLanguageById(Long id) {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(Language.class).add(Restrictions.eq("id", id));
    Object uniqueResult = crit.uniqueResult();
    if (uniqueResult != null) {
      return (Language) uniqueResult;
    }
    return null;
  }

  @Override
  public List<NodeNodeLanguageMod> getNodeNodeLanguageListMod() {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(NodeNodeLanguageMod.class);
    List list = crit.list();
    return list;
  }

  @Override
  public List<NodeNodeLanguageFrag> getNodeNodeLanguageListFrag() {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(NodeNodeLanguageFrag.class);
    List list = crit.list();
    return list;
  }

  @Override
  public NodeNodeLanguageMod getNodeNodeLanguageMod(long idNode, String flag) {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(NodeNodeLanguageMod.class).add(Restrictions.eq("idNode", idNode))
      .add(Restrictions.eq("flag", "bfh-flag-" + flag));
    Object uniqueResult = crit.uniqueResult();
    if (uniqueResult != null) {
      return (NodeNodeLanguageMod) uniqueResult;
    }
    return null;
  }

  @Override
  public NodeLanguage getNodeLanguageByWordAndLanguage(String word, long languageId) {
    final Session session = sessionFactory.getCurrentSession();
    Criteria crit = session.createCriteria(NodeLanguage.class).add(Restrictions.eq("word", word))
      .add(Restrictions.eq("languageId", languageId))
      .addOrder(Order.desc("lastUpdated"));
    List<NodeLanguage> list = crit.list();
    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public Integer getTotalUntranslatedModule() {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getTotalUntranslatedModuleSQL);

    BigInteger total = (BigInteger) sqlQuery.uniqueResult();
    return total.intValue();
  }

  @Override
  public List<JobModule> getUntranslatedModules(String flag) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getUntranslatedModulesSQL).addEntity(JobModule.class);
    sqlQuery.setParameter("param", flag);
    List<JobModule> list = sqlQuery.list();
    return list;
  }

  @Override
  public Integer getTotalModuleCount() {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getTotalModuleCountSQL);

    BigInteger total = (BigInteger) sqlQuery.uniqueResult();
    return total.intValue();
  }

  @Override
  public Integer getTotalFragmentCount() {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getTotalFragCountSQL);

    BigInteger total = (BigInteger) sqlQuery.uniqueResult();
    return total.intValue();
  }

  @Override
  public List<Fragment> getUntranslatedFragments(String flag) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getUntranslatedFragmentsSQL).addEntity(Fragment.class);
    sqlQuery.setParameter("param", flag);
    List<Fragment> list = sqlQuery.list();
    return list;
  }

  @Override
  public Integer getTotalUntranslatedFragment() {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getTotalUntranslatedFragmentSQL);

    BigInteger total = (BigInteger) sqlQuery.uniqueResult();
    return total.intValue();
  }

  @Override
  public List<LanguageModBreakdown> getLanguageModBreakdown(String flag) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getLanguageModBreakdownSQL)
      .addEntity(LanguageModBreakdown.class);
    sqlQuery.setParameter("param", flag);
    List<LanguageModBreakdown> list = sqlQuery.list();
    return list;
  }

  @Override
  public List<JobModule> getModulesIdNodeSQL() {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getModulesIdNodeSQL)
      .addEntity(JobModule.class);
    List<JobModule> list = sqlQuery.list();
    return list;
  }

  @Override
  public List<Fragment> getFragmentIdNodeSQL() {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getFragmentIdNodeSQL)
      .addEntity(Fragment.class);
    List<Fragment> list = sqlQuery.list();
    return list;
  }

  @Override
  public List<LanguageFragBreakdown> getLanguageFragBreakdown(String flag) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(getLanguageFragBreakdownSQL)
      .addEntity(LanguageFragBreakdown.class);
    sqlQuery.setParameter("param", flag);
    List<LanguageFragBreakdown> list = sqlQuery.list();
    return list;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("truncate table Node_Language").executeUpdate();
  }
}
