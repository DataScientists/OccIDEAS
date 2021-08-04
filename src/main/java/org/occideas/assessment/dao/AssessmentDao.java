package org.occideas.assessment.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

  public Long getAnswerSummaryTotalCount(AssessmentAnswerSummaryFilterVO filter) {
    final Session session = sessionFactory.getCurrentSession();
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
    Root<AssessmentAnswerSummary> root = criteria.from(AssessmentAnswerSummary.class);

    List<Predicate> listOfRestrictions = filter.getListOfRestrictions(builder,root);

    criteria.select(builder.count(root.get(AssessmentAnswerSummary_.primaryKey)));
    criteria.where(listOfRestrictions.toArray(new Predicate[0]));

    return sessionFactory.getCurrentSession().createQuery(criteria).getSingleResult();
  }
}