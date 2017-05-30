package org.occideas.interview.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Interview;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.entity.Module;
import org.occideas.entity.Question;
import org.occideas.entity.SystemProperty;
import org.occideas.fragment.dao.IFragmentDao;
import org.occideas.interview.dao.IInterviewDao;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewquestion.dao.IInterviewQuestionDao;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.mapper.FragmentMapper;
import org.occideas.mapper.InterviewMapper;
import org.occideas.mapper.InterviewQuestionMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.participant.service.ParticipantService;
import org.occideas.question.service.QuestionService;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.utilities.AssessmentStatusEnum;
import org.occideas.vo.InterviewAnswerVO;
import org.occideas.vo.InterviewQuestionComparator;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.ModuleVO;
import org.occideas.vo.NodeVO;
import org.occideas.vo.NoteVO;
import org.occideas.vo.ParticipantVO;
import org.occideas.vo.PossibleAnswerVO;
import org.occideas.vo.QuestionVO;
import org.occideas.vo.RandomInterviewReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

	private Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	private BaseDao dao;

	@Autowired
	private IInterviewDao interviewDao;

	@Autowired
	private IInterviewQuestionDao interviewQuestionDao;

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

	@Autowired
	private InterviewAnswerService interviewAnswerService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private IFragmentDao fragmentDao;

	@Autowired
	private FragmentMapper fragmentMapper;

	@Autowired
	private QuestionMapper questionMapper;

	private final String PARTICIPANT_PREFIX = "auto";

	@Override
	public void preloadActiveIntro() {
		interviewQuestionDao.preloadActiveIntro();
	}

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
		// set default assessed status
		o.setAssessedStatus(AssessmentStatusEnum.NOTASSESSED.getDisplay());
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
		if (o.getInterviewId() == 0L) {
		}
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
		List<Interview> debug = interviewDao.getAllInterviewsWithoutAnswers();
		return mapper.convertToInterviewWithoutAnswersList(debug);
	}
	
	@Override
	public List<InterviewVO> listAllInterviewsNotAssessed() {
		List<Interview> list = interviewDao.getAllInterviewsNotAssessed();
		return mapper.convertToInterviewWithoutAnswersList(list);
	}
	
	@Override
	public List<InterviewVO> listAllInterviewsAssessed() {
		List<Interview> list = interviewDao.getAllInterviewsAssessed();
		return mapper.convertToInterviewWithoutAnswersList(list);
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
	public void cleanDeletedAnswers(Long id) {
		List<InterviewQuestion> deletedQs = interviewQuestionDao.getAllDeleted();
		for(InterviewQuestion iq: deletedQs){
			this.deleteChildAnswers(iq);
		}
	}
	private void deleteChildAnswers(InterviewQuestion iq){
		for(InterviewAnswer ia:iq.getAnswers()){		
			this.deleteChildQuestions(ia);
			ia.setDeleted(1);			
		}
		interviewQuestionDao.saveOrUpdate(iq);
	}
	private void deleteChildQuestions(InterviewAnswer ia){
		//for (InterviewQuestion iq : ia.getQuestions()) {

		//	this.deleteChildAnswers(iq);
		//	iq.setDeleted(1);
		//	interviewQuestionDao.saveOrUpdate(iq);
		//}
		
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
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<RandomInterviewReport> createRandomInterviews(int count, Boolean isRandomAnswers,
			String[] filterModuleVO) {
		// get active intro
		SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
		if (activeIntro == null || !StringUtils.isNumeric(activeIntro.getValue())) {
			log.error("Active intro is either null or is not numeric");
			return null;
		}
		// get the module
		Module mod = moduleDao.get(Long.valueOf(activeIntro.getValue()));
		ModuleVO modVO = moduleMapper.convertToModuleVO(mod, false);
		// get latest participant count
		String maxReferenceNumber = participantService.getMaxReferenceNumber();
		if (maxReferenceNumber == null) {
			maxReferenceNumber = PARTICIPANT_PREFIX + "000";
		}
		String referenceNumber = generateReferenceAuto(maxReferenceNumber);
		List<RandomInterviewReport> results = new ArrayList<>();
		if (count != 0) {
			// loop the count and generate random interviews based on the count
			for (int i = 0; i < count; i++) {
				RandomInterviewReport randomInterviewReport = new RandomInterviewReport();
				randomInterviewReport.setReferenceNumber(referenceNumber);
				// create participant
				// Long idParticipant = maxParticipantId + 1;
				// maxParticipantId = idParticipant;
				ParticipantVO partVO = new ParticipantVO();
				// partVO.setIdParticipant(idParticipant);
				partVO.setReference(referenceNumber);
				ParticipantVO participantVO = participantService.create(partVO);
				// create interview
				InterviewVO interviewVO = new InterviewVO();
				interviewVO.setParticipant(participantVO);
				interviewVO.setModule(modVO);
				interviewVO.setReferenceNumber(referenceNumber);
				// set default assessed status
				interviewVO.setAssessedStatus(AssessmentStatusEnum.NOTASSESSED.getDisplay());
				Interview interviewEntity = mapper.convertToInterview(interviewVO);
				interviewDao.saveNewTransaction(interviewEntity);
				InterviewVO newInterviewVO = mapper.convertToInterviewVO(interviewEntity);
				randomInterviewReport.setInterviewId(newInterviewVO.getInterviewId());
				referenceNumber = generateReferenceAuto(referenceNumber);
				// populate interview question by module
				populateInterviewWithQuestions(modVO, results, randomInterviewReport, newInterviewVO, participantVO,
						isRandomAnswers, filterModuleVO);
			}
		}

		return results;
	}

	private void populateInterviewWithQuestions(NodeVO nodeVO, List<RandomInterviewReport> results,
			RandomInterviewReport randomInterviewReport, InterviewVO newInterviewVO, ParticipantVO participantVO,
			Boolean isRandomAnswers, String[] filterModuleVO) {
		InterviewQuestionVO interviewQuestionVO = new InterviewQuestionVO();
		interviewQuestionVO.setIdInterview(newInterviewVO.getInterviewId());
		interviewQuestionVO.setTopNodeId(nodeVO.getIdNode());
		interviewQuestionVO.setParentModuleId(nodeVO.getIdNode());
		interviewQuestionVO.setName(nodeVO.getName());
		interviewQuestionVO.setDescription(nodeVO.getDescription());
		interviewQuestionVO.setNodeClass(nodeVO.getNodeclass());
		interviewQuestionVO.setNumber(nodeVO.getNumber());
		interviewQuestionVO.setType(nodeVO.getType());
		interviewQuestionVO.setLink(nodeVO.getIdNode());
		interviewQuestionVO.setModCount(1);
		interviewQuestionVO.setDeleted(0);
		interviewQuestionVO.setIntQuestionSequence(0);
		// save link question and queue
		InterviewQuestionVO linkAndQueueQuestions = interviewQuestionService
				.updateInterviewLinkAndQueueQuestions(interviewQuestionVO);
		// get interview
		List<InterviewVO> interviewList = mapper
				.convertToInterviewWithQuestionAnswerList(interviewDao.getInterview(newInterviewVO.getInterviewId()));

		InterviewVO randomInterview = interviewList.get(0);
		processRandomQuestionsAndAnswers(randomInterview, randomInterviewReport, results, isRandomAnswers,
				filterModuleVO);
		List<NoteVO> notes = new ArrayList<>();
		NoteVO noteVO = new NoteVO();
		noteVO.setDeleted(0);
		noteVO.setInterviewId(randomInterview.getInterviewId());
		noteVO.setText("AUTO GENERATED INTERVIEW");
		noteVO.setType("System");
		notes.add(noteVO);
		interviewDao.saveNewTransaction(mapper.convertToInterview(randomInterview));
		participantVO.setStatus(2);
		participantService.updateNewTransaction(participantVO);
		results.add(randomInterviewReport);
	}

	private void processRandomQuestionsAndAnswers(InterviewVO randomInterview,
			RandomInterviewReport randomInterviewReport, List<RandomInterviewReport> results, Boolean isRandomAnswers,
			String[] filterModuleVO) {
		List<InterviewQuestionVO> questionList = randomInterview.getQuestionHistory();
		InterviewQuestionVO questionAsked = findNextQuestionQueued(questionList);
		if (questionAsked == null) {
			log.info("[Randominterview]-No question to ask for interview id " + randomInterview.getInterviewId()
					+ " end interview as completed");
			return;
		} else {
			List<QuestionVO> questionsWithSingleChildLevel = questionService
					.getQuestionsWithSingleChildLevel(questionAsked.getQuestionId());
			QuestionVO actualQuestion = questionsWithSingleChildLevel.get(0);
			List<PossibleAnswerVO> answers = actualQuestion.getChildNodes();
			if (actualQuestion.getLink() != 0L) {
				// its a linking question
				System.out.println("This is a linking questions");
				InterviewQuestionVO qVO = populateInteviewQuestionJsonByLinkedQuestion(randomInterview, questionAsked);
				qVO.setProcessed(true);
				questionAsked.setProcessed(true);
				interviewQuestionService.updateInterviewLinkAndQueueQuestions(qVO);
				randomInterviewReport.getListQuestion().add(questionAsked);
				InterviewAnswerVO interviewAnswerVO = new InterviewAnswerVO();
				interviewAnswerVO.setName("This is a linking question");
				randomInterviewReport.getListAnswer().add(interviewAnswerVO);
				List<InterviewVO> unprocessedQuestions = getUnprocessedQuestions(randomInterview.getInterviewId());
				if (unprocessedQuestions != null) {
					unprocessedQuestions.removeAll(Collections.singleton(null));
					if (!unprocessedQuestions.isEmpty()) {
						for (InterviewQuestionVO iVO : unprocessedQuestions.get(0).getQuestionQueueUnprocessed()) {
							if (!randomInterview.getQuestionHistory().contains(iVO)) {
								randomInterview.getQuestionHistory().add(iVO);
							}
						}
						processRandomQuestionsAndAnswers(randomInterview, randomInterviewReport, results,
								isRandomAnswers, filterModuleVO);
					}
				}
			} else if (answers.isEmpty()) {
				saveQuestion(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
						filterModuleVO);
				InterviewAnswerVO interviewAnswerVO = new InterviewAnswerVO();
				interviewAnswerVO.setName("ERROR: No answer to select for this Question.");
				randomInterviewReport.getListAnswer().add(interviewAnswerVO);
			} else {
				saveAnswer(randomInterview, questionAsked, answers, randomInterviewReport, isRandomAnswers,
						filterModuleVO);
				saveQuestion(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
						filterModuleVO);
			}
		}
	}

	private InterviewQuestionVO populateInteviewQuestionJsonByLinkedQuestion(InterviewVO randomInterview,
			InterviewQuestionVO actualQuestion) {
		InterviewQuestionVO intQuestionVO = new InterviewQuestionVO();
		intQuestionVO.setId(actualQuestion.getId());
		intQuestionVO.setIdInterview(randomInterview.getInterviewId());
		intQuestionVO.setTopNodeId(actualQuestion.getLink());
		intQuestionVO.setParentModuleId(Long.valueOf(actualQuestion.getParentModuleId()));
		intQuestionVO.setParentAnswerId(Long.valueOf(actualQuestion.getParentAnswerId()));
		intQuestionVO.setName(actualQuestion.getName());
		intQuestionVO.setDescription(actualQuestion.getDescription());
		intQuestionVO.setNodeClass(actualQuestion.getNodeClass());
		intQuestionVO.setAnswers(new ArrayList<>());
		intQuestionVO.setNumber("1");
		intQuestionVO.setType(actualQuestion.getType());
		intQuestionVO.setLink(actualQuestion.getLink());
		intQuestionVO.setIntQuestionSequence(0);
		intQuestionVO.setModCount(0);
		intQuestionVO.setDeleted(0);
		return intQuestionVO;
	}

	private void saveQuestion(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
			RandomInterviewReport randomInterviewReport, List<RandomInterviewReport> results, Boolean isRandomAnswers,
			String[] filterModuleVO) {
		questionAsked.setProcessed(true);
		interviewQuestionService.updateIntQ(questionAsked);
		randomInterviewReport.getListQuestion().add(questionAsked);
		processRandomQuestionsAndAnswers(randomInterview, randomInterviewReport, results, isRandomAnswers,
				filterModuleVO);
	}

	private void saveAnswer(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
			List<PossibleAnswerVO> answers, RandomInterviewReport randomInterviewReport, Boolean isRandomAnswers,
			String[] filterModuleVO) {

		PossibleAnswerVO selectedAnswer = null;
		if (!isRandomAnswers) {
			selectedAnswer = answers.get(0);
		} else {
			selectedAnswer = chooseRandomAnswer(answers, filterModuleVO);
		}
		InterviewAnswerVO interviewAnswer = populateInterviewAnswer(randomInterview, selectedAnswer);
		interviewAnswer.setInterviewQuestionId(questionAsked.getId());
		interviewAnswer.setIsProcessed(true);
		List<InterviewAnswerVO> listOfAnswers = new ArrayList<>();
		listOfAnswers.add(interviewAnswer);
		List<InterviewQuestionVO> questions = interviewAnswerService
				.saveIntervewAnswersAndGetChildQuestion(listOfAnswers);
		randomInterviewReport.getListAnswer().add(interviewAnswer);
		refreshUnprocessedQuestions(questions, randomInterview);
	}

	private PossibleAnswerVO chooseRandomAnswer(List<PossibleAnswerVO> answers, String[] filterModuleVO) {
		if (filterModuleVO != null && filterModuleVO.length > 0) {
			String filterModuleId = filterModuleVO[0];
			// choose an answer that would have the link module
			for (PossibleAnswerVO ans : answers) {
				List<Long> linksByAnswerId = interviewDao.getLinksByAnswerId(ans.getIdNode());
				if (linksByAnswerId.contains(Long.valueOf(filterModuleId))) {
					return ans;
				}
			}
		}
		int rnd = new Random().nextInt(answers.size());
		if (rnd == answers.size()) {
			rnd = rnd - 1;
		}
		return answers.get(rnd);
	}

	private void refreshUnprocessedQuestions(List<InterviewQuestionVO> questions, InterviewVO randomInterview) {
		if (questions != null && !questions.isEmpty()) {
			for (InterviewQuestionVO iVO : questions) {
				if (!randomInterview.getQuestionHistory().contains(iVO)) {
					randomInterview.getQuestionHistory().add(iVO);
				}
			}
		}
	}

	private InterviewQuestionVO findNextQuestionQueued(List<InterviewQuestionVO> questionList) {
		InterviewQuestionVO intQuestionVO = null;
		questionList.sort(new InterviewQuestionComparator());
		for (InterviewQuestionVO vo : questionList) {
			if (!vo.isProcessed() && vo.getDeleted() != 1) {
				intQuestionVO = vo;
				break;
			}
		}
		return intQuestionVO;
	}

	private InterviewAnswerVO populateInterviewAnswer(InterviewVO newInterviewVO, PossibleAnswerVO selectedAnswer) {
		InterviewAnswerVO answerVO = new InterviewAnswerVO();
		answerVO.setIdInterview(newInterviewVO.getInterviewId());
		answerVO.setTopNodeId(selectedAnswer.getTopNodeId());
		answerVO.setParentQuestionId(Long.valueOf(selectedAnswer.getParentId()));
		answerVO.setAnswerId(selectedAnswer.getIdNode());
		answerVO.setName(selectedAnswer.getName());
		answerVO.setModCount(1);
		answerVO.setAnswerFreetext(selectedAnswer.getName());
		answerVO.setNodeClass(selectedAnswer.getNodeclass());
		answerVO.setNumber(selectedAnswer.getNumber());
		answerVO.setType(selectedAnswer.getType());
		answerVO.setDeleted(selectedAnswer.getDeleted());
		answerVO.setIsProcessed(false);
		return answerVO;
	}

	private String generateReferenceAuto(String maxReference) {
		String replaceAll = maxReference.replaceAll("\\D+", "").replaceFirst("^0+(?!$)", "");
		Integer autoInt = Integer.valueOf(replaceAll);
		Integer finalInt = autoInt + 1;
		String finalIntStr = String.valueOf(finalInt);
		Integer length = finalIntStr.length();
		StringBuilder sb = new StringBuilder(finalIntStr);
		if (length < 3) {
			for (int i = length; i < 3; i++) {
				sb.insert(0, "0");
			}
		}
		System.out.println(sb.toString());
		return PARTICIPANT_PREFIX + sb.toString();
	}

	public List<Interview> listAssessmentsForNotes(String[] modules) {
		return interviewDao.getAssessmentsForNotes(modules);
	}

	@Override
	public List<String> getNoteTypes() {

		return interviewDao.getNoteTypes();
	}

	@Override
	public List<QuestionVO> getLinksByModule(Long id) {
		List<Question> linksByModule = interviewDao.getLinksByModule(id);
		List<QuestionVO> questionVOList = questionMapper.convertToQuestionVOList(linksByModule);
		return questionVOList;
	}

	@Override
	public void preloadAllModules() {
		interviewQuestionDao.preloadAllModules();
	}

}
