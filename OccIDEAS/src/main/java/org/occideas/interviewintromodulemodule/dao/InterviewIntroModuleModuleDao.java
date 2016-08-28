package org.occideas.interviewintromodulemodule.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewIntroModuleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewIntroModuleModuleDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<InterviewIntroModuleModule> getInterviewIntroModByModId(long idModule){
		 final Session session = sessionFactory.getCurrentSession();
         final Criteria crit = session.createCriteria(InterviewIntroModuleModule.class)
        		 					.add(Restrictions.eq("idModule", idModule));
         return crit.list();
	}
	
}
