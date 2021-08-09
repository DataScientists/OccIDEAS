package org.occideas.qsf.dao;

import org.hibernate.Session;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.QualtricsSurveySubscription;
import org.occideas.entity.QualtricsSurveySubscription_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class QualtricsSurveySubscriptionDao extends GenericBaseDao<QualtricsSurveySubscription, String> {

    public QualtricsSurveySubscriptionDao() {
        super(QualtricsSurveySubscription.class);
    }

    public Optional<QualtricsSurveySubscription> findBySurveyId(String surveyId) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<QualtricsSurveySubscription> criteria = builder.createQuery(QualtricsSurveySubscription.class);
        Root<QualtricsSurveySubscription> root = criteria.from(QualtricsSurveySubscription.class);
        criteria.select(root);
        criteria.where(builder.equal(root.get(QualtricsSurveySubscription_.SURVEY_ID), surveyId));

        List<QualtricsSurveySubscription> resultList = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }
}
