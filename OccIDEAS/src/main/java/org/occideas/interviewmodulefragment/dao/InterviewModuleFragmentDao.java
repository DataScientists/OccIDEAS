package org.occideas.interviewmodulefragment.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewModuleFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewModuleFragmentDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public List<InterviewModuleFragment> getModFragmentById(long id){
		 final Session session = sessionFactory.getCurrentSession();
         final Criteria crit = session.createCriteria(InterviewModuleFragment.class)
        		 					.add(Restrictions.eq("idFragment", id));
         return crit.list();
	}
	public List<InterviewModuleFragment> getModFragmentByInterviewId(long id){
		 final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewModuleFragment.class)
       		 					.add(Restrictions.eq("interviewId", String.valueOf(id)));
        return crit.list();
	}
	
}
