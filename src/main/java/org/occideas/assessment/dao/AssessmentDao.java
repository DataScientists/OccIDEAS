package org.occideas.assessment.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.entity.AssessmentAnswerSummary_;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class AssessmentDao {

  @Autowired
  private SessionFactory sessionFactory;

  private PageUtil<AssessmentAnswerSummary> pageUtil = new PageUtil<>();

  public List<AssessmentAnswerSummary> getAnswerSummary(AssessmentAnswerSummaryFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<AssessmentAnswerSummary> criteria = builder.createQuery(AssessmentAnswerSummary.class);
    Root<AssessmentAnswerSummary> root = criteria.from(AssessmentAnswerSummary.class);

    List<Predicate> listOfRestrictions = filter.getListOfRestrictions(builder,root);

    criteria.where(listOfRestrictions.toArray(new Predicate[0]));

    return sessionFactory.getCurrentSession().createQuery(criteria)
            .setFirstResult(pageUtil.calculatePageIndex(filter.getPageNumber(),
                    filter.getSize()))
            .setMaxResults(filter.getSize())
            .getResultList();
  }

  public List<AssessmentAnswerSummary> getAnswerSummaryByName(AssessmentAnswerSummaryFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(AssessmentAnswerSummary.class);
//		.setProjection(
//			    Projections.distinct(Projections.projectionList()
//			    	    .add(Projections.property("idinterview"), "idinterview")
//			    	    .add(Projections.property("answerId"), "answerId")
//			    	    .add(Projections.property("name"), "name")
//			    	    .add(Projections.property("idParticipant"), "idParticipant")
//			    	    .add(Projections.property("reference"), "reference")
//			    	    .add(Projections.property("assessedStatus"), "assessedStatus")
//			    	    .add(Projections.property("status"), "status")));
    if (filter.getAnswerId() != null) {
      crit.add(Restrictions.eq("answerId", filter.getAnswerId()));
    }
    if (filter.getName() != null) {
      crit.add(Restrictions.eq("name", filter.getName()));
    }
    if (filter.getModuleName() != null) {
      crit.add(Restrictions.eq("interviewModuleName", filter.getModuleName()));
    }
    if (filter.getIdinterview() != null) {
      crit.add(Restrictions.like("idinterview", filter.getIdinterview()));
    }
    if (filter.getIdParticipant() != null) {
      crit.add(Restrictions.like("idParticipant", filter.getIdParticipant()));
    }
    if (filter.getReference() != null) {
      crit.add(Restrictions.like("reference", filter.getReference(), MatchMode.ANYWHERE));
    }
    if (filter.getAssessedStatus() != null) {
      crit.add(Restrictions.like("assessedStatus", filter.getAssessedStatus(), MatchMode.ANYWHERE));
    }
    if (filter.getStatus() != null) {
      crit.add(Restrictions.like("status", filter.getStatus()));
    }
    crit.setFirstResult(pageUtil.calculatePageIndex(filter.getPageNumber(),
      filter.getSize()));
    crit.setMaxResults(filter.getSize());
    return crit.list();
  }

  public Long getAnswerSummaryByNameTotalCount(AssessmentAnswerSummaryFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    final Criteria crit = session.createCriteria(AssessmentAnswerSummary.class);
    if (filter.getAnswerId() != null) {
      crit.add(Restrictions.eq("answerId", filter.getAnswerId()));
    }
    if (filter.getName() != null) {
      crit.add(Restrictions.eq("name", filter.getName()));
    }
    if (filter.getIdinterview() != null) {
      crit.add(Restrictions.like("idinterview", filter.getIdinterview()));
    }
    if (filter.getIdParticipant() != null) {
      crit.add(Restrictions.like("idParticipant", filter.getIdParticipant()));
    }
    if (filter.getReference() != null) {
      crit.add(Restrictions.like("reference", filter.getReference(), MatchMode.ANYWHERE));
    }
    if (filter.getModuleName() != null) {
      crit.add(Restrictions.eq("interviewModuleName", filter.getModuleName()));
    }
    if (filter.getAssessedStatus() != null) {
      crit.add(Restrictions.like("assessedStatus", filter.getAssessedStatus(), MatchMode.ANYWHERE));
    }
    if (filter.getStatus() != null) {
      crit.add(Restrictions.like("status", filter.getStatus()));
    }
    crit.setProjection(Projections.rowCount());
    return (Long) crit.uniqueResult();
  }
}