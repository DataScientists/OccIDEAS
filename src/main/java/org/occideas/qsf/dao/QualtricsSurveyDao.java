package org.occideas.qsf.dao;

import org.hibernate.Session;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.QualtricsSurvey;
import org.occideas.entity.QualtricsSurvey_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class QualtricsSurveyDao extends GenericBaseDao<QualtricsSurvey, String> {

    public QualtricsSurveyDao() {
        super(QualtricsSurvey.class, QualtricsSurvey_.RESPONSE_ID);
    }

    public List<QualtricsSurvey> findByIsProcessed(boolean isProcessed) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<QualtricsSurvey> criteria = builder.createQuery(QualtricsSurvey.class);
        Root<QualtricsSurvey> root = criteria.from(QualtricsSurvey.class);
        criteria.select(root);
        criteria.where(builder.and(
                builder.equal(root.get(QualtricsSurvey_.IS_PROCESSED), isProcessed),
                builder.isNotNull(root.get(QualtricsSurvey_.RESPONSE))
        ));
        return sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
    }
}