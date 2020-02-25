package org.occideas.qsf.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.occideas.entity.NodeQSF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class NodeQSFDao implements INodeQSFDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public String save(String surveyId, long idNode, String results) {
        sessionFactory.getCurrentSession().saveOrUpdate(new NodeQSF(surveyId,idNode,results));
        return surveyId;
    }

    @Override
    public String getByIdNode(long idNode) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<NodeQSF> criteria = builder.createQuery(NodeQSF.class);
        Root<NodeQSF> root = criteria.from(NodeQSF.class);
        criteria.select(root).where(builder.equal(root.get("idNode"),idNode));

        Query<NodeQSF> query = sessionFactory.getCurrentSession().createQuery(criteria);
        List<NodeQSF> results = query.getResultList();
        if(results.isEmpty()){
           return null;
        }
        return results.get(0).getSurveyId();
    }

}
