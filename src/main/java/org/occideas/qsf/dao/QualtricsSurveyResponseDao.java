package org.occideas.qsf.dao;

import org.hibernate.Session;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.QualtricsSurveyResponse;
import org.occideas.entity.QualtricsSurveyResponse_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class QualtricsSurveyResponseDao extends GenericBaseDao<QualtricsSurveyResponse, Long> {

    public QualtricsSurveyResponseDao() {
        super(QualtricsSurveyResponse.class, QualtricsSurveyResponse_.ID);
    }

    public QualtricsSurveyResponse findBySurveyAndResponseId(String surveyId, String responseId) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<QualtricsSurveyResponse> criteria = builder.createQuery(QualtricsSurveyResponse.class);
        Root<QualtricsSurveyResponse> root = criteria.from(QualtricsSurveyResponse.class);
        criteria.select(root);
        criteria.where(builder.and(
                builder.equal(root.get(QualtricsSurveyResponse_.SURVEY_ID), surveyId),
                builder.equal(root.get(QualtricsSurveyResponse_.RESPONSE_ID), responseId)
        ));
        List<QualtricsSurveyResponse> resultList = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
}
