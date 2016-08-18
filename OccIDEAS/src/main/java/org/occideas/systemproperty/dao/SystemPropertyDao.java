package org.occideas.systemproperty.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.SystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SystemPropertyDao {

	@Autowired
	private SessionFactory sessionFactory;

	public SystemProperty save(SystemProperty sysProp) {
		final Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(sysProp);
		return sysProp;
	}
	
	public SystemProperty getById(String variable){
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SystemProperty.class);
		criteria.add(Restrictions.eq("variable", variable));
		
		SystemProperty sysProp = (SystemProperty)criteria.uniqueResult();
		return sysProp;
	}
	
	
}
