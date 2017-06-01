package org.occideas.interviewquestion.dao;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.occideas.entity.Constant;
import org.occideas.entity.Fragment;
import org.occideas.entity.InterviewQuestion;
import org.occideas.entity.Module;
import org.occideas.entity.Question;
import org.occideas.fragment.dao.IFragmentDao;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.module.service.ModuleService;
import org.occideas.question.service.QuestionService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.CommonUtil;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.FragmentVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.SystemPropertyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Repository
public class InterviewQuestionDao implements IInterviewQuestionDao {

	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private SystemPropertyService systemPropertyService;

	@Autowired
	private ModuleService moduleService;

	@Autowired
	private StudyAgentUtil studyAgentUtil;

	@Autowired
	private IModuleDao moduleDao;
	
	@Autowired
	private IFragmentDao fragmentDao;

	@Autowired
	private QuestionMapper qMapper;
	
	@Autowired
	private ModuleMapper moduleMapper;
	
	@Autowired
	private FragmentMapper fragmentMapper;

	private final String UNIQUE_INT_QUESTION_SQL = "select distinct(a.question_id) as question_id,a.id,a.idinterview,"
			+ "a.type,a.name,a.topNodeId, a.nodeClass,a.parentModuleId,"
			+ "a.modCount,a.parentAnswerId,a.link, a.deleted,a.isProcessed,"
			+ "a.description,a.number,a.intQuestionSequence,a.lastUpdated "
			+ "from Interview_Question a, Interview_Question b "
			+ "where a.question_id>0 and a.deleted = 0 and b.deleted = 0 and a.idinterview =b.idinterview and b.topNodeId in (:param) ";

	private final String PROCESSED_FRAGMENT = "select idNode from Node a, "
			+ " (select if(parentModuleId > 0, parentModuleId, parentAnswerId) as parentModuleId"
			+ " from Interview_Question where id = :id" + " and isProcessed = 1" + " ) b"
			+ " where (a.topNodeId = b.parentModuleId" + " or a.parent_idNode = b.parentModuleId)"
			+ " and a.link = :link";

	@Override
	@SuppressWarnings("unchecked")
	public List<InterviewQuestion> getUniqueInterviewQuestions(String[] filterModule) {
		System.out.println("Start getUniqueInterviewQuestions:" + new Date());
		final Session session = sessionFactory.getCurrentSession();
		SQLQuery sqlQuery = session.createSQLQuery(UNIQUE_INT_QUESTION_SQL).addEntity(InterviewQuestion.class);
		sqlQuery.setParameterList("param", filterModule);
		List<InterviewQuestion> list = sqlQuery.list();
		System.out.println("Complete getUniqueInterviewQuestions:" + new Date());
		return list;
	}

	@Override
	public void updateModuleNameForInterviewId(long id, String newName) {
		Session session = sessionFactory.getCurrentSession();
		String hqlUpdate = "update InterviewQuestion iq set iq.name = :newName where iq.id = :id";
		session.createQuery(hqlUpdate).setString("newName", newName).setLong("id", id).executeUpdate();
	}

	@Override
	public InterviewQuestion save(InterviewQuestion iq) {
		return (InterviewQuestion) sessionFactory.getCurrentSession().save(iq);
	}

	@Override
	public void delete(InterviewQuestion iq) {
		sessionFactory.getCurrentSession().delete(iq);
	}

	@Override
	public InterviewQuestion get(Long id) {
		return (InterviewQuestion) sessionFactory.getCurrentSession().get(InterviewQuestion.class, id);
	}

	@Override
	public InterviewQuestion merge(InterviewQuestion iq) {
		return (InterviewQuestion) sessionFactory.getCurrentSession().merge(iq);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public InterviewQuestion saveOrUpdate(InterviewQuestion iq) {
		sessionFactory.getCurrentSession().saveOrUpdate(iq);
		return iq;
	}
	
	@Override
	public InterviewQuestion saveOrUpdateSingleTransaction(InterviewQuestion iq) {
		sessionFactory.getCurrentSession().saveOrUpdate(iq);
		return iq;
	}

	@Override
	public List<InterviewQuestion> saveOrUpdate(List<InterviewQuestion> iqs) {
		List<InterviewQuestion> list = new ArrayList<>();
		for (InterviewQuestion iq : iqs) {
			sessionFactory.getCurrentSession().saveOrUpdate(iq);
			list.add(iq);
		}
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void preloadAllModules() {
		List<Module> modules = moduleDao.getAll(false);
		List<ModuleVO> voList =  moduleMapper.convertToModuleVOList(modules, false);
		for(ModuleVO moduleVO:voList){
			try {
				studyAgentUtil.createStudyAgentJson(String.valueOf(moduleVO.getIdNode()),moduleVO,true);
			} catch (Exception e) {
				log.error("Error creating study agent module json for "
						+moduleVO.getName()+"-"+moduleVO.getIdNode(),e);
			} 
		}
		List<Fragment> fragments = fragmentDao.getAll();
		List<FragmentVO> fragmentVOList = fragmentMapper.convertToFragmentVOList(fragments, false);
		for(FragmentVO fragmentVO:fragmentVOList){
			try {
				studyAgentUtil.createStudyAgentJson(String.valueOf(fragmentVO.getIdNode()),fragmentVO,true);
			} catch (Exception e) {
				log.error("Error creating study agent fragment json for "
						+fragmentVO.getName()+"-"+fragmentVO.getIdNode(),e);
			} 
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void preloadActiveIntro() {
		SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
		if (filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim())) {
			// get intro id
			SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
			if (introModule == null) {
				log.error("no intro module set");
			} else {
				String moduleId = introModule.getValue();
				List<Question> qList = moduleDao.getAllLinkingQuestionByModId(Long.valueOf(moduleId));
				List<QuestionVO> qListVO = qMapper.convertToQuestionVOList(qList);
				for (QuestionVO qVO : qListVO) {
					ModuleVO moduleFilterStudyAgent = (ModuleVO) moduleService
							.getModuleFilterStudyAgent(Long.valueOf(qVO.getLink()));
					try {
						studyAgentUtil.createStudyAgentJson(String.valueOf(qVO.getLink()), moduleFilterStudyAgent,true);
					} catch (Exception e) {
						log.error("Error creating study agent module json for "
									+qVO.getName()+"-"+qVO.getLink(),e);
					}
				}
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public InterviewQuestion saveInterviewLinkAndQueueQuestions(InterviewQuestion iq) {
		iq.setProcessed(true);
		sessionFactory.getCurrentSession().saveOrUpdate(iq);
		int intQuestionSequence = iq.getIntQuestionSequence();
		long parentModuleId = iq.getLink();
		SystemPropertyVO filterStudyAgentFlag = systemPropertyService.getByName(Constant.FILTER_STUDY_AGENTS);
		if (filterStudyAgentFlag != null && "true".equals(filterStudyAgentFlag.getValue().toLowerCase().trim())) {
			// get intro id
			SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
			if (introModule == null) {
				log.error("no intro module set");
				return null;
			} else {
				try {
					
					if (introModule.getValue().equals(String.valueOf(parentModuleId))) {
						loopChildQuestionsAndQueue(iq, intQuestionSequence, parentModuleId);
					} else {
						boolean filterAndCreateJson = false;
						
						
						try {
							if(!studyAgentUtil.doesStudyAgentJsonExist(String.valueOf(parentModuleId))){
								filterAndCreateJson = true;
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						NodeVO moduleFilterStudyAgent = null;
						NodeVO determineType = moduleService.getNodeNameById(parentModuleId);
						
						if(filterAndCreateJson){
							moduleFilterStudyAgent = (NodeVO) moduleService
									.getModuleFilterStudyAgent(Long.valueOf(parentModuleId));
							
							studyAgentUtil.createStudyAgentJson(String.valueOf(parentModuleId), moduleFilterStudyAgent,false);
						}else{
							if (determineType instanceof ModuleVO) {
								moduleFilterStudyAgent = studyAgentUtil.getStudyAgentJson(String.valueOf(parentModuleId));
							} else if (determineType instanceof FragmentVO) {
								moduleFilterStudyAgent = studyAgentUtil.getStudyAgentFragmentJson(String.valueOf(parentModuleId));
							}
							
						}
						
						if (moduleFilterStudyAgent == null) {
							// empty link remove it from queue
							iq.setDeleted(1);
							sessionFactory.getCurrentSession().saveOrUpdate(iq);

						} else if (moduleFilterStudyAgent instanceof ModuleVO) {
							ModuleVO modVO = (ModuleVO) moduleFilterStudyAgent;
							loopChildStudyAgentAndQueue(iq, intQuestionSequence, modVO.getChildNodes());
						} else if (moduleFilterStudyAgent instanceof FragmentVO) {
							FragmentVO fragVO = (FragmentVO) moduleFilterStudyAgent;
							loopChildStudyAgentAndQueue(iq, intQuestionSequence, fragVO.getChildNodes());
						}
					}
				} catch (Exception e) {
					log.error("Error on saveInterviewLinkAndQueueQuestions ",e);
				} 
			}
		} else {
			loopChildQuestionsAndQueue(iq, intQuestionSequence, parentModuleId);
		}
		return iq;
	}

	private void loopChildQuestionsAndQueue(InterviewQuestion iq, int intQuestionSequence, long parentModuleId) {
		List<QuestionVO> queueQuestions = new ArrayList<>();
		queueQuestions = questionService.getQuestionsWithParentId(String.valueOf(parentModuleId));
		Collections.sort(queueQuestions);
		for (QuestionVO question : queueQuestions) {
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
			sessionFactory.getCurrentSession().saveOrUpdate(iqQueue);
		}
	}

	private void loopChildStudyAgentAndQueue(InterviewQuestion iq, int intQuestionSequence,
			List<QuestionVO> queueQuestions) {
		Collections.sort(queueQuestions);
		for (QuestionVO question : queueQuestions) {
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
			sessionFactory.getCurrentSession().saveOrUpdate(iqQueue);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAll() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAllActive() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class).add(Restrictions.eq("deleted", 0));
		return crit.list();
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAllDeleted() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class).add(Restrictions.eq("deleted", 1));
		return crit.list();
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAllDeleted(Long idInterview) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		crit.add(Restrictions.eq("deleted", 1));
		crit.add(Restrictions.eq("idInterview",idInterview));
		return crit.list();
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<InterviewQuestion> getAllChildInterviewQuestions(Long idAnswer,Long idInterview) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		crit.add(Restrictions.eq("idInterview",idInterview));
		crit.add(Restrictions.eq("parentAnswerId",idAnswer));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<InterviewQuestion> findByInterviewId(Long interviewId) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		if (interviewId != null) {
			crit.add(Restrictions.eq("idInterview", interviewId));
		}
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public InterviewQuestion findIntQuestion(long idInterview, long questionId) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		crit.add(Restrictions.eq("idInterview", idInterview));
		crit.add(Restrictions.eq("questionId", questionId));
		List<InterviewQuestion> list = crit.list();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public Long getMaxIntQuestionSequence(long idInterview) {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("idInterview", idInterview)).addOrder(Order.desc("intQuestionSequence"))
				.setMaxResults(1).setProjection(Projections.projectionList()
						.add(Projections.property("intQuestionSequence"), "intQuestionSequence"));
		return (Long) crit.uniqueResult();
	}

	@Override
	public Long getUniqueInterviewQuestionCount(String[] filterModule) {

		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class).add(Restrictions.eq("deleted", 0))
				.add(Restrictions.in("topNodeId", CommonUtil.convertToLongList(filterModule)))
				.add(Restrictions.gt("questionId", 0L)).setProjection(Projections.countDistinct("idInterview"));

		return (Long) crit.uniqueResult();
	}

	@Override
	public InterviewQuestion getByQuestionId(Long questionId, Long interviewId) {

		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("questionId", questionId)).add(Restrictions.eq("idInterview", interviewId));

		return (InterviewQuestion) crit.uniqueResult();
	}

	@Override
	public List<InterviewQuestion> getQuestionsByNodeId(Long questionId) {

		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class);
		crit.add(Restrictions.eq("questionId", questionId));
		List<InterviewQuestion> list = crit.list();
		if (list.isEmpty()) {
			return null;
		}
		return list;
	}

	@Override
	public Long getIntroModuleId(Long interviewId) {

		final Session session = sessionFactory.getCurrentSession();
		final Criteria crit = session.createCriteria(InterviewQuestion.class)
				.add(Restrictions.eq("idInterview", interviewId)).add(Restrictions.eq("nodeClass", "M"))
				.setMaxResults(1).setProjection(Projections.projectionList().add(Projections.property("link"), "link"));

		return (Long) crit.uniqueResult();
	}

	@Override
	public Long checkFragmentProcessed(long link, long id) {

		final Session session = sessionFactory.getCurrentSession();
		Query sqlQuery = session.createSQLQuery(PROCESSED_FRAGMENT);
		sqlQuery.setParameter("id", id);
		sqlQuery.setParameter("link", link);

		Object result = sqlQuery.uniqueResult();

		return (result != null) ? ((BigInteger) result).longValue() : 0l;
	}

}
