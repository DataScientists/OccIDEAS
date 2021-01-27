package org.occideas.voxco.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.occideas.entity.NodeVoxco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.Date;
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
    public void updateAll(List<NodeVoxco> surveys) {
        if (CollectionUtils.isEmpty(surveys)) return;

        surveys.forEach(survey -> {
            update(survey.getSurveyId(), survey.getIdNode(), survey.getExtractionId(), survey.getExtractionStatus(),
                    survey.getFileId(), survey.getExtractionStart(), survey.getExtractionEnd(), survey.getResultPath());
        });
        sessionFactory.getCurrentSession().flush();
    }

    @Override
    public void update(long surveyId, long idNode, Long extractionId, String extractionStatus,
                       Long fileId, Date extractionStart, Date extractionEnd, String resultPath) {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaUpdate<NodeVoxco> criteriaUpdate = builder.createCriteriaUpdate(NodeVoxco.class);
        Root<NodeVoxco> root = criteriaUpdate.from(NodeVoxco.class);

        if (extractionId != null && extractionId > 0L) {
            criteriaUpdate.set(root.get("extractionId"), extractionId);
        }

        if (fileId != null && fileId > 0L) {
            criteriaUpdate.set(root.get("fileId"), fileId);
        }

        if (extractionStatus != null && !StringUtils.EMPTY.equals(extractionStatus)) {
            criteriaUpdate.set(root.get("extractionStatus"), extractionStatus);
        }

        if (extractionStart != null) {
            criteriaUpdate.set(root.get("extractionStart"), extractionStart);
        }

        if (extractionEnd != null) {
            criteriaUpdate.set(root.get("extractionEnd"), extractionEnd);
        }

        if (resultPath != null && !StringUtils.EMPTY.equals(resultPath)) {
            criteriaUpdate.set(root.get("resultPath"), resultPath);
        }

        criteriaUpdate.where(builder.and(
                builder.equal(root.get("deleted"), 0),
                builder.equal(root.get("surveyId"), surveyId),
                builder.equal(root.get("idNode"), idNode)
        ));
        sessionFactory.getCurrentSession().createQuery(criteriaUpdate).executeUpdate();
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

    @Override
    public List<NodeVoxco> getAllActive() {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<NodeVoxco> criteria = builder.createQuery(NodeVoxco.class);
        Root<NodeVoxco> root = criteria.from(NodeVoxco.class);
        criteria.where(builder.equal(root.get("deleted"), 0));
        Query<NodeVoxco> query = sessionFactory.getCurrentSession().createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public List<NodeVoxco> getAllActiveWithPendingExtraction() {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<NodeVoxco> criteria = builder.createQuery(NodeVoxco.class);
        Root<NodeVoxco> root = criteria.from(NodeVoxco.class);
        criteria.where(
                builder.and(
                    builder.equal(root.get("deleted"), 0),
                    builder.or(
                            builder.isNull(root.get("fileId")), builder.equal(root.get("fileId"), 0),
                            builder.isNull(root.get("extractionStatus")), builder.notEqual(root.get("extractionStatus"), "Completed")
                    )
                ));
        Query<NodeVoxco> query = sessionFactory.getCurrentSession().createQuery(criteria);
        return query.getResultList();
    }

    @Override
    public void clearResultExtractionData() {
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaUpdate<NodeVoxco> criteriaUpdate = builder.createCriteriaUpdate(NodeVoxco.class);
        Root<NodeVoxco> root = criteriaUpdate.from(NodeVoxco.class);
        criteriaUpdate.set(root.get("fileId"), 0);
        criteriaUpdate.set(root.get("extractionStatus"), StringUtils.EMPTY);
        criteriaUpdate.set(root.get("resultPath"), StringUtils.EMPTY);
        criteriaUpdate.set("extractionStart", null);
        criteriaUpdate.set("extractionEnd", null);
        sessionFactory.getCurrentSession().createQuery(criteriaUpdate).executeUpdate();
        sessionFactory.getCurrentSession().flush();
    }
}
