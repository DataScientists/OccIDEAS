package org.occideas.interviewquestion.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Constant;
import org.occideas.entity.InterviewQuestion;
import org.occideas.module.service.ModuleService;
import org.occideas.question.service.QuestionService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.CommonUtil;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InterviewQuestionDao {
	
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
    private QuestionService questionService;
	
	@Autowired
	private SystemPropertyService systemPropertyService;
	
    @Autowired
	private ModuleService moduleService;
	
	private final String UNIQUE_INT_QUESTION_SQL = 	
			"select distinct(a.question_id) as question_id,a.id,a.idinterview,"
			+ "a.type,a.name,a.topNodeId, a.nodeClass,a.parentModuleId,"
			+ "a.modCount,a.parentAnswerId,a.link, a.deleted,a.isProcessed,"
			+ "a.description,a.number,a.intQuestionSequence,a.lastUpdated "
			+ "from Interview_Question a, Interview_Question b "
			+ "where a.question_id>0 and a.deleted = 0 and b.deleted = 0 and a.idinterview =b.idinterview and b.topNodeId in (:param) ";
		
	@SuppressWarnings("unchecked")
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
        List<QuestionVO> queueQuestions = new ArrayList<>();
        SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
        if(filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim())){
        	// get intro id
        	SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
        	if(introModule == null){
        		log.error(Constant.STUDY_INTRO+" is not set in config , report to admin.");
        	}else{
        		if(introModule.getValue().equalsIgnoreCase(String.valueOf(parentModuleId))){
        			queueQuestions = questionService.getQuestionsWithParentId(String.valueOf(parentModuleId));
        		}else{
        			if(iq.getType().equalsIgnoreCase("Q_linkedajsm")){
        				FragmentVO moduleFilterStudyAgent = (FragmentVO)moduleService.getModuleFilterStudyAgent(parentModuleId);
                		queueQuestions = moduleFilterStudyAgent.getChildNodes();
        			}else{
        				ModuleVO moduleFilterStudyAgent = (ModuleVO)moduleService.getModuleFilterStudyAgent(parentModuleId);
                		queueQuestions = moduleFilterStudyAgent.getChildNodes();
        			}
        			
        		}       		
        	}
        }else{
        	queueQuestions = questionService.getQuestionsWithParentId(String.valueOf(parentModuleId));
        }
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

	@SuppressWarnings("unchecked")
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

	public Long getUniqueInterviewQuestionCount(String[] filterModule) {
		
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("deleted", 0))
				.add(Restrictions.in("topNodeId", CommonUtil.convertToLongList(filterModule)))
				.add(Restrictions.gt("questionId", 0L))
				.setProjection(Projections.countDistinct("idInterview"));
				
		return (Long) crit.uniqueResult();
	}

	public InterviewQuestion getByQuestionId(Long questionId, 
			Long interviewId) {
		
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("questionId", questionId))
				.add(Restrictions.eq("idInterview", interviewId));
		
		return (InterviewQuestion) crit.uniqueResult();
	}

	public List<InterviewQuestion> getQuestionsByNodeId(Long questionId) {
		
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		crit.add(Restrictions.eq("questionId",questionId));
		List<InterviewQuestion> list = crit.list();
		if(list.isEmpty()){
			return null;
		}
		return list;
	}
	
	public Long getIntroModuleId(Long interviewId) {
		
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("idInterview", interviewId))
				.add(Restrictions.eq("nodeClass", "M"))
				.setMaxResults(1)
				.setProjection(Projections.projectionList()
						.add(Projections.property("link"),"link"));
		
		return (Long)crit.uniqueResult();
	}

}
