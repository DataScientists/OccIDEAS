package org.occideas.voxco.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.occideas.entity.NodeVoxco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class NodeVoxcoDao implements INodeVoxcoDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long save(Long surveyId, long idNode, String surveyName) {
        if (surveyId == null || surveyId.longValue() == 0) {
            surveyId = getMaxSurveyId() + 1;
        }
        sessionFactory.getCurrentSession().saveOrUpdate(new NodeVoxco(surveyId, idNode, surveyName));
        return surveyId;
    }

    @Override
    public Long getMaxSurveyId() {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<NodeVoxco> criteria = builder.createQuery(NodeVoxco.class);
        Root<NodeVoxco> root = criteria.from(NodeVoxco.class);
        criteria.orderBy(builder.desc(root.get("surveyId")));
        Query<NodeVoxco> query = sessionFactory.getCurrentSession().createQuery(criteria);
        query.setMaxResults(1);
        return query.getSingleResult().getSurveyId();
    }

    @Override
    public List<NodeVoxco> findByIdNodeAndDeleted(Long idNode, Boolean deleted) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<NodeVoxco> criteria = builder.createQuery(NodeVoxco.class);
        Root<NodeVoxco> root = criteria.from(NodeVoxco.class);
        if (deleted != null) {
            criteria.where(builder.and(builder.equal(root.get("idNode"), idNode),
                    builder.equal(root.get("deleted"), Boolean.TRUE.equals(deleted) ? 1 : 0)));
        } else {
            criteria.where(builder.equal(root.get("idNode"), idNode));
        }
        criteria.orderBy(builder.desc(root.get("lastUpdated")));
        Query<NodeVoxco> query = sessionFactory.getCurrentSession().createQuery(criteria);
        return query.getResultList();
    }
}
