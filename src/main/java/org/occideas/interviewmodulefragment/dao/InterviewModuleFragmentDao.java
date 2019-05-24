package org.occideas.interviewmodulefragment.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.InterviewModuleFragment;
import org.occideas.entity.InterviewQuestion;
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
		 
		 DetachedCriteria subquery = DetachedCriteria.forClass(InterviewQuestion.class)
					.setProjection(Projections.property("id"))
					.add(Restrictions.eq("idInterview", Long.valueOf(id)))
					.add(Restrictions.eq("isProcessed", true))
					.add(Restrictions.eq("type", "Q_linkedajsm"));
			
        final Criteria crit = session.createCriteria(InterviewModuleFragment.class)
       		 					.add(Restrictions.eq("interviewId", String.valueOf(id)))
   		 						.add(Property.forName("interviewPrimaryKey").in(subquery));
        
        return crit.list();
	}
	
}
