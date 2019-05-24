package org.occideas.assessment.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.AssessmentAnswerSummary;
import org.occideas.utilities.PageUtil;
import org.occideas.vo.AssessmentAnswerSummaryFilterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssessmentDao implements IAssessmentDao {

  @Autowired
  private SessionFactory sessionFactory;
  @Autowired
  private PageUtil<AssessmentAnswerSummary> pageUtil;

  @Override
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

  @Override
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