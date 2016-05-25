package org.occideas.base.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao{

    @Autowired
    private SessionFactory sessionFactory;

    public long save(final Object o){
      return (long) sessionFactory.getCurrentSession().save(o);
    }


    public void delete(final Object object){
      sessionFactory.getCurrentSession().delete(object);
    }

    @SuppressWarnings("unchecked")
	public <T> T get(Class<T> type, final Long id){
      return (T) sessionFactory.getCurrentSession().get(type, id);
    }

    @SuppressWarnings("unchecked")
	public <T> T merge(final T o)   {
      return (T) sessionFactory.getCurrentSession().merge(o);
    }

    public <T> void saveOrUpdate(final T o){
      sessionFactory.getCurrentSession().saveOrUpdate(o);
    }
    
    public <T> void update(final T o){
        sessionFactory.getCurrentSession().update(o);
      }

    @SuppressWarnings("unchecked")
	public <T> List<T> getAll(final Class<T> type) {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(type);
      return crit.list();
    }
    
    @SuppressWarnings("unchecked")
   	public <T> List<T> getListbyId(final Class<T> type,final Long id) {
         final Session session = sessionFactory.getCurrentSession();
         final Criteria crit = session.createCriteria(type);
         crit.add(Restrictions.eq("idNode", id));
         return crit.list();
    }
}
