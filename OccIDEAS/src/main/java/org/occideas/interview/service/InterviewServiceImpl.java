package org.occideas.interview.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Interview;
import org.occideas.entity.Module;
import org.occideas.entity.SystemProperty;
import org.occideas.interview.dao.IInterviewDao;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.mapper.InterviewMapper;
import org.occideas.mapper.InterviewQuestionMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.participant.service.ParticipantService;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.ParticipantVO;
import org.occideas.vo.RandomInterviewReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private BaseDao dao;

	@Autowired
	private IInterviewDao interviewDao;

	@Autowired
	private InterviewQuestionDao interviewQuestionDao;

	@Autowired
	private ParticipantService participantService;

	@Autowired
	private InterviewMapper mapper;

	@Autowired
	private InterviewQuestionMapper qsMapper;
	
	@Autowired
	private SystemPropertyDao systemPropertyDao;
	
	@Autowired
	private IModuleDao moduleDao;
	
	@Autowired
	private ModuleMapper moduleMapper;
	
	@Autowired
	private InterviewQuestionService interviewQuestionService;
	
	
	private final String PARTICIPANT_PREFIX = "auto";

	@Override
	public List<InterviewVO> listAll() {
		return mapper.convertToInterviewVOList(interviewDao.getAll());
	}

	@Override
	public List<InterviewVO> listAllWithRulesVO(String type) {
		System.out.println("2:listAllWithRules:" + new Date());
		List<InterviewVO> retValue = mapper.convertToInterviewWithRulesNoAnswersVOList(interviewDao.getAll(type));
		System.out.println("3:listAllWithRules:" + new Date());

		return retValue;
	}

	@Override
	public List<Interview> listAllWithRules(String[] modules) {

		// Disable slow mapping
		// List<InterviewVO> retValue =
		// mapper.convertToInterviewWithRulesNoAnswersVOList(interviewDao.getAll(modules));
		return interviewDao.getAllWithModules(modules);
	}

	@Override
	public List<Interview> listAllWithAssessments(String[] modules) {

		// Disable slow mapping
		// List<InterviewVO> retValue =
		// mapper.convertToInterviewWithAssessmentsVOList(interviewDao.getAll(modules));

		return interviewDao.getAllWithModules(modules);
	}

	@Override
	public List<InterviewVO> listAllWithAnswers() {
		return mapper.convertToInterviewVOList(interviewDao.getAll());
	}

	@Override
	public List<InterviewVO> listAssessments() {
		return mapper.convertToInterviewVOList(interviewDao.getAssessments());
	}

	@Override
	public List<InterviewVO> findById(Long id) {
		Interview interview = interviewDao.get(id);
		InterviewVO InterviewVO = mapper.convertToInterviewVO(interview);
		List<InterviewVO> list = new ArrayList<InterviewVO>();
		list.add(InterviewVO);
		return list;
	}

	@Override
	public List<InterviewVO> findByReferenceNumber(String referenceNumber) {
		List<Interview> list = interviewDao.findByReferenceNumber(referenceNumber);
		List<InterviewVO> listVO = mapper.convertToInterviewVOList(list);
		return listVO;
	}

	@Override
	public List<InterviewVO> findByIdWithRules(Long id) {
		return findByIdWithRules(id, true);
	}

	@Override
	public List<InterviewVO> findByIdWithRules(Long id, boolean isIncludeAnswer) {
		System.out.println("1:findByIdWithRules:" + new Date());
		Interview interview = interviewDao.get(id);
		InterviewVO InterviewVO = mapper.convertToInterviewWithRulesVO(interview, isIncludeAnswer);
		List<InterviewVO> list = new ArrayList<InterviewVO>();
		list.add(InterviewVO);
		System.out.println("2:findByIdWithRules:" + new Date());
		return list;
	}

	@Auditable(actionType = AuditingActionType.CREATE_INTERVIEW)
	@Override
	public InterviewVO create(InterviewVO o) {
		// TODO: Hotfix - Just don't understand why it returns interviewId
		// instead of object
		Object obj = dao.save(mapper.convertToInterview(o));
		Interview intervew = null;
		if (obj instanceof Interview) {
			intervew = (Interview) obj;
		} else if (obj instanceof Long) {
			intervew = dao.get(Interview.class, (Long) obj);
		}

		return mapper.convertToInterviewVO(intervew);
	}

	@Override
	public void update(InterviewVO o) {
		dao.saveOrUpdate(mapper.convertToInterview(o));
	}

	@Override
	public void merge(InterviewVO o) {
		dao.merge(mapper.convertToInterview(o));
	}

	@Override
	public void delete(InterviewVO o) {
		dao.delete(mapper.convertToInterview(o));
	}

	@Override
	public List<InterviewVO> getInterview(long interviewId) {
		return mapper.convertToInterviewWithModulesVOList(interviewDao.getInterview(interviewId));
	}

	@Override
	public List<Long> getInterviewIdlist() {
		System.out.println("1:getInterviewIdlist:" + new Date());
		List<Interview> debug = interviewDao.getInterviewIdList();
		List<Interview> debug1 = new ArrayList<Interview>();
		for (int i = 0; i < debug.size(); i++) {
			if (i < 10) {
				debug1.add(debug.get(i));
			}
		}
		System.out.println("2:getInterviewIdlist:" + new Date());
		return mapper.convertToInterviewIdList(debug1);
	}

	@Override
	public List<InterviewVO> listAllInterviewsWithoutAnswers() {
		System.out.println("1:listAllInterviewsWithoutAnswers:" + new Date());
		// List<Interview> debug = interviewDao.getAll();
		List<Interview> debug = interviewDao.getAllInterviewsWithoutAnswers();
		List<Interview> debug1 = new ArrayList<Interview>();
		for (int i = 0; i < debug.size(); i++) {
			if (i < 10) {
				debug1.add(debug.get(i));
			}
		}
		System.out.println("2:listAllInterviewsWithoutAnswers:" + new Date());
		return mapper.convertToInterviewWithoutAnswersList(debug1);
	}

	@Override
	public List<InterviewVO> getInterviewQuestionAnswerVO(long idinterview) {
		return mapper.convertToInterviewWithQuestionAnswerList(interviewDao.getInterview(idinterview));
	}

	@Override
	public List<Interview> getInterviewQuestionAnswer(long idinterview) {
		return interviewDao.getInterview(idinterview);
	}

	@Override
	public List<Interview> getInterviewsQuestionAnswer(Long[] ids) {
		return interviewDao.getInterviews(ids);
	}

	@Override
	public List<InterviewVO> getUnprocessedQuestions(Long id) {
		Interview interview = interviewDao.get(id);
		InterviewVO InterviewVO = mapper.convertToInterviewUnprocessQuestion(interview);
		List<InterviewVO> list = new ArrayList<InterviewVO>();
		list.add(InterviewVO);
		return list;
	}

	@Override
	public InterviewVO findInterviewWithFiredRulesById(Long id) {
		Interview interview = interviewDao.get(id);
		return mapper.convertToInterviewVOWithFiredRules(interview);
	}

	@Override
	public Long getAllWithRulesCount(String[] modules) {

		return interviewDao.getCountForModules(modules);
	}

	@Override
	public InterviewVO getQuestionHistory(Long id) {
		InterviewVO interview = new InterviewVO();
		interview.setQuestionHistory(
				qsMapper.convertToInterviewQuestionVOList(interviewDao.get(id).getQuestionHistory(), true));
		return interview;
	}

	@Override
	public BigInteger listAllWithRuleCount(String assessmentStatus) {

		return interviewDao.getAssessmentCount(assessmentStatus);
	}

	@Override
	public boolean isQuestionAnswered(Long interviewId, Long nodeId) {
		BigInteger count = interviewDao.getAnswerCount(interviewId, nodeId);
		return count.intValue() == 0 ? false : true;
	}

	@Override
	public Long getIntroModuleId(Long interviewId) {
		return interviewQuestionDao.getIntroModuleId(interviewId);
	}

	@Override
	public Long checkFragmentProcessed(long idFragment, long primaryKey) {
		return interviewQuestionDao.checkFragmentProcessed(idFragment, primaryKey);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public List<RandomInterviewReport> createRandomInterviews(int count) {
		//get active intro
		SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
		if(activeIntro == null || !StringUtils.isNumeric(activeIntro.getValue())){
			log.error("Active intro is either null or is not numeric");
			return null;
		}
		//get the module
		Module mod = moduleDao.get(Long.valueOf(activeIntro.getValue()));
		ModuleVO modVO = moduleMapper.convertToModuleVO(mod, false);
		//get latest participant count
		String maxReferenceNumber = participantService.getMaxReferenceNumber();
		if(maxReferenceNumber == null){
			maxReferenceNumber = PARTICIPANT_PREFIX+"000";
		}
		String referenceNumber = generateReferenceAuto(maxReferenceNumber);
		
		if (count != 0) {
			// get the last participantId and later increment by 1
			Long maxParticipantId = participantService.getMaxParticipantId();
			
			// loop the count and generate random interviews based on the count
			for (int i = 0; i < count; i++) {
				// create participant
				Long idParticipant = maxParticipantId + 1;
				maxParticipantId = idParticipant;
				ParticipantVO partVO = new ParticipantVO();
				partVO.setIdParticipant(idParticipant);
				partVO.setReference(referenceNumber);
				ParticipantVO participantVO = participantService.create(partVO);
				//create interview
				InterviewVO interviewVO = new InterviewVO();
				interviewVO.setParticipant(participantVO);
				interviewVO.setModule(modVO);
				interviewVO.setReferenceNumber(referenceNumber);
				Interview interviewEntity = mapper.convertToInterview(interviewVO);
				interviewDao.saveNewTransaction(interviewEntity);
				InterviewVO newInterviewVO = mapper.convertToInterviewVO(interviewEntity);
				referenceNumber = generateReferenceAuto(referenceNumber);
				// populate  interview question by module
				InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
				interviewQuestionVO.setIdInterview(newInterviewVO.getInterviewId());
				interviewQuestionVO.setTopNodeId(modVO.getIdNode());
				interviewQuestionVO.setParentModuleId(modVO.getIdNode());
				interviewQuestionVO.setName(modVO.getName());
				interviewQuestionVO.setDescription(modVO.getDescription());
				interviewQuestionVO.setNodeClass(modVO.getNodeclass());
				interviewQuestionVO.setNumber(modVO.getNumber());
				interviewQuestionVO.setType(modVO.getType());
				interviewQuestionVO.setLink(modVO.getIdNode());
				interviewQuestionVO.setModCount(1);
				interviewQuestionVO.setDeleted(0);
				interviewQuestionVO.setIntQuestionSequence(0);
				// save link question and queue
				InterviewQuestionVO linkAndQueueQuestions = interviewQuestionService.updateInterviewLinkAndQueueQuestions(interviewQuestionVO);
				
				// get interview
				List<InterviewVO> interviewList = 
				mapper.convertToInterviewWithQuestionAnswerList(interviewDao.getInterview(newInterviewVO.getInterviewId()));
				
				InterviewVO randomInterview = interviewList.get(0);
				// find next question queued
				List<InterviewQuestionVO> questionList = randomInterview.getQuestionHistory();
				BeanComparator bc = new BeanComparator("getNumber");
				Collections.sort(questionList, bc);
				for(InterviewQuestionVO vo:questionList){
					if(!vo.isProcessed() && vo.getDeleted() != 1){
						System.out.println(vo);
					}	
				}	
			}
		}

		return null;
	}

	private String generateReferenceAuto(String maxReference){
		String replaceAll = maxReference.replaceAll("\\D+","").replaceFirst("^0+(?!$)", "");
		Integer autoInt = Integer.valueOf(replaceAll);
		Integer finalInt = autoInt+1;
		String finalIntStr = String.valueOf(finalInt);
		Integer length = finalIntStr.length();
		StringBuilder sb = new StringBuilder(finalIntStr);
		if(length < 3){
			for(int i=length;i < 3;i++){
				 sb.insert(0, "0");
			}
		}
		System.out.println(sb.toString());
		return PARTICIPANT_PREFIX+sb.toString();
	}
}
