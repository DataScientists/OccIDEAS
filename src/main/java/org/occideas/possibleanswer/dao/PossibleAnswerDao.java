package org.occideas.possibleanswer.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.occideas.base.dao.BaseDao;
import org.occideas.entity.PossibleAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PossibleAnswerDao implements IPossibleAnswerDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private BaseDao baseDao;

    @Override
    public PossibleAnswer get(long id) {
        return baseDao.get(PossibleAnswer.class, id);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void saveOrUpdate(PossibleAnswer answer) {
        sessionFactory.getCurrentSession().saveOrUpdate(answer);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void saveOrUpdateIgnoreFK(PossibleAnswer answer) {
        sessionFactory.getCurrentSession().createSQLQuery("SET foreign_key_checks = 0")
                .executeUpdate();
        sessionFactory.getCurrentSession().saveOrUpdate(answer);
    }

    @Override
    public PossibleAnswer findByTopNodeIdAndNumber(long moduleId, String answerNumber) {
        final Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PossibleAnswer> criteria = builder.createQuery(PossibleAnswer.class);
        Root<PossibleAnswer> root = criteria.from(PossibleAnswer.class);

        Predicate whereClause = builder.and(
                builder.equal(root.get("topNodeId"), moduleId),
                builder.equal(root.get("number"), answerNumber),
                builder.equal(root.get("deleted"), 0));
        criteria.select(root).where(whereClause);

        TypedQuery<PossibleAnswer> query = sessionFactory.getCurrentSession().createQuery(criteria);
        final List<PossibleAnswer> list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

}
