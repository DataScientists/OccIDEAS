package org.occideas.systemproperty.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Constant;
import org.occideas.entity.PossibleAnswer;
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
	
	/**
	 * Get all that matches the key
	 * @param key
	 * @return
	 */
	public List<SystemProperty> getAll(String key) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(SystemProperty.class);
		crit.add(Restrictions.eq("id", key));
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
	
	private final String POS_ANS_WITH_STUDY_AGENTS_SQL = "SELECT * FROM Node where idNode in" 
			+" (SELECT idNode FROM ModuleRule where idModule=:param "
			+" and idAgent in (select value from SYS_CONFIG where type='studyagent'"
			+"))";
	
	public List<PossibleAnswer> getPosAnsWithStudyAgentsByIdMod(long idModule){
		final Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(POS_ANS_WITH_STUDY_AGENTS_SQL).
				addEntity(PossibleAnswer.class);
		sqlQuery.setParameter("param", String.valueOf(idModule));
		List<PossibleAnswer> list = sqlQuery.list();
		return list;
	}
}
