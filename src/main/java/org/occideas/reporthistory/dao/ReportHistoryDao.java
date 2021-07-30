package org.occideas.reporthistory.dao;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.occideas.entity.InterviewRuleReport;
import org.occideas.entity.ReportHistory;
import org.occideas.utilities.ReportsStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ReportHistoryDao implements IReportHistoryDao {

  private final String interviewRuleReportFilterSQL = "SELECT CONCAT(CONVERT(i.idinterview, CHAR(250)), '_', CONVERT(f.idRule, CHAR(250))) as id,i.idinterview,i.referenceNumber,f.idRule,"
    + " a.name,CASE WHEN r.level = 0 THEN 'probHigh'" +
    " WHEN r.level = 1 THEN 'probMedium' " +
    " WHEN r.level = 2 THEN 'probLow' " +
    " WHEN r.level = 3 THEN 'probUnknown' " +
    " WHEN r.level = 4 THEN 'possUnknown' " +
    " WHEN r.level = 5 THEN 'noExposure' " +
    " END as level, nt.name as modName " +
    " FROM Interview i,Interview_FiredRules f," +
    " Rule r,AgentInfo a,Node_Rule nr,Node n," +
    " Node nt " +
    " where f.idinterview = i.idinterview " +
    " and f.idRule = r.idRule " +
    " and r.agentId = a.idAgent " +
    " and a.idAgent IN (:agentIds)" +
    " and nr.idRule = r.idRule " +
    " and nr.idNode = n.idNode " +
    " and nt.idNode = n.topNodeId " +
    " group by i.idinterview,i.referenceNumber,f.idRule, a.name, level, modName"
    + " order by i.referenceNumber";
  private final String interviewRuleReportSQL = "SELECT CONCAT(CONVERT(i.idinterview, CHAR(250)), '_', CONVERT(f.idRule, CHAR(250))) as id,i.idinterview,i.referenceNumber,f.idRule,"
    + " a.name,CASE WHEN r.level = 0 THEN 'probHigh'" +
    " WHEN r.level = 1 THEN 'probMedium' " +
    " WHEN r.level = 2 THEN 'probLow' " +
    " WHEN r.level = 3 THEN 'probUnknown' " +
    " WHEN r.level = 4 THEN 'possUnknown' " +
    " WHEN r.level = 5 THEN 'noExposure' " +
    " END as level, " +
    "  nt.name as modName " +
    " FROM Interview i,Interview_FiredRules f," +
    " Rule r,AgentInfo a," +
    " Node_Rule nr,Node n," +
    " Node nt " +
    " where f.idinterview = i.idinterview " +
    " and f.idRule = r.idRule " +
    " and r.agentId = a.idAgent " +
    " and nr.idRule = r.idRule " +
    " and nr.idNode = n.idNode " +
    " and nt.idNode = n.topNodeId";
  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public List<ReportHistory> getAll() {
    Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ReportHistory.class);
    crit.addOrder(Order.desc("id"));
    crit.add(Restrictions.ne("type", "Lookup"));
    return crit.list();
  }

  @Override
  public List<ReportHistory> getByType(String type) {
    Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ReportHistory.class);
    crit.add(Restrictions.eq("type", type));
    return crit.list();
  }

  @Override
  public ReportHistory getLatestByType(String type) {

    Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(ReportHistory.class)
      .addOrder(Order.desc("endDt"))
      .setMaxResults(1)
      .add(Restrictions.eq("type", type))
      .add(Restrictions.eq("status", ReportsStatusEnum.COMPLETED.getValue()));

    return (ReportHistory) crit.uniqueResult();
  }

  @Override
  public long getMaxId() {
    Session session = sessionFactory.getCurrentSession();
    DetachedCriteria maxId = DetachedCriteria.forClass(ReportHistory.class)
            .setProjection( Projections.max("id") );
    final List<ReportHistory> reports = session.createCriteria(ReportHistory.class)
            .add(Property.forName("id").eq(maxId))
            .list();
    if(!reports.isEmpty()){
      ReportHistory reportHistory = reports.get(0);
      return reportHistory.getId();
    }
    return 0L;
  }

  @Override
  public ReportHistory save(ReportHistory entity) {
    Session session = sessionFactory.getCurrentSession();
    session.saveOrUpdate(entity);
    return entity;
  }

  @Override
  public void delete(ReportHistory entity) {
    Session session = sessionFactory.getCurrentSession();
    session.delete(entity);
  }

  @Override
  public List<InterviewRuleReport> getInterviewRuleReportFilter(List<Long> agentIds) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(interviewRuleReportFilterSQL)
      .addEntity(InterviewRuleReport.class);
    sqlQuery.setParameterList("agentIds", agentIds);
    List<InterviewRuleReport> list = sqlQuery.list();
    return list;
  }

  @Override
  public List<InterviewRuleReport> getInterviewRuleReport() {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(interviewRuleReportSQL)
      .addEntity(InterviewRuleReport.class);
    List<InterviewRuleReport> list = sqlQuery.list();
    return list;
  }

}
