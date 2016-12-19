package org.occideas.interview.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Interview;
import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.utilities.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void save(Interview interview){
		sessionFactory.getCurrentSession().persist(interview);
    }

    public void delete(Interview interview){
      sessionFactory.getCurrentSession().delete(interview);
    }

	public Interview get(Long id){
      return (Interview) sessionFactory.getCurrentSession().get(Interview.class, id);
    }

	public Interview merge(Interview interview)   {
      return (Interview) sessionFactory.getCurrentSession().merge(interview);
    }

    public void saveOrUpdate(Interview interview){
      sessionFactory.getCurrentSession().saveOrUpdate(interview);
    }
    
    public List<Interview> getAll() {
    	//No filter
    	return getAll(null);
    }

    @SuppressWarnings("unchecked")
	public List<Interview> getAll(String[] modules) {
    	
      final Session session = sessionFactory.getCurrentSession();
      
      final Criteria crit = session.createCriteria(Interview.class, "interview")		  						
	  						.setProjection(Projections.projectionList()
	   		  					.add(Projections.property("fragment"),"fragment")
	   		  					.add(Projections.property("module"),"module")
	   		  					.add(Projections.property("moduleList"),"moduleList")
	   		  					.add(Projections.property("idinterview"),"idinterview")
	   		  					.add(Projections.property("referenceNumber"),"referenceNumber"))
	  						.addOrder(Order.asc("referenceNumber"))
	  						.setResultTransformer(Transformers.aliasToBean(Interview.class));
      
      if(modules != null){
    	  getSubQuery(modules, crit);
      }
      
      List<Interview> retValue = new ArrayList<Interview>();      
      setFiredRules(retValue, crit.list());
      return retValue;
    }


	//TODO Fix this workaround and ask discuss with Jed about why hibernate is not populating firedRules
	private void setFiredRules(List<Interview> retValue, List<Interview> temp) {
		for(Interview interview: temp){
			  interview = this.get(interview.getIdinterview()); 
			  retValue.add(interview);
		  }
	}
    @SuppressWarnings("unchecked")
	public List<Interview> getAssessments() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Interview.class)
    		  						.addOrder(Order.asc("referenceNumber"));
      List<Interview> retValue = new ArrayList<Interview>();
      List<Interview> temp = crit.list();
      for(Interview interview: temp){
    	  interview = this.get(interview.getIdinterview()); //Todo fix this workaround and ask discuss with Jed about why hibernate is not populating firedRules
    	  interview.setFiredRules(interview.getFiredRules());
    	  retValue.add(interview);
      }
      return retValue;
    }
    @SuppressWarnings("unchecked")
	public List<Interview> findByReferenceNumber(String referenceNumber) {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Interview.class)
						    		.setProjection(Projections.projectionList()
								  		.add(Projections.property("fragment"),"fragment")
								  		.add(Projections.property("module"),"module")
								  		.add(Projections.property("idinterview"),"idinterview")
								  		.add(Projections.property("referenceNumber"),"referenceNumber")
								  		)
						    		.add(Restrictions.eq("referenceNumber", referenceNumber))
    		  						.setResultTransformer(Transformers.aliasToBean(Interview.class));
      List<Interview> retValue = new ArrayList<Interview>();
      List<Interview> temp = crit.list();
      setFiredRules(retValue, temp);
      return retValue;
    }

	@SuppressWarnings("unchecked")
	public List<Interview> getInterview(Long interviewId) {
		final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Interview.class);
        if (interviewId != null) {
            crit.add(Restrictions.eq("idinterview", interviewId));
        }
        return crit.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Interview> getInterviews(Long[] interviewIds) {
		final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Interview.class);
        if (interviewIds != null) {
            crit.add(Restrictions.in("idinterview", interviewIds));
        }
        return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<Interview> getInterviewIdList() {
		 final Session session = sessionFactory.getCurrentSession();
	     final Criteria crit = session.createCriteria(Interview.class)
	    		 .setProjection(Projections.projectionList()
	    			      .add(Projections.property("idinterview"), "idinterview"))
	    			    .setResultTransformer(Transformers.aliasToBean(Interview.class));
	    List<Interview> list = crit.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Interview> getAllInterviewsWithoutAnswers() {
		final Session session = sessionFactory.getCurrentSession();
	      final Criteria crit = session.createCriteria(Interview.class)		  						
	    		  						.setProjection(Projections.projectionList()
	    	       		  					.add(Projections.property("fragment"),"fragment")
	    	       		  					.add(Projections.property("module"),"module")
	    	       		  					.add(Projections.property("idinterview"),"idinterview")
	    	       		  					.add(Projections.property("referenceNumber"),"referenceNumber"))
	    		  						.addOrder(Order.asc("referenceNumber"))
	    		  						.setResultTransformer(Transformers.aliasToBean(Interview.class));
	      List<Interview> temp = crit.list();
	      return temp;
	}	

	public Long getCountForModules(String[] modules) {

		final Session session = sessionFactory.getCurrentSession();

		final Criteria crit = session.createCriteria(Interview.class, "interview")
				.setProjection((Projections.rowCount()));

		if (modules != null) {
			
			getSubQuery(modules, crit);
		}

		return (Long) crit.uniqueResult();
	}

	private void getSubQuery(String[] modules, final Criteria crit) {
		
		DetachedCriteria subquery = DetachedCriteria.forClass(InterviewIntroModuleModule.class, "iimm")
				.setProjection(Projections.property("interviewId"))
				.add(Restrictions.in("iimm.idModule", CommonUtil.convertToLongList(modules)))
				.add(Restrictions.eqProperty("iimm.interviewId", "interview.idinterview"));

		crit.add(Property.forName("interview.idinterview").in(subquery));
	}
}

