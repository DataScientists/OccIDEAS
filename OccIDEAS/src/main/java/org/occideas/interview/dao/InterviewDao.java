package org.occideas.interview.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Interview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
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

    @SuppressWarnings("unchecked")
	public List<Interview> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Interview.class)		  						
    		  						.setProjection(Projections.projectionList()
    	       		  					.add(Projections.property("fragment"),"fragment")
    	       		  					.add(Projections.property("module"),"module")
    	       		  					.add(Projections.property("idinterview"),"idinterview")
    	       		  					.add(Projections.property("referenceNumber"),"referenceNumber"))
    		  						.addOrder(Order.asc("referenceNumber"))
    		  						.setResultTransformer(Transformers.aliasToBean(Interview.class));
      List<Interview> retValue = new ArrayList<Interview>();
      List<Interview> temp = crit.list();
      for(Interview interview: temp){
    	  interview = this.get(interview.getIdinterview()); //Todo fix this workaround and ask discuss with Jed about why hibernate is not populating firedRules
    	  retValue.add(interview);
      }
      return retValue;
    }
    @SuppressWarnings("unchecked")
	public List<Interview> getAssessments() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(Interview.class)
						    		.setProjection(Projections.projectionList()
								  		.add(Projections.property("fragment"),"fragment")
								  		.add(Projections.property("module"),"module")
								  		.add(Projections.property("idinterview"),"idinterview")
								  		.add(Projections.property("referenceNumber"),"referenceNumber")
								  		)
    		  						.createAlias("firedRules", "firedRules")
    		  						.addOrder(Order.asc("referenceNumber"))
    		  						.setResultTransformer(Transformers.aliasToBean(Interview.class));
      List<Interview> retValue = new ArrayList<Interview>();
      List<Interview> temp = crit.list();
      for(Interview interview: temp){
    	  interview = this.get(interview.getIdinterview()); //Todo fix this workaround and ask discuss with Jed about why hibernate is not populating firedRules
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
      for(Interview interview: temp){
    	  interview = this.get(interview.getIdinterview()); //Todo fix this workaround and ask discuss with Jed about why hibernate is not populating firedRules
    	  retValue.add(interview);
      }
      return retValue;
    }

	public List<Interview> getInterview(long interviewId) {
		final Session session = sessionFactory.getCurrentSession();
	    final Criteria crit = session.createCriteria(Interview.class)
							    		.add(Restrictions.eq("idinterview", interviewId))
	    		  						.setResultTransformer(Transformers.aliasToBean(Interview.class));
	    return crit.list();
	}
}
