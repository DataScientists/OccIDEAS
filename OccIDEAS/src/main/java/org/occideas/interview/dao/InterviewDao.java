package org.occideas.interview.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
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
import org.occideas.entity.Note;
import org.occideas.entity.SystemProperty;
import org.occideas.utilities.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class InterviewDao implements IInterviewDao{
	
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
			+ " from Interview_Answer a,"
			+ " Interview_Question q"
			+ " where a.answerId = :answerId and a.idinterview  = :idInterview"
			+ " and a.idinterview = q.idinterview"
			+ " and q.id = a.interviewQuestionId";
	
	private final String NOTES_QUERY_WITH_MODULE = 
			" SELECT a.interviewId, b.referenceNumber,"
			 + " GROUP_CONCAT(DISTINCT text SEPARATOR '++') as notes"
			 + " FROM note a, interview b, interviewintromodule_module c"
			 + " where a.interviewId = b.idinterview"
			 + " and a.interviewId = c.interviewId"
			 + " and (c.idModule in (:modules))"
			 +  " group by a.interviewId";
	
	private final String NOTES_QUERY = 
			" SELECT a.interviewId, b.referenceNumber,"
			 + " GROUP_CONCAT(DISTINCT text SEPARATOR '++') as notes"
			 + " FROM Note a, Interview b, InterviewIntroModule_Module c"
			 + " where a.interviewId = b.idinterview"
			 + " and a.interviewId = c.interviewId"
			 + " group by a.interviewId";

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Interview interview){
		sessionFactory.getCurrentSession().persist(interview);
    }

    @Override
    public void delete(Interview interview){
      sessionFactory.getCurrentSession().delete(interview);
    }

    @Override
	public Interview get(Long id){
      return (Interview) sessionFactory.getCurrentSession().get(Interview.class, id);
    }

    @Override
	public Interview merge(Interview interview)   {
      return (Interview) sessionFactory.getCurrentSession().merge(interview);
    }

    @Override
    public void saveOrUpdate(Interview interview){
      sessionFactory.getCurrentSession().saveOrUpdate(interview);
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void saveNewTransaction(Interview interview){
       sessionFactory.getCurrentSession().saveOrUpdate(interview);
    }
    
    @Override
    public List<Interview> getAll() {
    	//No filter
    	return getAllWithModules(null);
    }

    @Override
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
    
	@Override
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
	
	@Override
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
	
	@Override
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

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@SuppressWarnings("unchecked")
	public List<Interview> getInterview(Long interviewId) {
		final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Interview.class);
        if (interviewId != null) {
            crit.add(Restrictions.eq("idinterview", interviewId));
        }
        return crit.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Interview> getInterviews(Long[] interviewIds) {
		final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(Interview.class);
        if (interviewIds != null) {
            crit.add(Restrictions.in("idinterview", interviewIds));
        }
        return crit.list();
	}

	@Override
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

	@Override
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

	@Override
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
	
	@Override
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
	
	@Override
	public BigInteger getAnswerCount(Long interviewId, Long nodeId) {
		
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(ANSWER_COUNT_QUERY);
		sqlQuery.setMaxResults(1);
		sqlQuery.setParameter("idInterview", interviewId);
		sqlQuery.setParameter("answerId", nodeId);
		
		return (BigInteger) sqlQuery.uniqueResult();
	}

	@Override
	public List<Interview> getAssessmentsForNotes(String[] modules) {
		
		final Session session = sessionFactory.getCurrentSession();
		
		Query sqlQuery = session.createSQLQuery(modules != null ? NOTES_QUERY_WITH_MODULE : NOTES_QUERY);
		
		if(modules != null) {
			sqlQuery.setParameterList("modules", modules);
		} 		
		
		List<Object[]> rows = sqlQuery.list();
		
		List<Interview> result = new ArrayList();
		
		//Map manually
		for (Object[] row : rows) {
			Interview interview = new Interview();
		    interview.setIdinterview(((BigInteger)row[0]).longValue());
		    interview.setReferenceNumber(row[1].toString());
		    
		    if(row[2] != null){
		    	ArrayList<Note> notes = new ArrayList<>();
		    	for(String s : (row[2].toString().split("\\++"))){
		    		Note note = new Note();
				    note.setText(s);
				    notes.add(note);
		    	}
		    	interview.setNotes(notes);
		    }
		    
			result.add(interview);
		}		
		
		return result;
	}
}

