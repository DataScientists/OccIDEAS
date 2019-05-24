package org.occideas.moduleintromodule.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.ModuleIntroModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleIntroModuleDao implements IModuleIntroModuleDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<ModuleIntroModule> getModuleIntroModuleByModuleId(long moduleId) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(ModuleIntroModule.class)
				.add(Restrictions.eq("moduleId", moduleId));
		return crit.list();
	}

}
