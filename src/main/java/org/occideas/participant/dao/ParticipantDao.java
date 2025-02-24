package org.occideas.participant.dao;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.occideas.entity.*;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.GenericFilterVO;
import org.occideas.vo.ParticipantVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ParticipantDao implements IParticipantDao {

  private final String paginatedParticipantSQL = "select distinct p.idParticipant,p.reference,p.status,p.lastUpdated,p.deleted,i.idinterview, i.assessedStatus"
    + "  from Participant p " + "  join Interview i " + " where p.idParticipant = i.idParticipant"
    + " and p.idParticipant like :idParticipant" + " and p.reference like :reference"
    + " and p.status like :status" + " and i.idinterview like :idinterview" + " and p.deleted = 0 and p.reference NOT LIKE '%-J%' ORDER BY p.idParticipant";
  private final String participantCountSQL = "select count(1) from (select distinct p.idParticipant,p.reference,p.status,p.lastUpdated,p.deleted,i.idinterview"
    + " from Participant p " + " join Interview i " + " where p.idParticipant = i.idParticipant"
    + " and p.idParticipant like :idParticipant" + " and p.reference like :reference"
    + " and p.status like :status" + " and i.idinterview like :idinterview" + " and p.deleted = 0 and p.reference NOT LIKE '%-J%') a";
  private final String paginatedParticipantWithModSQL = "select p.idParticipant,p.reference,p.status,p.lastUpdated,p.deleted,i.idinterview,i.assessedStatus,im.idModule"
    + ",im.interviewModuleName from Participant p " + " join Interview i join InterviewIntroModule_Module im "
    + " where p.idParticipant = i.idParticipant " + " and i.idinterview = im.interviewId "
    + " and p.idParticipant like :idParticipant" + " and p.reference like :reference"
    + " and p.status like :status"
    + " and coalesce(i.assessedStatus, '%%') like coalesce(:assessedStatus, '%%')"
    + " and i.idinterview like :idinterview" + " and im.interviewModuleName like :interviewModuleName"
    + " and im.moduleType != 'M_IntroModule'"
    + " and p.deleted = 0";
  private final String participantCountWithModule = "select count(*) from Participant p "
    + " join Interview i join InterviewIntroModule_Module im " + " where p.idParticipant = i.idParticipant "
    + " and i.idinterview = im.interviewId "
    + " and coalesce(p.idParticipant, '%%') like coalesce(:idParticipant, '%%')"
    + " and coalesce(p.reference, '%%') like coalesce(:reference, '%%')"
    + " and coalesce(p.status, '%%') like coalesce(:status, '%%')"
    + " and coalesce(i.idinterview, '%%') like coalesce(:idinterview, '%%')"
    + " and coalesce(i.assessedStatus, '%%') like coalesce(:assessedStatus, '%%')"
    + " and coalesce(im.interviewModuleName, '%%') like coalesce(:interviewModuleName, '%%')"
    + " and im.moduleType != 'M_IntroModule'"
    + " and p.deleted = 0";
  @Autowired
  private SessionFactory sessionFactory;
  private PageUtil<ParticipantVO> pageUtil = new PageUtil<>();

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Long save(Participant participant) {
    return (Long) sessionFactory.getCurrentSession().save(participant);
  }

  @Override
  public void delete(Participant participant) {

    sessionFactory.getCurrentSession().delete(participant);
  }

  @Override
  public Participant get(Long id) {
	  final Session session = sessionFactory.getCurrentSession();
	  Participant retValue = (Participant) session.get(Participant.class, id);
	  session.clear();
    return retValue;
  }

  @Override
  public Participant getByReferenceNumber(String referenceNumber) {

    CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
    CriteriaQuery<Participant> criteria = builder.createQuery(Participant.class);
    Root<Participant> root = criteria.from(Participant.class);
    final CriteriaQuery<Participant> select = criteria.select(root);
    select.where(builder.equal(root.get("reference"), referenceNumber),
            builder.equal(root.get("deleted"), 0));
    Query<Participant> query = sessionFactory.getCurrentSession().createQuery(criteria);
    
    if (!query.getResultList().isEmpty()) {
    	return (Participant) query.getResultList().get(0);
    }   
    return null;   
  }
  
  @Override
  public List<Participant> getByReferenceNumberPrefix(String referenceNumberPrefix) {

    CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
    CriteriaQuery<Participant> criteria = builder.createQuery(Participant.class);
    Root<Participant> root = criteria.from(Participant.class);
    final CriteriaQuery<Participant> select = criteria.select(root);
    select.where(builder.like(root.get("reference"), referenceNumberPrefix + "%"),
            builder.equal(root.get("deleted"), 0));
    Query<Participant> query = sessionFactory.getCurrentSession().createQuery(criteria);
    
    if (!query.getResultList().isEmpty()) {

      List<Participant> retValue = sortParticipantsByPriority(query.getResultList());
      return retValue;
    }   
    return null;   
  }

  private List<Participant> sortParticipantsByPriority(List<Participant> participants) {
    return participants.stream()
            .sorted(Comparator.comparingInt(participant -> {
              String priorityValue = participant.getParticipantDetails().stream()
                      .filter(detail -> "Priority".equals(detail.getDetailName()))
                      .map(ParticipantDetails::getDetailValue)
                      .findFirst()
                      .orElse(null);

              try {
                return priorityValue != null ? Integer.parseInt(priorityValue) : Integer.MAX_VALUE;
              } catch (NumberFormatException e) {
                return Integer.MAX_VALUE; // Non-integer values go to the end
              }
            }))
            .collect(Collectors.toList());
  }

  @Override
  public Participant merge(Interview participant) {
    return (Participant) sessionFactory.getCurrentSession().merge(participant);
  }

  @Override
  public void saveOrUpdate(Participant participant) {
    sessionFactory.getCurrentSession().saveOrUpdate(participant);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Participant> getAll() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Participant.class);

    return crit.list();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ParticipantIntMod> getPaginatedParticipantList(int pageNumber, int size, GenericFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(paginatedParticipantSQL).addEntity(ParticipantIntMod.class);
    sqlQuery.setFirstResult(pageUtil.calculatePageIndex(pageNumber, size));
    sqlQuery.setMaxResults(size);
    filter.applyFilter(filter, sqlQuery);
    List<ParticipantIntMod> list = sqlQuery.list();
    return list;
  }

  @Override
  public BigInteger getPaginatedParticipantTotalCount(GenericFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(participantCountSQL);
    filter.applyFilter(filter, sqlQuery);
    return (BigInteger) sqlQuery.uniqueResult();
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ParticipantIntMod> getPaginatedParticipantWithModList(int pageNumber, int size,
                                                                    GenericFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(paginatedParticipantWithModSQL).addEntity(ParticipantIntMod.class);
    sqlQuery.setFirstResult(pageUtil.calculatePageIndex(pageNumber, size));
    sqlQuery.setMaxResults(size);
    filter.applyFilter(filter, sqlQuery);
    List<ParticipantIntMod> list = sqlQuery.list();
    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<AssessmentIntMod> getPaginatedAssessmentWithModList(int pageNumber, int size, GenericFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(paginatedParticipantWithModSQL).addEntity(AssessmentIntMod.class);
    sqlQuery.setFirstResult(pageUtil.calculatePageIndex(pageNumber, size));
    sqlQuery.setMaxResults(size);
    filter.applyFilter(filter, sqlQuery);
    List<AssessmentIntMod> list = sqlQuery.list();
    return list;
  }

  @Override
  public BigInteger getParticipantWithModTotalCount(GenericFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(participantCountWithModule);
    filter.applyFilter(filter, sqlQuery);
    return (BigInteger) sqlQuery.uniqueResult();
  }

  @Override
  public BigInteger getAsssessmentWithModTotalCount(GenericFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    SQLQuery sqlQuery = session.createSQLQuery(participantCountWithModule);
    filter.applyFilter(filter, sqlQuery);
    return (BigInteger) sqlQuery.uniqueResult();
  }

  @Override
  public Long getMaxParticipantId() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Participant.class).addOrder(Order.desc("idParticipant"))
      .setMaxResults(1).setProjection(
        Projections.projectionList().add(Projections.property("idParticipant"), "idParticipant"));
    Long participantId = (Long) crit.uniqueResult();
    if (participantId == null) {
      return 1L;
    }
    return participantId;
  }

  @Override
  public String getMaxReferenceNumber() {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(Participant.class).addOrder(Order.desc("reference"))
      .setMaxResults(1).add(Restrictions.like("reference", "auto", MatchMode.START))
      .setProjection(Projections.projectionList().add(Projections.property("reference"), "reference"));
    String reference = (String) crit.uniqueResult();
		return reference;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteAll() {
    sessionFactory.getCurrentSession().createSQLQuery("truncate table Participant").executeUpdate();
  }

  @Override
  public void softDeleteAll() {
    final Session session = sessionFactory.getCurrentSession();
    Query<Participant> query =session.createQuery("update Participant p set p.deleted=1");
    query.executeUpdate();
  }
}
