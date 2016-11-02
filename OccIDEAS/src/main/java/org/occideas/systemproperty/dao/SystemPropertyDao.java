package org.occideas.systemproperty.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Constant;
import org.occideas.entity.SystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SystemPropertyDao {

	@Autowired
	private SessionFactory sessionFactory;

	public SystemProperty save(SystemProperty sysProp) {
		final Session session = sessionFactory.getCurrentSession();
		session.clear();
		session.saveOrUpdate(sysProp);
		return sysProp;
	}

	public SystemProperty getById(long id) {
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SystemProperty.class);
		criteria.add(Restrictions.eq("id", id));

		SystemProperty sysProp = (SystemProperty) criteria.uniqueResult();
		return sysProp;
	}

	public List<SystemProperty> getAll() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(SystemProperty.class);
		return crit.list();
	}

	public void delete(SystemProperty entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}

	public SystemProperty getByName(String name) {
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SystemProperty.class);
		criteria.add(Restrictions.eq("name", name));

		SystemProperty sysProp = (SystemProperty) criteria.uniqueResult();
		return sysProp;
	}

	public List<SystemProperty> getByType(String type) {
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SystemProperty.class);
		criteria.add(Restrictions.eq("type", type));

		List<SystemProperty> list = criteria.list();
		if(list.isEmpty()){
			return null;
		}
		return list;
	}

	public boolean isStudyAgent(long agentId) {
		final Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(SystemProperty.class);
		criteria.add(Restrictions.eq("type", Constant.STUDY_AGENT_SYS_PROP));
		criteria.add(Restrictions.eq("value", String.valueOf(agentId)));
		SystemProperty sysProp = (SystemProperty) criteria.uniqueResult();
		if(sysProp != null){
			return true;
		}
		return false;
	}
}
