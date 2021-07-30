package org.occideas.base.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
@Transactional
public class GenericBaseDao<T, I> {

    @Autowired
    protected SessionFactory sessionFactory;

    public T save(T entity){
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<?> criteriaQuery = criteriaBuilder.createQuery(entity.getClass());
        sessionFactory.getCurrentSession().save(entity);
        return entity;
    }

    public void saveAll(List<T> list){
        list.stream().forEach(entity -> save(entity));
    }

    public void delete(T entity){
        sessionFactory.getCurrentSession().delete(entity);
    }

    public void deleteAll(List<T> list){
        list.stream().forEach(entity -> delete(entity));
    }

    public void deleteAll(){
        sessionFactory.getCurrentSession()
                .createSQLQuery("truncate table T").executeUpdate();
    }

}
