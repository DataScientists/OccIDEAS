package org.occideas.base.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Transactional
public class GenericBaseDao<T, I> {

    private final Class<T> clazz;
    private final String idFieldName;

    @Autowired
    protected SessionFactory sessionFactory;

    public GenericBaseDao(Class<T> clazz, String idFieldName) {
        this.clazz = clazz;
        this.idFieldName = idFieldName;
    }

    public Optional<T> findById(I id) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        Root<T> root = criteria.from(clazz);
        criteria.select(root);
        criteria.where(builder.equal(root.get(idFieldName), id));

        List<T> resultList = sessionFactory.getCurrentSession().createQuery(criteria).getResultList();
        if (resultList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultList.get(0));
    }

    public T save(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    public void saveAll(List<T> list) {
        list.forEach(this::save);
    }

    public void delete(T entity){
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void deleteAll(List<T> list){
        list.forEach(this::delete);
    }

    public void deleteAll() {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete = builder.createCriteriaDelete(clazz);
        criteriaDelete.from(clazz);
        session.createQuery(criteriaDelete).executeUpdate();
    }
}
