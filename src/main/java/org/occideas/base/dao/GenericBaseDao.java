package org.occideas.base.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import java.util.List;

@Transactional
public class GenericBaseDao<T, I> {

    private Class<T> clazz;

    @Autowired
    protected SessionFactory sessionFactory;

    public GenericBaseDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T save(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    public void saveAll(List<T> list) {
        list.stream().forEach(entity -> save(entity));
    }

    public void delete(T entity){
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void deleteAll(List<T> list){
        list.stream().forEach(entity -> delete(entity));
    }

    public void deleteAll() {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaDelete<T> criteriaDelete = builder.createCriteriaDelete(clazz);
        criteriaDelete.from(clazz);
        session.createQuery(criteriaDelete).executeUpdate();
    }
}
