package org.occideas.interview.dao;

import org.hibernate.Session;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.InterviewResults;
import org.occideas.entity.InterviewResults_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class InterviewResultDao extends GenericBaseDao<InterviewResults, Long> {

    public InterviewResultDao() {
        super(InterviewResults.class, InterviewResults_.INTERVIEW_ID);
    }

    public InterviewResults findByReferenceNumber(String referenceNumber) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<InterviewResults> criteria = builder.createQuery(InterviewResults.class);
        Root<InterviewResults> root = criteria.from(InterviewResults.class);
        criteria.select(root);
        criteria.where(builder.and(
                builder.equal(root.get(InterviewResults_.REFERENCE_NUMBER), referenceNumber)
        ));
        List<InterviewResults> resultList = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    public void deleteByReferenceNumber(String referenceNumber) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<InterviewResults> criteria = builder.createCriteriaDelete(InterviewResults.class);
        Root<InterviewResults> root = criteria.from(InterviewResults.class);
        criteria.where(builder.and(
                builder.equal(root.get(InterviewResults_.REFERENCE_NUMBER), referenceNumber)
        ));
        sessionFactory.getCurrentSession().createQuery(criteria).executeUpdate();
    }
}