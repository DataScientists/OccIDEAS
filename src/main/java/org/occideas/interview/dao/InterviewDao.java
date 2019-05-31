package org.occideas.interview.dao;

import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.occideas.entity.*;
import org.occideas.utilities.AssessmentStatusEnum;
import org.occideas.utilities.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class InterviewDao implements IInterviewDao {

  private final String ASSESSMENT_BASE_COUNT =
    " select count(*) from Participant p"
      + " join Interview i join InterviewIntroModule_Module im"
      + " where p.idParticipant = i.idParticipant"
      + " and i.idinterview = im.interviewId"
      + " and im.moduleType != 'M_IntroModule'"
      + " and p.deleted = 0";

  private final String NOT_ASSESSED_COUNT =
    ASSESSMENT_BASE_COUNT
      + " and i.assessedStatus like '" + Constant.NOT_ASSESSED + "'";

  private final String ASSESSED_COUNT =
    ASSESSMENT_BASE_COUNT
      + " and i.assessedStatus like '" + Constant.AUTO_ASSESSED + "'";

  private final String ANSWER_COUNT_QUERY = "select count(1)"
    + " from Interview_Answer a,"
    + " Interview_Question q"
    + " where a.answerId = :answerId and a.idinterview  = :idInterview"
    + " and a.idinterview = q.idinterview"
    + " and q.id = a.interviewQuestionId";

  private final String NOTES_QUERY_WITH_MODULE =
    " SELECT a.interviewId, b.referenceNumber,"
      + " GROUP_CONCAT(DISTINCT text SEPARATOR '++') as notes"
      + " FROM Note a, Interview b, InterviewIntroModule_Module c"
      + " where a.interviewId = b.idinterview"
      + " and a.interviewId = c.interviewId"
      + " and (c.idModule in (:modules))"
      + " group by a.interviewId";

  private final String NOTES_QUERY =
    " SELECT a.interviewId, b.referenceNumber,"
      + " GROUP_CONCAT(DISTINCT text SEPARATOR '++') as notes"
      + " FROM Note a, Interview b, InterviewIntroModule_Module c"
      + " where a.interviewId = b.idinterview"
      + " and a.interviewId = c.interviewId"
      + " group by a.interviewId";

  //Dynamic type column
  private final String NOTES_TYPE_COLUMN_QUERY =
    " CASE WHEN type = ':type' THEN a.text END as ':type'";

  private final String SELECT_NOTES_QUERY =
    " SELECT a.interviewId,"
      + " referenceNumber,"
      + " lastUpdated";

  private final String SELECT_NOTES_FROM =
    " FROM Note a, Interview b"
      + " where a.interviewId = b.idinterview"
      + " order by interviewId, lastUpdated";

  private final String SELECT_NOTES_WITH_MODULE_FROM =
    " FROM Note a, Interview b, InterviewIntroModule_Module c"
      + " where a.interviewId = b.idinterview and a.interviewId = c.interviewId"
      + " and (c.idModule in (:modules))"
      + " order by interviewId, lastUpdated";

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public void save(Interview interview) {
    sessionFactory.getCurrentSession().persist(interview);
  }

  @Override
  public void delete(Interview interview) {
    sessionFactory.getCurrentSession().delete(interview);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @Override
  public Interview get(Long id) {
    return (Interview) sessionFactory.getCurrentSession().get(Interview.class, id);
  }

  @Override
  public Interview merge(Interview interview) {
    return (Interview) sessionFactory.getCurrentSession().merge(interview);
  }

  @Override
  public void saveOrUpdate(Interview interview) {
    sessionFactory.getCurrentSession().saveOrUpdate(interview);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveNewTransaction(Interview interview) {
    sessionFactory.getCurrentSession().saveOrUpdate(interview);
  }

  @Override
  public List<Interview> getAll() {
    //No filter
    return getAllWithModules(null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> getAll(String assessmentStatus) {

    final Session session = sessionFactory.getCurrentSession();

    final Criteria crit = getInterviewCriteria(session);

    DetachedCriteria activeIntroQuery = DetachedCriteria.forClass(SystemProperty.class)
      .setProjection(Projections.property("value"))
      .add(Restrictions.eq("name", "activeIntro"));
    activeIntroQuery.getExecutableCriteria(session).setMaxResults(1);

    DetachedCriteria subquery = DetachedCriteria.forClass(InterviewIntroModuleModule.class, "iimm")
      .setProjection(Projections.property("interviewId"));
    subquery.add(Property.forName("iimm.idModule").ne(activeIntroQuery));

    crit.add(Property.forName("interview.idinterview").in(subquery));

    if (assessmentStatus != null) {
      if (Constant.NOT_ASSESSED.equals(assessmentStatus)) {
        crit.add(Restrictions.eq("assessedStatus", AssessmentStatusEnum.NOTASSESSED.getDisplay()));
      } else if (Constant.AUTO_ASSESSED.equals(assessmentStatus)) {
        crit.add(Restrictions.eq("assessedStatus", assessmentStatus));
      }
    }

    List<Interview> retValue = new ArrayList<Interview>();
    setFiredRules(retValue, crit.list());
    return retValue;
  }

  private Criteria getInterviewCriteria(final Session session) {
    final Criteria crit = session.createCriteria(Interview.class, "interview")
      .setProjection(Projections.projectionList()
        .add(Projections.property("fragment"), "fragment")
        .add(Projections.property("module"), "module")
        .add(Projections.property("moduleList"), "moduleList")
        .add(Projections.property("idinterview"), "idinterview")
        .add(Projections.property("referenceNumber"), "referenceNumber"))
      .addOrder(Order.asc("referenceNumber"))
      .setResultTransformer(Transformers.aliasToBean(Interview.class));
    return crit;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> getAllWithModules(String[] modules) {

    final Session session = sessionFactory.getCurrentSession();

    final Criteria crit = getInterviewCriteria(session);

    if (modules != null) {
      getSubQuery(modules, crit);
    }

    List<Interview> retValue = new ArrayList<Interview>();
    setFiredRules(retValue, crit.list());
    return retValue;
  }


  //TODO Fix this workaround and ask discuss with Jed about why hibernate is not populating firedRules
  private void setFiredRules(List<Interview> retValue, List<Interview> temp) {
    for (Interview interview : temp) {
      interview = this.get(interview.getIdinterview());
      if (interview.getParticipant().getDeleted() == 0) {
        retValue.add(interview);
      }
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> getAssessments() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Interview.class)
      .addOrder(Order.asc("referenceNumber"));
    List<Interview> retValue = new ArrayList<Interview>();
    List<Interview> temp = crit.list();
    for (Interview interview : temp) {
      interview = this.get(interview.getIdinterview()); //Todo fix this workaround and ask discuss with Jed about why hibernate is not populating firedRules
      interview.setFiredRules(interview.getFiredRules());
      retValue.add(interview);
    }
    return retValue;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> findByReferenceNumber(String referenceNumber) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria interviewCriteria = session.createCriteria(Interview.class)
      .setProjection(Projections.projectionList()
        .add(Projections.property("fragment"), "fragment")
        .add(Projections.property("module"), "module")
        .add(Projections.property("idinterview"), "idinterview")
        .add(Projections.property("referenceNumber"), "referenceNumber")
      )
      .add(Restrictions.eq("referenceNumber", referenceNumber))
      .createAlias("participant", "participant")
      .add(Restrictions.eq("participant.deleted", 0))
      .setResultTransformer(Transformers.aliasToBean(Interview.class));
    List<Interview> retValue = new ArrayList<Interview>();
    List<Interview> temp = interviewCriteria.list();
    setFiredRules(retValue, temp);
    return retValue;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @SuppressWarnings("unchecked")
  public List<Interview> getInterview(Long interviewId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Interview.class);
    if (interviewId != null) {
      crit.add(Restrictions.eq("idinterview", interviewId));
    }
    return crit.list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> getInterviews(Long[] interviewIds) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Interview.class);
    if (interviewIds != null) {
      crit.add(Restrictions.in("idinterview", interviewIds));
    }
    return crit.list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> getInterviewIdList() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Interview.class)
      .setProjection(Projections.projectionList()
        .add(Projections.property("idinterview"), "idinterview"))
      .setResultTransformer(Transformers.aliasToBean(Interview.class));
    List<Interview> list = crit.list();
    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> getAllInterviewsWithoutAnswers() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Interview.class)
      .setProjection(Projections.projectionList()
        .add(Projections.property("fragment"), "fragment")
        .add(Projections.property("module"), "module")
        .add(Projections.property("idinterview"), "idinterview")
        .add(Projections.property("assessedStatus"), "assessedStatus")
        .add(Projections.property("referenceNumber"), "referenceNumber"))
      .addOrder(Order.asc("referenceNumber"))
      .setResultTransformer(Transformers.aliasToBean(Interview.class));
    List<Interview> temp = crit.list();
    return temp;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> getAllInterviewsNotAssessed() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Interview.class)
      .setProjection(Projections.projectionList()
        .add(Projections.property("fragment"), "fragment")
        .add(Projections.property("module"), "module")
        .add(Projections.property("idinterview"), "idinterview")
        .add(Projections.property("referenceNumber"), "referenceNumber"))
      .add(Restrictions.eq("assessedStatus", AssessmentStatusEnum.NOTASSESSED.getDisplay()))
      .addOrder(Order.asc("referenceNumber"))
      .setResultTransformer(Transformers.aliasToBean(Interview.class));
    List<Interview> temp = crit.list();
    return temp;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Interview> getAllInterviewsAssessed() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Interview.class)
      .setProjection(Projections.projectionList()
        .add(Projections.property("fragment"), "fragment")
        .add(Projections.property("module"), "module")
        .add(Projections.property("idinterview"), "idinterview")
        .add(Projections.property("referenceNumber"), "referenceNumber"))
      .add(Restrictions.eq("assessedStatus", AssessmentStatusEnum.AUTOASSESSED.getDisplay()))
      .addOrder(Order.asc("referenceNumber"))
      .setResultTransformer(Transformers.aliasToBean(Interview.class));
    List<Interview> temp = crit.list();
    return temp;
  }

  @Override
  public Long getCountForModules(String[] modules) {

    final Session session = sessionFactory.getCurrentSession();

    final Criteria crit = session.createCriteria(Interview.class, "interview")
      .setProjection((Projections.rowCount()));

    if (modules != null) {

      getSubQuery(modules, crit);
    }

    return (Long) crit.uniqueResult();
  }

  private void getSubQuery(String[] modules, final Criteria crit) {

    DetachedCriteria subquery = DetachedCriteria.forClass(InterviewIntroModuleModule.class, "iimm")
      .setProjection(Projections.property("interviewId"))
      .add(Restrictions.in("iimm.idModule", CommonUtil.convertToLongList(modules)))
      .add(Restrictions.eqProperty("iimm.interviewId", "interview.idinterview"));

    crit.add(Property.forName("interview.idinterview").in(subquery));
  }

  @Override
  public BigInteger getAssessmentCount(String assessmentStatus) {

    final Session session = sessionFactory.getCurrentSession();

    String query = ASSESSMENT_BASE_COUNT;

    if (Constant.AUTO_ASSESSED.equals(assessmentStatus)) {
      query = ASSESSED_COUNT;
    } else if (Constant.NOT_ASSESSED.equals(assessmentStatus)) {
      query = NOT_ASSESSED_COUNT;
    }

    SQLQuery sqlQuery = session.createSQLQuery(query);
    sqlQuery.setMaxResults(1);

    return (BigInteger) sqlQuery.uniqueResult();
  }

  @Override
  public BigInteger getAnswerCount(Long interviewId, Long nodeId) {

    SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(ANSWER_COUNT_QUERY);
    sqlQuery.setMaxResults(1);
    sqlQuery.setParameter("idInterview", interviewId);
    sqlQuery.setParameter("answerId", nodeId);

    return (BigInteger) sqlQuery.uniqueResult();
  }

  @Override
  public List<Interview> getAssessmentsForNotes(String[] modules) {

    final Session session = sessionFactory.getCurrentSession();
    List<String> types = getNoteTypes();
    String[] typeColumns = getTypeColumns(types);

    if (typeColumns != null) {

      final Query sqlQuery = session.createSQLQuery(
        appendTypes(SELECT_NOTES_QUERY, typeColumns, modules));

      if (modules != null) {
        sqlQuery.setParameterList("modules", modules);
      }

      List<Object[]> rows = sqlQuery.list();

      List<Interview> result = new ArrayList();

      //Map manually
      for (Object[] row : rows) {

        Interview interview = new Interview();
        interview.setIdinterview(((BigInteger) row[0]).longValue());
        interview.setReferenceNumber(row[1].toString());

        ArrayList<Note> notes = new ArrayList<>();
        int j = 0;
        for (int i = 3; i < row.length; i++) {

          Note note = new Note();
          note.setText((String) row[i]);
          note.setType(types.get(j++));
          note.setLastUpdated((Date) row[2]);
          notes.add(note);
        }

        interview.setNotes(notes);

        result.add(interview);
      }

      return result;
    }

    return null;
  }

  private String appendTypes(String query, String[] typeColumns, String[] modules) {
    StringBuilder sb = new StringBuilder(query);
    for (String column : typeColumns) {
      sb.append(",");
      sb.append(column);
    }

    sb.append(modules != null ? SELECT_NOTES_WITH_MODULE_FROM : SELECT_NOTES_FROM);

    return sb.toString();
  }

  private String[] getTypeColumns(List<String> list) {
    String[] listColumn = null;

    if (list.size() > 0) {

      listColumn = new String[list.size()];

      for (int i = 0; i < list.size(); i++) {
        listColumn[i] = NOTES_TYPE_COLUMN_QUERY.replace(":type", list.get(i));
      }
    }

    return listColumn;
  }

  @Override
  public List<String> getNoteTypes() {

    final Session session = sessionFactory.getCurrentSession();

    final Query types = session.createSQLQuery("select distinct type from Note");

    return types.list();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<Long> getLinksByAnswerId(long answerId) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Node.class)
      .add(Restrictions.eq("parentId", String.valueOf(answerId)))
      .add(Restrictions.ne("link", 0L))
      .setProjection(Projections.projectionList()
        .add(Projections.property("link"), "link")
      );
    return crit.list();
  }

  @Override
  public List<Question> getLinksByModule(Long id) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Question.class)
      .add(Restrictions.eq("topNodeId", id))
      .add(Restrictions.ne("link", 0L));
    return crit.list();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("truncate table Interview").executeUpdate();
  }

}

