package org.occideas.interviewquestion.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Interview;
import org.occideas.entity.InterviewQuestion;
import org.occideas.question.service.QuestionService;
import org.occideas.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewQuestionDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
    private QuestionService questionService;
	
	private final String UNIQUE_INT_QUESTION_SQL = 	
			"select distinct(a.question_id) as question_id,a.id,a.idinterview,"
			+ "a.type,a.name,a.topNodeId, a.nodeClass,a.parentModuleId,"
			+ "a.modCount,a.parentAnswerId,a.link, a.deleted,a.isProcessed,"
			+ "a.description,a.number,a.intQuestionSequence,a.lastUpdated "
			+ "from Interview_Question a, Interview_Question b "
			+ "where a.question_id>0 and a.deleted = 0 and b.deleted = 0 and a.idinterview =b.idinterview and b.topNodeId in (:param) "
			+ "and b.topNodeId = a.topNodeId";
		
	public List<InterviewQuestion> getUniqueInterviewQuestions(String[] filterModule){
		System.out.println("Start getUniqueInterviewQuestions:"+new Date());
		final Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(UNIQUE_INT_QUESTION_SQL).
				addEntity(InterviewQuestion.class);
		sqlQuery.setParameterList("param", filterModule);
		List<InterviewQuestion> list = sqlQuery.list();
		System.out.println("Complete getUniqueInterviewQuestions:"+new Date());
		return list;
	}
	
	public void updateModuleNameForInterviewId(long id,String newName){
    	Session session = sessionFactory.getCurrentSession();
    	String hqlUpdate = "update InterviewQuestion iq set iq.name = :newName where iq.id = :id";
    	session.createQuery( hqlUpdate )
    	        .setString( "newName", newName )
    	        .setLong( "id", id )
    	        .executeUpdate();
    }
	
	public InterviewQuestion save(InterviewQuestion iq){
      return (InterviewQuestion) sessionFactory.getCurrentSession().save(iq);
    }

    public void delete(InterviewQuestion iq){
      sessionFactory.getCurrentSession().delete(iq);
    }

	public InterviewQuestion get(Long id){
		return (InterviewQuestion) sessionFactory.getCurrentSession().get(InterviewQuestion.class, id);
    }
	
	public InterviewQuestion merge(InterviewQuestion iq)   {
      return (InterviewQuestion) sessionFactory.getCurrentSession().merge(iq);
    }

    public InterviewQuestion saveOrUpdate(InterviewQuestion iq){
      sessionFactory.getCurrentSession().saveOrUpdate(iq);
      return iq;
    }
    
    public List<InterviewQuestion> saveOrUpdate(List<InterviewQuestion> iqs) {
		List<InterviewQuestion> list = new ArrayList<>();
		for(InterviewQuestion iq:iqs){
			sessionFactory.getCurrentSession().saveOrUpdate(iq);
			list.add(iq);
		}
		return list;
	}
    
    public InterviewQuestion saveInterviewLinkAndQueueQuestions(InterviewQuestion iq){
    	iq.setProcessed(true);
    	sessionFactory.getCurrentSession().saveOrUpdate(iq);
    	int intQuestionSequence = iq.getIntQuestionSequence();
    	long parentModuleId = iq.getLink();
        List<QuestionVO> queueQuestions = questionService.getQuestionsWithParentId(String.valueOf(parentModuleId));
        Collections.sort(queueQuestions); 
        for(QuestionVO question :queueQuestions){
        	InterviewQuestion iqQueue = new InterviewQuestion();
        	
        	iqQueue.setIdInterview(iq.getIdInterview());
        	iqQueue.setName(question.getName());
        	iqQueue.setDescription(question.getDescription());
			iqQueue.setNodeClass(question.getNodeclass());
			iqQueue.setNumber(question.getNumber());
			iqQueue.setModCount(iq.getModCount());
			iqQueue.setLink(question.getLink());
			iqQueue.setType(question.getType());
        	iqQueue.setParentModuleId(question.getTopNodeId());
        	iqQueue.setQuestionId(question.getIdNode());
        	iqQueue.setTopNodeId(question.getTopNodeId());
        	iqQueue.setIntQuestionSequence(++intQuestionSequence);
        	iqQueue.setDeleted(0);
//			intQuestionSequence++;
			sessionFactory.getCurrentSession().saveOrUpdate(iqQueue);
			
        }
        return iq;
      }

    @SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAll() {
      final Session session = sessionFactory.getCurrentSession();
      final Criteria crit = session.createCriteria(InterviewQuestion.class);
      return crit.list();
    }
    @SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAllActive() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("deleted", 0));
		return crit.list();
	}
    
    @SuppressWarnings("unchecked")
    public List<InterviewQuestion> findByInterviewId(Long interviewId) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(InterviewQuestion.class);
        if (interviewId != null) {
            crit.add(Restrictions.eq("idInterview", interviewId));
        }
        return crit.list();
    }

	public InterviewQuestion findIntQuestion(long idInterview, long questionId) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		crit.add(Restrictions.eq("idInterview", idInterview));
		crit.add(Restrictions.eq("questionId",questionId));
		List<InterviewQuestion> list = crit.list();
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
    
	public Long getMaxIntQuestionSequence(long idInterview) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("idInterview", idInterview))
				.addOrder(Order.desc("intQuestionSequence"))
				.setMaxResults(1)
				.setProjection(Projections.projectionList()
						.add(Projections.property("intQuestionSequence"),"intQuestionSequence"));
		return (Long)crit.uniqueResult();
	}

}
