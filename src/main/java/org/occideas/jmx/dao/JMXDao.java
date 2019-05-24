package org.occideas.jmx.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.JMXLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("JMXDao")
public class JMXDao implements IJMXDao
{
    
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public JMXLog save(JMXLog entity)
    {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void deleteSoft(JMXLog entity)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteHard(JMXLog entity)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<JMXLog> find(String searchName, Object searchVal)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<JMXLog> list()
    {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(JMXLog.class);
        crit.add(Restrictions.eq("deleted", 0));
        return crit.list();
    }

    @Override
    public List<JMXLog> listDeleted()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
