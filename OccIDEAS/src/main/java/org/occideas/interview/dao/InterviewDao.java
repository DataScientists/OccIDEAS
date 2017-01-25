package org.occideas.interview.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.occideas.entity.Constant;
import org.occideas.entity.Interview;
import org.occideas.entity.InterviewIntroModuleModule;
import org.occideas.entity.SystemProperty;
import org.occideas.utilities.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewDao {
	
	private final String ASSESSMENT_BASE_COUNT = 
		" select count(*) from Participant p"  
	  	+ " join Interview i join InterviewIntroModule_Module im" 
	  	+ " where p.idParticipant = i.idParticipant"  
	  	+ " and i.idinterview = im.interviewId"
	  	+ " and im.idModule != (select value from SYS_CONFIG where name = 'activeintro' limit 1)"
	  	+ " and p.deleted = 0";
	
	private final String NOT_ASSESSED_COUNT = 
			ASSESSMENT_BASE_COUNT 
		  	+ " and (i.assessedStatus like '' or i.assessedStatus is null)";
	
	private final String ASSESSED_COUNT = 
			ASSESSMENT_BASE_COUNT  
		  	+ " and i.assessedStatus like '"+ Constant.AUTO_ASSESSED +"'";
	
	private final String ANSWER_COUNT_QUERY = "select count(1)" 
			+ " from interview_answer a,"
			+ " interview_question q"
			+ " where a.answerId = :answerId and a.idinterview  = :idInterview"
			+ " and a.idinterview = q.idinterview"
			+ " and q.id = a.interviewQuestionId";

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
    	return getAllWithModules(null);
    }

    @SuppressWarnings("unchecked")
	public List<Interview> getAll(String assessmentStatus) {
    	
		final Session session = sessionFactory.getCurrentSession();

		final Criteria crit = getInterviewCriteria(session);
		
		DetachedCriteria activeIntroQuery = DetachedCriteria.forClass(SystemProperty.class)
				.setProjection(Projections.property("value"))
				.add(Restrictions.eq("name", "activeIntro"));
		activeIntroQuery.getExecutableCriteria(session).setMaxResults(1);
		
		DetachedCriteria subquery = DetachedCriteria.forClass(InterviewIntroModuleModule.class, "iimm")
				.setProjection(Projections.property("interviewId"));		
		subquery.add(Property.forName("iimm.idModule").ne(activeIntroQuery));

		crit.add(Property.forName("interview.idinterview").in(subquery));		

		if (assessmentStatus != null) {
			if (Constant.NOT_ASSESSED.equals(assessmentStatus)) {
				crit.add(Restrictions.or(Restrictions.isNull("assessedStatus"), 
						Restrictions.eq("assessedStatus", "")));
			} else if (Constant.AUTO_ASSESSED.equals(assessmentStatus)){
				crit.add(Restrictions.eq("assessedStatus", assessmentStatus));
			}
		}

		List<Interview> retValue = new ArrayList<Interview>();
		setFiredRules(retValue, crit.list());
		return retValue;
    }

	private Criteria getInterviewCriteria(final Session session) {
		final Criteria crit = session.createCriteria(Interview.class, "interview")		  						
		  						.setProjection(Projections.projectionList()
		   		  					.add(Projections.property("fragment"),"fragment")
		   		  					.add(Projections.property("module"),"module")
		   		  					.add(Projections.property("moduleList"),"moduleList")
		   		  					.add(Projections.property("idinterview"),"idinterview")
		   		  					.add(Projections.property("referenceNumber"),"referenceNumber"))
		  						.addOrder(Order.asc("referenceNumber"))
		  						.setResultTransformer(Transformers.aliasToBean(Interview.class));
		return crit;
	}
    
    @SuppressWarnings("unchecked")
	public List<Interview> getAllWithModules(String[] modules) {
    	
      final Session session = sessionFactory.getCurrentSession();
      
      final Criteria crit = getInterviewCriteria(session);

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
	
	public BigInteger getAssessmentCount(String assessmentStatus) {
		
		final Session session = sessionFactory.getCurrentSession();
		
		String query = ASSESSMENT_BASE_COUNT;
		
		if(Constant.AUTO_ASSESSED.equals(assessmentStatus)){
			query = ASSESSED_COUNT;
		}
		else if(Constant.NOT_ASSESSED.equals(assessmentStatus)){
			query = NOT_ASSESSED_COUNT;
		}
		
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setMaxResults(1);		
		
		return (BigInteger) sqlQuery.uniqueResult();
	}
	
	public BigInteger getAnswerCount(Long interviewId, Long nodeId) {
		
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(ANSWER_COUNT_QUERY);
		sqlQuery.setMaxResults(1);
		sqlQuery.setParameter("idInterview", interviewId);
		sqlQuery.setParameter("answerId", nodeId);
		
		return (BigInteger) sqlQuery.uniqueResult();
	}
}

