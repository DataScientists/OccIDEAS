package org.occideas.qsf.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.occideas.entity.NodeQSF;
import org.occideas.vo.NodeQSFFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class NodeQSFDao implements INodeQSFDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String save(String surveyId, long idNode, String results) {
        sessionFactory.getCurrentSession().saveOrUpdate(new NodeQSF(surveyId, idNode, results));
        return surveyId;
    }

    @Override
    public String getByIdNode(long idNode) {
        List<NodeQSF> results = list(new NodeQSFFilter<Long>("idNode", idNode));
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0).getSurveyId();
    }

    @Override
    public List<NodeQSF> list() {
        return list(null);
    }

    private List<NodeQSF> list(NodeQSFFilter... filters) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<NodeQSF> criteria = builder.createQuery(NodeQSF.class);
        Root<NodeQSF> root = criteria.from(NodeQSF.class);
        final CriteriaQuery<NodeQSF> select = criteria.select(root);
        if (filters != null && filters.length > 0) {
            Stream.of(filters).forEach(filter -> select.where(builder.equal(root.get(filter.getFilterId()), filter.getValue())));
        }
        criteria.distinct(true);
        criteria.orderBy(builder.desc(root.get("lastUpdated")));
        Query<NodeQSF> query = sessionFactory.getCurrentSession().createQuery(criteria);
        List<NodeQSF> results = query.getResultList();
        return results;
    }

}
