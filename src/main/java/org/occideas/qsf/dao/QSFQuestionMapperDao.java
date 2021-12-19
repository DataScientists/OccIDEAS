package org.occideas.qsf.dao;

import org.hibernate.Session;
import org.occideas.base.dao.GenericBaseDao;
import org.occideas.entity.QSFQuestionMapper;
import org.occideas.entity.QSFQuestionMapperId;
import org.occideas.entity.QSFQuestionMapperId_;
import org.occideas.entity.QSFQuestionMapper_;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Transactional
public class QSFQuestionMapperDao extends GenericBaseDao<QSFQuestionMapper, QSFQuestionMapperId> {

    public QSFQuestionMapperDao() {
        super(QSFQuestionMapper.class, QSFQuestionMapper_.ID);
    }

    public Map<String, QSFQuestionMapper> getQuestionBySurveyId(String surveyId) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<QSFQuestionMapper> criteria = builder.createQuery(QSFQuestionMapper.class);
        Root<QSFQuestionMapper> root = criteria.from(QSFQuestionMapper.class);
        criteria.select(root);
        criteria.where(builder.and(
                builder.equal(root.get(QSFQuestionMapper_.ID).get(QSFQuestionMapperId_.SURVEY_ID), surveyId)
        ));
        List<QSFQuestionMapper> resultList = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
        return resultList.stream().collect(Collectors.toMap(qsfQuestionMapper -> qsfQuestionMapper.getId().getQuestionId(), Function.identity()));
    }

    public void deleteBySurveyId(String surveyId) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<QSFQuestionMapper> delete = builder.createCriteriaDelete(QSFQuestionMapper.class);
        Root<QSFQuestionMapper> root = delete.from(QSFQuestionMapper.class);
        delete.where(builder.equal(root.get(QSFQuestionMapper_.ID).get(QSFQuestionMapperId_.SURVEY_ID), surveyId));
        sessionFactory.getCurrentSession().createQuery(delete).executeUpdate();
    }
}
