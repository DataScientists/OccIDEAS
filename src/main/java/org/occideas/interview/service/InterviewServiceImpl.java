package org.occideas.interview.service;

import com.opencsv.CSVReaderHeaderAware;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.agent.dao.IAgentDao;
import org.occideas.base.dao.BaseDao;
import org.occideas.config.QualtricsConfig;
import org.occideas.entity.*;
import org.occideas.interview.dao.IInterviewDao;
import org.occideas.interview.service.result.assessor.NoiseAssessmentService;
import org.occideas.interviewanswer.dao.IInterviewAnswerDao;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewautoassessment.dao.InterviewAutoAssessmentDao;
import org.occideas.interviewfiredrules.dao.InterviewFiredRulesDao;
import org.occideas.interviewmanualassessment.dao.InterviewManualAssessmentDao;
import org.occideas.interviewquestion.dao.IInterviewQuestionDao;
import org.occideas.interviewquestion.service.InterviewQuestionService;
import org.occideas.mapper.InterviewMapper;
import org.occideas.mapper.InterviewQuestionMapper;
import org.occideas.mapper.ModuleMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.module.dao.IModuleDao;
import org.occideas.modulerule.dao.ModuleRuleDao;
import org.occideas.participant.service.ParticipantService;
import org.occideas.qsf.dao.QualtricsSurveyResponseDao;
import org.occideas.qsf.service.IQSFService;
import org.occideas.question.service.QuestionService;
import org.occideas.rule.dao.IRuleDao;
import org.occideas.systemproperty.dao.SystemPropertyDao;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.AssessmentStatusEnum;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

    private final static String PARTICIPANT_PREFIX = "auto";
    private final Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    private BaseDao dao;
    @Autowired
    private IInterviewDao interviewDao;
    @Autowired
    @Lazy
    private IInterviewQuestionDao interviewQuestionDao;
    @Autowired
    private IInterviewAnswerDao interviewAnswerDao;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private InterviewMapper mapper;
    @Autowired
    private SystemPropertyDao systemPropertyDao;
    @Autowired
    private SystemPropertyService systemPropertyService;
    @Autowired
    private IModuleDao moduleDao;
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private InterviewQuestionService interviewQuestionService;
    @Autowired
    private InterviewAnswerService interviewAnswerService;
    @Autowired
    @Lazy
    private QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private InterviewQuestionMapper intQuestionMapper;
    @Autowired
    private IRuleDao ruleDao;
    @Autowired
    private InterviewAutoAssessmentDao interviewAutoAssessmentDao;
    @Autowired
    private ModuleRuleDao moduleRuleDao;
    @Autowired
    private InterviewManualAssessmentDao interviewManualAssessmentDao;
    @Autowired
    private IAgentDao agentDao;
    @Autowired
    private StudyAgentUtil studyAgentUtil;
    @Autowired
    private InterviewFiredRulesDao interviewFiredRulesDao;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    @Lazy
    private AutoAssessmentService autoAssessmentService;
    @Autowired
    private NoiseAssessmentService noiseAssessmentService;
    @Autowired
	private QualtricsConfig qualtricsConfig;
    @Autowired
    private QualtricsSurveyResponseDao qualtricsSurveyResponseDao;
    @Autowired
    @Lazy
    private IQSFService iqsfService;

    @Override
    public SystemPropertyVO preloadActiveIntro() {
        return interviewQuestionDao.preloadActiveIntro();
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
        List<InterviewVO> list = new ArrayList<>();
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
        if (StringUtils.isEmpty(o.getAssessedStatus())) {
            o.setAssessedStatus(AssessmentStatusEnum.NOTASSESSED.getDisplay());
        }
        Interview interview = mapper.convertToInterview(o);
        dao.saveOrUpdate(interview);
        //System.out.println("Assessment Status:"+interview.getAssessedStatus());

    }

    @Override
    public void update(Interview interview) {
        if (StringUtils.isEmpty(interview.getAssessedStatus())) {
            interview.setAssessedStatus(AssessmentStatusEnum.NOTASSESSED.getDisplay());
        }
        dao.saveOrUpdate(interview);
    }

    private void bulkUpdate(List<Interview> interviews) {
        log.info("start saving interviews {}", interviews.size());
        interviews.stream().forEach(interview -> dao.saveOrUpdate(interview));
        log.info("completed saving interviews {}", interviews.size());
    }

    @Override
    public void merge(Interview interview) {
        if (StringUtils.isEmpty(interview.getAssessedStatus())) {
            interview.setAssessedStatus(AssessmentStatusEnum.NOTASSESSED.getDisplay());
        }
        dao.merge(interview);
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
                intQuestionMapper.convertToInterviewQuestionVOList(interviewDao.get(id).getQuestionHistory(), true));
        return interview;
    }

    @Override
    public void cleanDeletedAnswers(Long id) {
        List<InterviewQuestion> deletedQs = interviewQuestionDao.getAllDeleted(id);
        if (deletedQs != null && !deletedQs.isEmpty()) {
            log.info("cleanDeletedAnswers number of questions to cleanup:" + deletedQs.size());
        }
        //	List<InterviewQuestionVO> list = new ArrayList<>();
        for (InterviewQuestion q : deletedQs) {
            //InterviewQuestionVO iq = intQuestionMapper.convertToInterviewQuestionVO(q);
            if (q.getAnswers() == null || q.getAnswers().isEmpty()) {
                log.info("ignored no answers to delete for question:" + q.getId() + " for interview " + q.getIdInterview());
            } else {
                log.info("delete child answers for question:" + q.getId() + " for interview " + q.getIdInterview());
                this.checkAndDeleteAnswerIfRequired(q);
            }
        }
        //	this.deleteChildAnswers(list);
    }

    private void checkAndDeleteAnswerIfRequired(InterviewQuestion iq) {
        boolean needsUpdate = false;
        for (InterviewAnswer ia : iq.getAnswers()) {
            if (ia.getDeleted() == 1) {
                log.info("ignored child answer to be deleted:" + ia + " for questions " + iq.getId() + " for interview " + iq.getIdInterview());
            } else {
                log.info("adding child answer to be deleted:" + ia + " for questions " + iq.getId() + " for interview " + iq.getIdInterview());
                ia.setDeleted(1);
                needsUpdate = false;
            }
            List<InterviewQuestion> qstodelete = interviewQuestionDao.getAllChildInterviewQuestions(ia.getAnswerId(), iq.getIdInterview());
            this.deleteChildQuestions(qstodelete);
        }
        if (needsUpdate) {
            interviewAnswerDao.saveWithClearSession(iq.getAnswers());

        }
    }


    private void deleteChildQuestions(List<InterviewQuestion> list) {
        for (InterviewQuestion iq : list) {
            iq.setDeleted(1);
            log.info("saving question:" + iq.getId() + " for interview " + iq.getIdInterview());
            interviewQuestionDao.saveOrUpdateSingleTransaction(iq);
            this.checkAndDeleteAnswerIfRequired(iq);
        }
    }

    @Override
    public BigInteger listAllWithRuleCount(String assessmentStatus) {

        return interviewDao.getAssessmentCount(assessmentStatus);
    }

    @Override
    public boolean isQuestionAnswered(Long interviewId, Long nodeId) {
        BigInteger count = interviewDao.getAnswerCount(interviewId, nodeId);
        return count.intValue() != 0;
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
        JobModule mod = moduleDao.get(Long.valueOf(activeIntro.getValue()));
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
                ParticipantDetailsVO pd = new ParticipantDetailsVO();
                pd.setDetailName("Priority");
                pd.setDetailValue("0");
                pd.setParticipantId(participantVO.getIdParticipant());
                participantVO.getParticipantDetails().add(pd);
                //participantService.update(participantVO);
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
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<RandomInterviewReport> createRandomInterviews1(int count, Boolean isRandomAnswers,
                                                              String[] filterModuleVO) {
        // get active intro
        SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
        if (activeIntro == null || !StringUtils.isNumeric(activeIntro.getValue())) {
            log.error("Active intro is either null or is not numeric");
            return null;
        }
        // get the module
        JobModule mod = moduleDao.get(Long.valueOf(activeIntro.getValue()));
        ModuleVO modVO = moduleMapper.convertToModuleVO(mod, false);
        // get latest participant count
        String maxReferenceNumber = participantService.getMaxReferenceNumber();
        if (maxReferenceNumber == null) {
            maxReferenceNumber = PARTICIPANT_PREFIX + "000";
        }
        String referenceNumber = generateReferenceAuto(maxReferenceNumber);
        List<RandomInterviewReport> results = new ArrayList<>();

        String csvFilePath = "/opt/data/redcapResponses/2025-09-01_0621.csv";
        //String csvFilePath = "/opt/data/redcapResponses/test003.csv";

        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(csvFilePath))) {
            Map<String, String> row;

            int rowNum = 1;
            while ((row = reader.readMap()) != null) {

                List<Map<String,String>> jobInterviews =  splitRowV2(row);

                //Map<String,String> baseRow = jobInterviews.get(0);

                rowNum++;
                for (Map<String,String> jobInterview : jobInterviews) {
                    // process or write split…
                    System.out.println(jobInterview);
                  //  if(jobInterview.size() <= 86){
                  //      continue;
                  //  }
                    boolean hasIntrColumn = false;

                    // Iterate through the keys (column names) of the current row.
                    for (String col : jobInterview.keySet()) {
                        if (col.startsWith("intr")) {
                            //boolean requireJobModule = false;
                            String strRequireJobModule = jobInterview.get("intr_1");
                            if(strRequireJobModule.equalsIgnoreCase("intr_1a")){
                                hasIntrColumn = true;
                                break; // Exit the inner loop as soon as a match is found.
                            }

                        }
                    }

                    // If no column with the 'intr' prefix was found, skip the rest of the loop.
                    if (!hasIntrColumn) {
                        continue; // Skips to the next row in the outer loop.
                    }


                    referenceNumber = jobInterview.get("record_id");

                    //referenceNumber = padReferenceNumber(referenceNumber);
                    RandomInterviewReport randomInterviewReport = new RandomInterviewReport();

                    randomInterviewReport.setReferenceNumber(referenceNumber);
                    System.out.println(referenceNumber);

                    ParticipantVO partVO = new ParticipantVO();

                    partVO.setReference(referenceNumber);

                    ParticipantVO participantVO = participantService.create(partVO);
                    ParticipantDetailsVO pd = new ParticipantDetailsVO();
                    pd.setDetailName("Priority");
                    pd.setDetailValue("0");
                    pd.setParticipantId(participantVO.getIdParticipant());
                    participantVO.getParticipantDetails().add(pd);

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

                    populateInterviewWithQuestions1(modVO, results, randomInterviewReport, newInterviewVO, participantVO,
                            isRandomAnswers, filterModuleVO,jobInterview);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        if (count != 0) {
            // loop the count and generate random interviews based on the count

            for (int i = 0; i < count; i++) {
                RandomInterviewReport randomInterviewReport = new RandomInterviewReport();
                randomInterviewReport.setReferenceNumber(referenceNumber);

                ParticipantVO partVO = new ParticipantVO();
                partVO.setReference(referenceNumber);

                ParticipantVO participantVO = participantService.create(partVO);
                ParticipantDetailsVO pd = new ParticipantDetailsVO();
                pd.setDetailName("Priority");
                pd.setDetailValue("0");
                pd.setParticipantId(participantVO.getIdParticipant());
                participantVO.getParticipantDetails().add(pd);

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

                populateInterviewWithQuestions1(modVO, results, randomInterviewReport, newInterviewVO, participantVO,
                        isRandomAnswers, filterModuleVO);
            }

        }

         */
        return results;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<RandomInterviewReport> createRandomInterviews2(int count, Boolean isRandomAnswers,
                                                               String[] filterModuleVO) {
        // get active intro
        SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
        if (activeIntro == null || !StringUtils.isNumeric(activeIntro.getValue())) {
            log.error("Active intro is either null or is not numeric");
            return null;
        }
        // get the module
        JobModule mod = moduleDao.get(Long.valueOf(activeIntro.getValue()));
        ModuleVO modVO = moduleMapper.convertToModuleVO(mod, false);
        // get latest participant count
        String maxReferenceNumber = participantService.getMaxReferenceNumber();
        if (maxReferenceNumber == null) {
            maxReferenceNumber = PARTICIPANT_PREFIX + "000";
        }
        String referenceNumber = generateReferenceAuto(maxReferenceNumber);
        List<RandomInterviewReport> results = new ArrayList<>();

        //String csvFilePath = "/opt/data/awes25/import/CINT01.csv";
        //String csvFilePath = "/opt/data/awes25/import/CINT02.csv";
        //String csvFilePath = "/opt/data/awes25/import/LINA01.csv";
        //String csvFilePath = "/opt/data/awes25/import/OCTO01.csv";
        //String csvFilePath = "/opt/data/awes25/import/OPEN01.csv";
        //String csvFilePath = "/opt/data/awes25/import/AWESOVERALL.csv";
        String csvFilePath = "";


        //String csvFilePath = "/opt/data/awes25/import/3340AWESLinAV1.csv";
        //String csvFilePath = "/opt/data/awes25/import/3340AWESCINTV1.csv";
        //String csvFilePath = "/opt/data/awes25/import/3340AWESCATIV1.csv";
        try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(csvFilePath))) {
            Map<String, String> row;

            int rowNum = 1;
            while ((row = reader.readMap()) != null) {

                //List<Map<String,String>> jobInterviews =  splitRowV2(row);

                //Map<String,String> baseRow = jobInterviews.get(0);

                rowNum++;
        //        for (Map<String,String> jobInterview : jobInterviews) {
                    // process or write split…
                    System.out.println(row);
                    //  if(jobInterview.size() <= 86){
                    //      continue;
                    //  }

                referenceNumber = row.get("id");
                //referenceNumber += "-"+(rowNum-1);

                //referenceNumber = padReferenceNumber(referenceNumber);
                    RandomInterviewReport randomInterviewReport = new RandomInterviewReport();

                    randomInterviewReport.setReferenceNumber(referenceNumber);
                    System.out.println(referenceNumber);

                    ParticipantVO partVO = new ParticipantVO();

                    partVO.setReference(referenceNumber);

                    ParticipantVO participantVO = participantService.create(partVO);
                    ParticipantDetailsVO pd = new ParticipantDetailsVO();
                    pd.setDetailName("Priority");
                    pd.setDetailValue("0");
                    pd.setParticipantId(participantVO.getIdParticipant());
                    participantVO.getParticipantDetails().add(pd);

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

                    populateInterviewWithQuestions2(modVO, results, randomInterviewReport, newInterviewVO, participantVO,
                            isRandomAnswers, filterModuleVO,row);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<RandomInterviewReport> createRandomInterviews3(int count, Boolean isRandomAnswers,
                                                               String[] filterModuleVO) {
        // get active intro
        SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
        if (activeIntro == null || !StringUtils.isNumeric(activeIntro.getValue())) {
            log.error("Active intro is either null or is not numeric");
            return null;
        }
        // get the module
        JobModule mod = moduleDao.get(Long.valueOf(activeIntro.getValue()));
        ModuleVO modVO = moduleMapper.convertToModuleVO(mod, false);
        // get latest participant count
        String maxReferenceNumber = participantService.getMaxReferenceNumber();
        if (maxReferenceNumber == null) {
            maxReferenceNumber = PARTICIPANT_PREFIX + "000";
        }
        String referenceNumber = generateReferenceAuto(maxReferenceNumber);
        List<RandomInterviewReport> results = new ArrayList<>();

        //String csvFilePath = "/opt/data/amr/import/LAND3.csv";
        //csvFilePath = "/opt/data/amr/import/NONO5.csv";
        //csvFilePath = "/opt/data/amr/import/MINE1.csv";
        //csvFilePath = "/opt/data/amr/import/AREM.csv";
        //csvFilePath = "/opt/data/amr/import/ANEC.csv";
        //csvFilePath = "/opt/data/amr/import/CEMT.csv";
        //csvFilePath = "/opt/data/amr/import/FURN1.csv";
        //csvFilePath = "/opt/data/amr/import/INSU2.csv";
        //csvFilePath = "/opt/data/amr/import/LAUN.csv";
        //csvFilePath = "/opt/data/amr/import/TEXT.csv";
        //csvFilePath = "/opt/data/amr/import/TIPW.csv";
        //csvFilePath = "/opt/data/amr/import/TRAD.csv";
        //csvFilePath = "/opt/data/amr/import/WATE.csv";
        String directory = "/opt/data/amr/import/";
        Path dirPath = Paths.get(directory);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, "*.csv")) {
            System.out.println("Searching for CSV files in: " + dirPath.toAbsolutePath());

            for (Path entry : stream) {
                // Get the full absolute path
                System.out.println("Found: " + entry.toAbsolutePath());
                String csvFilePath = entry.toAbsolutePath().toString();

                try (CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(csvFilePath))) {
                    Map<String, String> row;

                    int rowNum = 1;
                    while ((row = reader.readMap()) != null) {

                        //List<Map<String,String>> jobInterviews =  splitRowV2(row);

                        //Map<String,String> baseRow = jobInterviews.get(0);

                        rowNum++;
                        //        for (Map<String,String> jobInterview : jobInterviews) {
                        // process or write split…
                        System.out.println(row);
                        //  if(jobInterview.size() <= 86){
                        //      continue;
                        //  }

                        referenceNumber = row.get("Participant");
                        referenceNumber += "-"+row.get("Job History");
                        //referenceNumber += "-"+(rowNum-1);

                        //referenceNumber = padReferenceNumber(referenceNumber);
                        RandomInterviewReport randomInterviewReport = new RandomInterviewReport();

                        randomInterviewReport.setReferenceNumber(referenceNumber);
                        System.out.println(referenceNumber);

                        ParticipantVO partVO = new ParticipantVO();

                        partVO.setReference(referenceNumber);

                        ParticipantVO participantVO = participantService.create(partVO);
                        ParticipantDetailsVO pd = new ParticipantDetailsVO();
                        pd.setDetailName("Priority");
                        pd.setDetailValue("0");
                        pd.setParticipantId(participantVO.getIdParticipant());
                        participantVO.getParticipantDetails().add(pd);

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

                        populateInterviewWithQuestions3(modVO, results, randomInterviewReport, newInterviewVO, participantVO,
                                isRandomAnswers, filterModuleVO,row);
                        //}
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                results = new ArrayList<>();


            }

        } catch (IOException e) {
            System.err.println("Error reading the directory: " + e.getMessage());
        }


        return results;
    }

    private String padReferenceNumber(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        // Split on first underscore
        int idx = input.indexOf('_');
        String prefix, suffix = null;
        if (idx >= 0) {
            prefix = input.substring(0, idx);
            suffix = input.substring(idx + 1);
        } else {
            prefix = input;
        }

        // Ensure the prefix is numeric
        if (!prefix.matches("\\d+")) {
            throw new IllegalArgumentException("Prefix must be numeric: " + prefix);
        }

        // Pad to length 5
        String padded = String.format("%05d", Integer.parseInt(prefix));

        return (suffix != null)
                ? padded + "_" + suffix
                : padded;
    }
    private List<Map<String, String>> splitRow(Map<String, String> originalRow) {
        Map<String, String> baseRow = new HashMap<>();
        Map<String, String> v2Row  = new HashMap<>();
        Map<String, String> v3Row  = new HashMap<>();
        Map<String, String> v4Row  = new HashMap<>();
        Map<String, String> v5Row  = new HashMap<>();

        // Original record_id before splitting
        String originalId = originalRow.get("record_id");

        // Distribute and filter fields
        for (Map.Entry<String, String> entry : originalRow.entrySet()) {
            String col = entry.getKey();
            String val = entry.getValue();

            // Skip null or empty
            if (val == null || val.trim().isEmpty()) continue;
            String trimmed = val.trim();

            // Skip numeric zeros
            boolean include = true;
            try {
                double num = Double.parseDouble(trimmed);
                if (num == 0) include = false;
            } catch (NumberFormatException ignored) {}
            if (!include) continue;

            // Route to appropriate version map (strip suffix)
            if (col.endsWith("_v2")) {
                v2Row.put(col.substring(0, col.length() - 3), trimmed);
            } else if (col.endsWith("_v3")) {
                v3Row.put(col.substring(0, col.length() - 3), trimmed);
            } else if (col.endsWith("_v4")) {
                v4Row.put(col.substring(0, col.length() - 3), trimmed);
            } else if (col.endsWith("_v5")) {
                v5Row.put(col.substring(0, col.length() - 3), trimmed);
            } else {
                baseRow.put(col, trimmed);
            }
        }

        // Append suffix to record_id and include in each split row
        for (int i = 1; i <= 5; i++) {
            Map<String, String> target;
            switch (i) {
                case 1: target = baseRow; break;
                case 2: target = v2Row;  break;
                case 3: target = v3Row;  break;
                case 4: target = v4Row;  break;
                default: target = v5Row; break;
            }
            if (originalId != null) {
                target.put("record_id", originalId + "_" + i);
            }
        }

        // Collect in order
        List<Map<String, String>> result = new ArrayList<>(5);
        result.add(baseRow);
        result.add(v2Row);
        result.add(v3Row);
        result.add(v4Row);
        result.add(v5Row);
        return result;
    }
    private List<Map<String, String>> splitRowV2(Map<String, String> originalRow) {
        Map<String, String> baseRow = new HashMap<>();
        Map<String, String> v2Row = new HashMap<>();
        Map<String, String> v3Row = new HashMap<>();
        Map<String, String> v4Row = new HashMap<>();
        Map<String, String> v5Row = new HashMap<>();

        // A flag to check if any of the specific "intr_1" columns have a valid value
        boolean hasIntr1ValidValue = false;

        // A map to hold all rows for easier management
        List<Map<String, String>> allRows = new ArrayList<>(5);
        allRows.add(baseRow);
        allRows.add(v2Row);
        allRows.add(v3Row);
        allRows.add(v4Row);
        allRows.add(v5Row);

        // Columns that do not start with 'intr' will be copied to all rows
        Map<String, String> commonCols = new HashMap<>();

        // Pattern to find columns starting with 'intr' and ending with a version suffix
        final Pattern intrVersionPattern = Pattern.compile("^(intr.*)_v(\\d+)$");

        for (Map.Entry<String, String> entry : originalRow.entrySet()) {
            String col = entry.getKey();
            String val = entry.getValue();

            if (val == null || val.trim().isEmpty()) {
                continue;
            }
            String trimmed = val.trim();

            try {
                if (Double.parseDouble(trimmed) == 0) {
                    continue;
                }
            } catch (NumberFormatException ignored) {}

            // Check for the specific condition based on the column name
            if ("intr_1".equalsIgnoreCase(col) || col.startsWith("intr_1_v")) {
                hasIntr1ValidValue = true;
            }

            Matcher matcher = intrVersionPattern.matcher(col);
            if (matcher.matches()) {
                // Case 1: 'intr' column with a version suffix
                String baseKey = matcher.group(1);
                String version = matcher.group(2);
                Map<String, String> targetRow = null;
                switch (version) {
                    case "2":
                        targetRow = v2Row;
                        break;
                    case "3":
                        targetRow = v3Row;
                        break;
                    case "4":
                        targetRow = v4Row;
                        break;
                    case "5":
                        targetRow = v5Row;
                        break;
                }
                if (targetRow != null) {
                    targetRow.put(baseKey, trimmed);
                }
            } else if (col.startsWith("intr")) {
                // Case 2: 'intr' column without a suffix, goes to the base row
                baseRow.put(col, trimmed);
            } else {
                // Case 3: All other columns
                commonCols.put(col, trimmed);
            }
        }

        // Final merging and adding record_id based on the flag
        String originalId = originalRow.get("record_id");
        if (hasIntr1ValidValue) {
            // If the specific condition is met, populate all rows as before
            for (int i = 0; i < allRows.size(); i++) {
                Map<String, String> row = allRows.get(i);
                row.putAll(commonCols);
                if (originalId != null) {
                    row.put("record_id", originalId + "_" + (i + 1));
                }
            }
        } else {
            // If the condition is not met, clear the versioned rows and add common columns to the base row only
            v2Row.clear();
            v3Row.clear();
            v4Row.clear();
            v5Row.clear();

            baseRow.putAll(commonCols);
            if (originalId != null) {
                baseRow.put("record_id", originalId + "_1");
            }
        }

        return allRows;
    }

    // Example usage inside your loop:
    /*
    for (Map<String,String> row : allRowsFromCsv) {
        List<Map<String,String>> exploded = splitRow(row);
        for (Map<String,String> split : exploded) {
            // process or write split…
            System.out.println(split);
        }
    }
    */

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createTestingAddresses() {

        List<ParticipantVO> participants = participantService.listAllParticipantWithInt();

        String filePath = "/opt/data/amrResidentialHistory.csv";
        List<ParticipantVO> participantAddresses = readCSV(filePath);

        int iIndex = 0;
        Long currentParticipantId = Long.valueOf(0);
        Long runningParticipantId = Long.valueOf(1);
        int iSize = participants.size();
        int iParticipantIndex = -1;
        for(int i=0;i<iSize;i++){
            ParticipantVO pWithAddress = participantAddresses.get(i);
            currentParticipantId = pWithAddress.getIdParticipant();
            if(currentParticipantId.equals(runningParticipantId)){
                System.out.println("Same Participant");
            }else{
                iParticipantIndex++;
            }
            ParticipantVO p = participants.get(iParticipantIndex);
            for(ParticipantDetailsVO pd:pWithAddress.getParticipantDetails()){
                if(pd!=null){
                    if(pd.getDetailName().endsWith("person_id")){
                        pd.setParticipantId(99999);
                    }else if(pd.getDetailName().endsWith("from_month")){
                        pd.setParticipantId(99999);
                    }else if(pd.getDetailName().endsWith("from_year")){
                        pd.setParticipantId(99999);
                    }else if(pd.getDetailName().endsWith("to_month")){
                        pd.setParticipantId(99999);
                    }else if(pd.getDetailName().endsWith("to_year")){
                        pd.setParticipantId(99999);
                    }else{
                        pd.setParticipantId(p.getIdParticipant());
                    }
                }
            }
            p.getParticipantDetails().addAll(pWithAddress.getParticipantDetails());

            runningParticipantId = pWithAddress.getIdParticipant();
            System.out.println("processing "+i+" of "+ iSize);

        }
        for(ParticipantVO p : participants){
            participantService.updateNewTransaction(p);
        }
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createTestingOccupationalHistories() {

        List<ParticipantVO> participants = participantService.listAllParticipantWithInt();

        String filePath = "/opt/data/amrOccupationalHistory.csv";
        List<ParticipantVO> participantOccupations = readOccupationalCSV(filePath);

        List<ParticipantVO> participantsJobs = new ArrayList<ParticipantVO>();
        SystemProperty activeIntro = systemPropertyDao.getByName("activeIntro");
        if (activeIntro == null || !StringUtils.isNumeric(activeIntro.getValue())) {
            log.error("Active intro is either null or is not numeric");

        }
        // get the module
        JobModule mod = moduleDao.get(Long.valueOf(activeIntro.getValue()));
        ModuleVO modVO = moduleMapper.convertToModuleVO(mod, false);

        int iIndex = 0;
        Long currentParticipantId = Long.valueOf(0);
        Long runningParticipantId = Long.valueOf(1);
        int iSize = participants.size();
        int iParticipantIndex = -1;
        ParticipantVO p = participants.get(0);
        int jobCount = 1;
        for(int i=0;i<iSize;i++){
            ParticipantVO pWithOccupation = participantOccupations.get(i);
            currentParticipantId = pWithOccupation.getIdParticipant();

            if(currentParticipantId.equals(runningParticipantId)){
                System.out.println("Same Participant");
                jobCount++;

            }else{
                jobCount = 1;
                iParticipantIndex++;
                p = participants.get(iParticipantIndex);
            }
            String reference = p.getReference();
            pWithOccupation.setReference(reference.substring(0,5)+"-J"+jobCount);
            ParticipantVO participantVO = participantService.create(pWithOccupation);
            System.out.println("Created "+participantVO.getReference());

            InterviewVO interviewVO = new InterviewVO();
            interviewVO.setParticipant(participantVO);
            interviewVO.setModule(modVO);
            interviewVO.setReferenceNumber(participantVO.getReference());
            // set default assessed status
            interviewVO.setAssessedStatus(AssessmentStatusEnum.NOTASSESSED.getDisplay());
            Interview interviewEntity = mapper.convertToInterview(interviewVO);

            interviewDao.saveNewTransaction(interviewEntity);
            InterviewVO newInterviewVO = mapper.convertToInterviewVO(interviewEntity);
            startInterview(modVO,newInterviewVO);

            for(ParticipantDetailsVO pd:pWithOccupation.getParticipantDetails()){
                if(pd!=null){
                    if(pd.getDetailName().endsWith("participantid")){
                        pd.setParticipantId(99999);
                    }else if(pd.getDetailName().endsWith("start_month")){
                        pd.setParticipantId(99999);
                    }else if(pd.getDetailName().endsWith("start_year")){
                        pd.setParticipantId(participantVO.getIdParticipant());
                    }else if(pd.getDetailName().endsWith("end_month")){
                        pd.setParticipantId(99999);
                    }else if(pd.getDetailName().endsWith("end_year")){
                        pd.setParticipantId(participantVO.getIdParticipant());
                    }else{
                        pd.setParticipantId(participantVO.getIdParticipant());
                    }
                }
            }

            participantVO.getParticipantDetails().addAll(pWithOccupation.getParticipantDetails());
            participantsJobs.add(participantVO);

            //participantService.updateNewTransaction(p);
            runningParticipantId = pWithOccupation.getIdParticipant();
            System.out.println("processing "+i+" of "+ iSize);

        }
        for(ParticipantVO p1 : participantsJobs){
            participantService.updateNewTransaction(p1);
        }
    }

    private List<ParticipantVO> readCSV(String filePath) {
        List<ParticipantVO> participantsList = new ArrayList<>();
        Map<String, Integer> personIdCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return participantsList; // Return empty if file is empty
            }

            String[] headers = headerLine.split(",");
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Handle empty values
                String personId = values[0]; // Assume person_id is the first column

                // Increment the count for this person_id or initialize it
                int recordCount = personIdCountMap.getOrDefault(personId, 0) + 1;
                personIdCountMap.put(personId, recordCount);

                ParticipantDetailsVO[] participantDetails = new ParticipantDetailsVO[headers.length];

                //add first record which is priority
                //participantDetails[0] = new ParticipantDetailsVO();
                //participantDetails[0].setDetailName("Priority");
                //participantDetails[0].setDetailValue("0");
                for (int i = 0; i < headers.length; i++) {
                    participantDetails[i] = new ParticipantDetailsVO();
                    String prefixedDetailName = "R" + recordCount + "-" + headers[i];
                    participantDetails[i].setDetailName(prefixedDetailName);
                    participantDetails[i].setDetailValue(values[i]);
                }
                ParticipantVO p = new ParticipantVO();
                p.setIdParticipant(Long.valueOf(personId));
                p.setParticipantDetails(Arrays.asList(participantDetails));
                participantsList.add(p);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return participantsList;
    }
    private List<ParticipantVO> readOccupationalCSV(String filePath) {
        List<ParticipantVO> participantsList = new ArrayList<>();
        Map<String, Integer> personIdCountMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return participantsList; // Return empty if file is empty
            }

            String[] headers = headerLine.split(",");
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1); // Handle empty values
                String personId = values[0]; // Assume person_id is the first column

                // Increment the count for this person_id or initialize it
                int recordCount = personIdCountMap.getOrDefault(personId, 0) + 1;
                personIdCountMap.put(personId, recordCount);

                ParticipantDetailsVO[] participantDetails = new ParticipantDetailsVO[headers.length+1];

                //add first record which is priority
                participantDetails[0] = new ParticipantDetailsVO();
                participantDetails[0].setDetailName("Priority");
                participantDetails[0].setDetailValue("-");
                for (int i = 1; i < headers.length; i++) {
                    participantDetails[i] = new ParticipantDetailsVO();
                    participantDetails[i].setDetailName(headers[i]);
                    participantDetails[i].setDetailValue(values[i]);
                }
                ParticipantVO p = new ParticipantVO();
                p.setIdParticipant(Long.valueOf(personId));
                p.setParticipantDetails(Arrays.asList(participantDetails));
                participantsList.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return participantsList;
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
        NoteVO qualtricsLinkNote = new NoteVO();
        qualtricsLinkNote.setInterviewId(newInterviewVO.getInterviewId());
        qualtricsLinkNote.setText("SV_eXo3qHX2ImA3ew6");
        qualtricsLinkNote.setType("AMRSurveyLink");
        notes.add(qualtricsLinkNote);
        randomInterview.getNotes().addAll(notes);
        interviewDao.saveNewTransaction(mapper.convertToInterview(randomInterview));
        participantVO.setStatus(2);
        participantService.updateNewTransaction(participantVO);
        results.add(randomInterviewReport);
    }
    private void populateInterviewWithQuestions1(NodeVO nodeVO, List<RandomInterviewReport> results,
                                                 RandomInterviewReport randomInterviewReport, InterviewVO newInterviewVO, ParticipantVO participantVO,
                                                 Boolean isRandomAnswers, String[] filterModuleVO, Map<String, String> jobInterview) {
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
        processRandomQuestionsAndAnswers1(randomInterview, randomInterviewReport, results, isRandomAnswers,
                filterModuleVO,jobInterview);
        List<NoteVO> notes = new ArrayList<>();
        NoteVO noteVO = new NoteVO();
        noteVO.setDeleted(0);
        noteVO.setInterviewId(randomInterview.getInterviewId());
        noteVO.setText("AUTO GENERATED INTERVIEW");
        noteVO.setType("System");
        notes.add(noteVO);
        NoteVO qualtricsLinkNote = new NoteVO();
        qualtricsLinkNote.setInterviewId(newInterviewVO.getInterviewId());
        qualtricsLinkNote.setText("SV_eXo3qHX2ImA3ew6");
        qualtricsLinkNote.setType("AMRSurveyLink");
        notes.add(qualtricsLinkNote);
        randomInterview.getNotes().addAll(notes);
        interviewDao.saveNewTransaction(mapper.convertToInterview(randomInterview));
        participantVO.setStatus(2);
        participantService.updateNewTransaction(participantVO);
        results.add(randomInterviewReport);
    }
    private void populateInterviewWithQuestions2(NodeVO nodeVO, List<RandomInterviewReport> results,
                                                 RandomInterviewReport randomInterviewReport, InterviewVO newInterviewVO, ParticipantVO participantVO,
                                                 Boolean isRandomAnswers, String[] filterModuleVO, Map<String, String> jobInterview) {
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
        processRandomQuestionsAndAnswers2(randomInterview, randomInterviewReport, results, isRandomAnswers,
                filterModuleVO,jobInterview);
        List<NoteVO> notes = new ArrayList<>();
        NoteVO noteVO = new NoteVO();
        noteVO.setDeleted(0);
        noteVO.setInterviewId(randomInterview.getInterviewId());
        noteVO.setText("FORSTA IMPORT");
        noteVO.setType("System"); //change this to interviewer notes
        notes.add(noteVO);
        NoteVO forstaLinkNote = new NoteVO();
        forstaLinkNote.setInterviewId(newInterviewVO.getInterviewId());
        String forstaLink = jobInterview.get("job_module_surveyid");
        forstaLinkNote.setText(forstaLink);
        forstaLinkNote.setType("System");
        notes.add(forstaLinkNote);
        NoteVO assessorNote = new NoteVO();
        assessorNote.setInterviewId(newInterviewVO.getInterviewId());
        String jobInformation = "main_OCC:";
        jobInformation += jobInterview.get("main_OCC").replace(':', '-');
        jobInformation += " :main_DUTIES:";
        jobInformation += jobInterview.get("main_DUTIES").replace(':', '-');
        //jobInformation += " :main_occ_autocoded:";
        //jobInformation += jobInterview.get("main_occ_autocoded");
        jobInformation += " :main_ANZSCO_code:";
        jobInformation += jobInterview.get("main_ANZSCO_code").replace(':', '-');
        jobInformation += " :main_ANZSCO_label:";
        jobInformation += jobInterview.get("main_ANZSCO_label").replace(':', '-');
        jobInformation += " :main_ANZSCO_confidence:";
        jobInformation += jobInterview.get("main_ANZSCO_confidence").replace(':', '-');
        jobInformation += " :main_LOC:";
        jobInformation += jobInterview.get("main_LOC_label").replace(':', '-');
       // jobInformation += " :main_multi_module_flag:";
       // jobInformation += jobInterview.get("main_multi_module_flag").replace(':', '-');
        jobInformation += " :main_MODULE_DES:";
        jobInformation += jobInterview.get("main_MODULE_DES").replace(':', '-');
        jobInformation += " :main_JOB10:";
        jobInformation += jobInterview.get("main_JOB10_label").replace(':', '-');
        jobInformation += " :main_JOB11:";
        jobInformation += jobInterview.get("main_JOB11_label").replace(':', '-');
        jobInformation += " :main_INDUSTRY:";
        jobInformation += jobInterview.get("main_INDUSTRY").replace(':', '-');
        assessorNote.setText(jobInformation);
        assessorNote.setType("Assessor");
        notes.add(assessorNote);
        randomInterview.getNotes().addAll(notes);
        interviewDao.saveNewTransaction(mapper.convertToInterview(randomInterview));
        participantVO.setStatus(2);
        participantService.updateNewTransaction(participantVO);
        results.add(randomInterviewReport);
    }
    private void populateInterviewWithQuestions3(NodeVO nodeVO, List<RandomInterviewReport> results,
                                                 RandomInterviewReport randomInterviewReport, InterviewVO newInterviewVO, ParticipantVO participantVO,
                                                 Boolean isRandomAnswers, String[] filterModuleVO, Map<String, String> jobInterview) {
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
        processRandomQuestionsAndAnswers3(randomInterview, randomInterviewReport, results, isRandomAnswers,
                filterModuleVO,jobInterview);
        List<NoteVO> notes = new ArrayList<>();
        NoteVO noteVO = new NoteVO();
        noteVO.setDeleted(0);
        noteVO.setInterviewId(randomInterview.getInterviewId());
        noteVO.setText("AMR IMPORT");
        noteVO.setType("System"); //change this to interviewer notes
        notes.add(noteVO);

        NoteVO assessorNote = new NoteVO();
        assessorNote.setInterviewId(newInterviewVO.getInterviewId());
        String jobInformation = "AMRID:";
        jobInformation += jobInterview.get("AMRID");
        jobInformation += " :JH:";
        jobInformation += jobInterview.get("Job History");
        assessorNote.setText(jobInformation);
        assessorNote.setType("Assessor");
        notes.add(assessorNote);
        randomInterview.getNotes().addAll(notes);
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
    private void processRandomQuestionsAndAnswers1(InterviewVO randomInterview,
                                                   RandomInterviewReport randomInterviewReport, List<RandomInterviewReport> results, Boolean isRandomAnswers,
                                                   String[] filterModuleVO, Map<String, String> jobInterview) {
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
                        processRandomQuestionsAndAnswers1(randomInterview, randomInterviewReport, results,
                                isRandomAnswers, filterModuleVO, jobInterview);
                    }
                }
            } else if (answers.isEmpty()) {
                saveQuestion1(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                        filterModuleVO,jobInterview);
                InterviewAnswerVO interviewAnswerVO = new InterviewAnswerVO();
                interviewAnswerVO.setName("ERROR: No answer to select for this Question.");
                randomInterviewReport.getListAnswer().add(interviewAnswerVO);
            } else if (questionAsked.getType().equalsIgnoreCase("Q_multiple")) {

                List<PossibleAnswerVO> answersToSave = new ArrayList<PossibleAnswerVO>();
                String prefix = "";
                NodeVO parentModule = questionService.getTopModuleByTopNodeId(questionAsked.getTopNodeId());
                prefix = parentModule.getName().substring(0,4).toLowerCase();

                for(PossibleAnswerVO ans: answers) {
                    String searchString = prefix+"_"+ans.getNumber();
                    if(getEntriesByKeyContains(jobInterview,searchString).size() > 0) {
                        answersToSave.add(ans);
                    }
                }
                saveMultiAnswer1(randomInterview,questionAsked, answersToSave,randomInterviewReport, isRandomAnswers,filterModuleVO);
                //saveAnswer1(randomInterview, questionAsked, answers, randomInterviewReport, isRandomAnswers,
                //        filterModuleVO,jobInterview);
                saveQuestion1(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                        filterModuleVO,jobInterview);
            } else {
                saveAnswer1(randomInterview, questionAsked, answers, randomInterviewReport, isRandomAnswers,
                            filterModuleVO,jobInterview);
                saveQuestion1(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                            filterModuleVO,jobInterview);
            }
        }
    }
    private void processRandomQuestionsAndAnswers2(InterviewVO randomInterview,
                                                   RandomInterviewReport randomInterviewReport, List<RandomInterviewReport> results, Boolean isRandomAnswers,
                                                   String[] filterModuleVO, Map<String, String> jobInterview) {
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
                        processRandomQuestionsAndAnswers2(randomInterview, randomInterviewReport, results,
                                isRandomAnswers, filterModuleVO, jobInterview);
                    }
                }
            } else if (answers.isEmpty()) {
                saveQuestion2(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                        filterModuleVO,jobInterview);
                InterviewAnswerVO interviewAnswerVO = new InterviewAnswerVO();
                interviewAnswerVO.setName("ERROR: No answer to select for this Question.");
                randomInterviewReport.getListAnswer().add(interviewAnswerVO);
            } else if (questionAsked.getType().equalsIgnoreCase("Q_multiple")) {

                List<PossibleAnswerVO> answersToSave = new ArrayList<PossibleAnswerVO>();
                String prefix = "";
                NodeVO parentModule = questionService.getTopModuleByTopNodeId(questionAsked.getTopNodeId());
                prefix = parentModule.getName().substring(0,4)+questionAsked.getQuestionId();


                for(PossibleAnswerVO ans: answers) {
                    String searchString =  selectedJobModule +"_"+prefix+"_"+ans.getNumber();
                    String multiQuestionAnswer = getEntryByKey(jobInterview,searchString);
                    if(multiQuestionAnswer.equalsIgnoreCase("1")) {
                        answersToSave.add(ans);
                    }
                }
                saveMultiAnswer2(randomInterview,questionAsked, answersToSave,randomInterviewReport, isRandomAnswers,filterModuleVO);
                //saveAnswer1(randomInterview, questionAsked, answers, randomInterviewReport, isRandomAnswers,
                //        filterModuleVO,jobInterview);
                saveQuestion2(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                        filterModuleVO,jobInterview);
            } else {
                saveAnswer2(randomInterview, questionAsked, answers, randomInterviewReport, isRandomAnswers,
                        filterModuleVO,jobInterview);
                saveQuestion2(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                        filterModuleVO,jobInterview);
            }
        }
    }
    private void processRandomQuestionsAndAnswers3(InterviewVO randomInterview,
                                                   RandomInterviewReport randomInterviewReport, List<RandomInterviewReport> results, Boolean isRandomAnswers,
                                                   String[] filterModuleVO, Map<String, String> jobInterview) {
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
                        processRandomQuestionsAndAnswers3(randomInterview, randomInterviewReport, results,
                                isRandomAnswers, filterModuleVO, jobInterview);
                    }
                }
            } else if (answers.isEmpty()) {
                saveQuestion3(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                        filterModuleVO,jobInterview);
                InterviewAnswerVO interviewAnswerVO = new InterviewAnswerVO();
                interviewAnswerVO.setName("ERROR: No answer to select for this Question.");
                randomInterviewReport.getListAnswer().add(interviewAnswerVO);
            } else if (questionAsked.getType().equalsIgnoreCase("Q_multiple")) {

                List<PossibleAnswerVO> answersToSave = new ArrayList<PossibleAnswerVO>();
                String qNodeNumber = questionAsked.getNumber();
                String columnName = getOldAMRId(qNodeNumber);
                if(columnName != null){
                    List<String> actualAnswers = findValuesContaining(jobInterview,columnName);

                    if(selectedJobModule.equalsIgnoreCase("MINE")){
                        for(PossibleAnswerVO ans: answers) {
                            for (String answer : actualAnswers) {
                                if(answer.contains("underground")){
                                    if(ans.getName().contains("underground")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("open cut")){
                                    if(ans.getName().contains("open cut")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("Crushing")){
                                    if(ans.getName().contains("Crushing")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("mill")){
                                    if(ans.getName().contains("mill")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("agging")){
                                    if(ans.getName().contains("agging")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("driving")){
                                    if(ans.getName().contains("driving")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("office")){
                                    if(ans.getName().contains("office")){
                                        answersToSave.add(ans);
                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                for(String answer: actualAnswers){
                                    if(!answer.trim().equalsIgnoreCase("")){
                                        boolean alreadyAdded = false;
                                        for(PossibleAnswerVO ans1:answersToSave){
                                            if(ans1.getName().equalsIgnoreCase(answer)){
                                                alreadyAdded = true;
                                            }
                                        }
                                        if(!alreadyAdded){
                                            ans.setName(answer);
                                            answersToSave.add(ans);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(selectedJobModule.equalsIgnoreCase("NONO")){
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                for(String answer: actualAnswers){
                                    if(!answer.trim().equalsIgnoreCase("")){
                                        ans.setName(answer);
                                    }
                                }
                                answersToSave.add(ans);
                            } else {
                                for (String answer : actualAnswers) {
                                    System.out.println(answer);
                                    if(answer.contains("etween 1942")){
                                        if(ans.getName().contains("rom 1942")){
                                            answersToSave.add(ans);
                                        }
                                    } else if(qNodeNumber.toLowerCase().contains("9a1")) {
                                        try {
                                            int year = Integer.parseInt(answer);
                                            if (year < 1967) {
                                                if(ans.getName().contains("efore the mine closed")){
                                                    answersToSave.add(ans);
                                                }
                                            } else if (year > 1966 ) {
                                                if(ans.getName().contains("fter the mine was closed")){
                                                    answersToSave.add(ans);
                                                }
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Issue with :"+randomInterview.getReferenceNumber()+"-"+actualQuestion.getName());
                                            System.out.println("Defaulting to Last possible answer for:"+answer);
                                        }
                                    } else if(qNodeNumber.toLowerCase().contains("9a4")) {

                                        if(answer.contains("business reasons")){
                                            if(ans.getName().contains("business reasons")){
                                                answersToSave.add(ans);
                                            }
                                        } else if(answer.contains("including holiday")){
                                            if(ans.getName().contains("including holiday")){
                                                answersToSave.add(ans);
                                            }
                                        } else if(answer.contains("ther reason")){
                                            if(ans.getName().contains("ther reason")){
                                                answersToSave.add(ans);
                                            }
                                        }

                                    }
                                    else{
                                        try {
                                            int year = Integer.parseInt(answer);

                                            if (year <= 1941) {
                                                if(ans.getName().contains("efore 1942")){
                                                    answersToSave.add(ans);
                                                }
                                            } else if (year >= 1942 && year <= 1987) {
                                                if(ans.getName().contains("etween 1942")){
                                                    answersToSave.add(ans);
                                                }
                                            } else if (year > 1988) {
                                                if(ans.getName().contains("fter 1987")){
                                                    answersToSave.add(ans);
                                                }
                                            }
                                        } catch (NumberFormatException e) {
                                            System.out.println("Issue with :"+randomInterview.getReferenceNumber()+"-"+actualQuestion.getName());
                                            System.out.println("Defaulting to Last possible answer for:"+answer);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(selectedJobModule.equalsIgnoreCase("LAND")){
                        if(questionAsked.getNumber().equalsIgnoreCase("2A1")){
                            for(PossibleAnswerVO ans: answers) {
                                for (String answer : actualAnswers) {
                                    if((answer.contains("Yes")) || (answer.contains("No"))){
                                        if(ans.getName().equalsIgnoreCase("Asbestos insulation or lagging")){
                                            answersToSave.add(ans);
                                        }
                                    }
                                }

                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            for (String answer : actualAnswers) {
                                if(answer.contains("Yes")){
                                    if(ans.getName().contains("flocking")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("Raw asbestos")){
                                    if(ans.getName().contains("Raw asbestos")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("Manufactured asbestos")){
                                    if(ans.getName().contains("Manufactured asbestos")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("Used asbestos")){
                                    if(ans.getName().contains("Used asbestos")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("No")){
                                    if(ans.getName().contains("No")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("Don't know")){
                                    if(ans.getName().contains("Don't know")){
                                        answersToSave.add(ans);
                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                for(String answer: actualAnswers){
                                    if((!answer.trim().equalsIgnoreCase("")) && (!answer.trim().equalsIgnoreCase("No"))){
                                        boolean alreadyAdded = false;
                                        for(PossibleAnswerVO ans1:answersToSave){
                                            if(ans1.getName().equalsIgnoreCase(answer)){
                                                alreadyAdded = true;
                                            }
                                        }
                                        if(!alreadyAdded){
                                            if(!answer.equalsIgnoreCase("Yes")){
                                                ans.setName(answer);
                                                answersToSave.add(ans);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(selectedJobModule.equalsIgnoreCase("AREM")){
                        ArrayList<String> answersManaged = new ArrayList<String>();
                        for(PossibleAnswerVO ans: answers) {
                            for (String answer : actualAnswers) {
                                if(answer.contains("Fibro")){
                                    if(ans.getName().contains("Fibro")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Fire rated")){
                                    if(ans.getName().contains("Fire rated")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Vinyl")){
                                    if(ans.getName().contains("Vinyl")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Asbesos cavity")){
                                    if(ans.getName().contains("Asbestos insulation")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Any asbestos")){
                                    if(ans.getName().contains("Asbestos insulation")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Lagging")){
                                    if(ans.getName().contains("Asbestos insulation")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Asbestos ceiling")){
                                    if(ans.getName().contains("Asbestos insulation")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                actualAnswers.removeAll(answersManaged);
                                for(String answer: actualAnswers){
                                    if(!answer.trim().equalsIgnoreCase("")){
                                        ans.setName(answer);
                                        answersToSave.add(ans);

                                    }
                                }
                            }
                        }
                    }
                    else if(selectedJobModule.equalsIgnoreCase("ANEC")){
                        ArrayList<String> answersManaged = new ArrayList<String>();


                    }
                    else if(selectedJobModule.equalsIgnoreCase("CEMT")){
                        ArrayList<String> answersManaged = new ArrayList<String>();
                        for(PossibleAnswerVO ans: answers) {
                            for (String answer : actualAnswers) {
                                if(answer.contains("Gypsum")){
                                    if(ans.getName().contains("Gypsum")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Basic")){
                                    if(ans.getName().contains("Basic")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Fibrous")){
                                    if(ans.getName().contains("Fibrous")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Office")){
                                    if(ans.getName().contains("office")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                actualAnswers.removeAll(answersManaged);
                                for(String answer: actualAnswers){
                                    if(!answer.trim().equalsIgnoreCase("")){
                                        ans.setName(answer);
                                        answersToSave.add(ans);

                                    }
                                }
                            }
                        }
                    }
                    else if(selectedJobModule.equalsIgnoreCase("FURN")){
                        ArrayList<String> answersManaged = new ArrayList<String>();
                        for(PossibleAnswerVO ans: answers) {
                            for (String answer : actualAnswers) {
                                if(answer.contains("Fibro")){
                                    if(ans.getName().contains("Fibro")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Fire")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Fire")){
                                        answersToSave.add(ans);
                                    }
                                } else if(answer.contains("Instulation")){
                                    if(ans.getName().contains("insulation")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("batts")){
                                    if(ans.getName().contains("batts")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Sprayed")){
                                    if(ans.getName().contains("Sprayed")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("textile lagging")){
                                    if(ans.getName().contains("textile lagging")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("Loose")){
                                    if(ans.getName().contains("Loose")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                } else if(answer.contains("never")){
                                    if(ans.getName().contains("never")){
                                        answersToSave.add(ans);
                                        answersManaged.add(answer);
                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                actualAnswers.removeAll(answersManaged);
                                for(String answer: actualAnswers){
                                    if(!answer.trim().equalsIgnoreCase("")){
                                        ans.setName(answer);
                                        answersToSave.add(ans);

                                    }
                                }
                            }
                        }
                    }
                    else if(selectedJobModule.equalsIgnoreCase("INSU")){
                        ArrayList<String> answersManaged = new ArrayList<String>();
                        if(questionAsked.getNumber().equalsIgnoreCase("2A1")){
                            for(PossibleAnswerVO ans: answers) {
                                if(ans.getName().equalsIgnoreCase("Asbestos insulation or lagging")) {
                                    answersToSave.add(ans);
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            for (String answer : actualAnswers) {
                                if(answer.contains("Fire")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Fire")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("batts")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("batts")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Sprayed")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Sprayed")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("blankets")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("blankets")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Loose-fill")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Loose fill")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Buildings")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Buildings")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Ships")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Ships")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Trains")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Trains")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Furnaces")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Furnaces")){
                                        answersToSave.add(ans);

                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                actualAnswers.removeAll(answersManaged);
                                for(String answer: actualAnswers){
                                    if(!answer.trim().equalsIgnoreCase("")){
                                        ans.setName(answer);
                                        answersToSave.add(ans);
                                        if(!questionAsked.getNumber().equalsIgnoreCase("3")){
                                            break;
                                        }

                                    }
                                }
                            }
                        }
                    }
                    else if(selectedJobModule.equalsIgnoreCase("TEXT")){
                        ArrayList<String> answersManaged = new ArrayList<String>();

                        for(PossibleAnswerVO ans: answers) {
                            for (String answer : actualAnswers) {
                                if(answer.contains("Fire")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Fire")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Vinyl floor")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Vinyl floor")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Neither")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("Neither")){
                                        answersToSave.add(ans);

                                    }
                                } else if(answer.contains("Office")){
                                    answersManaged.add(answer);
                                    if(ans.getName().contains("ffice")){
                                        answersToSave.add(ans);

                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                actualAnswers.removeAll(answersManaged);
                                for(String answer: actualAnswers){
                                    if(!answer.trim().equalsIgnoreCase("")){
                                        ans.setName(answer);
                                        answersToSave.add(ans);
                                    }
                                }
                            }
                        }

                    }
                    else if(selectedJobModule.equalsIgnoreCase("TRAD")){
                        if(questionAsked.getNumber().equalsIgnoreCase("2A1")){
                            for(PossibleAnswerVO ans: answers) {
                                for (String answer : actualAnswers) {
                                    if(answer.contains("Yes")){
                                        if(ans.getName().equalsIgnoreCase("Asbestos insulation or lagging")){
                                            answersToSave.add(ans);
                                        }
                                    }
                                }

                            }
                        }
                        if(questionAsked.getNumber().equalsIgnoreCase("5A1")){
                            for(PossibleAnswerVO ans: answers) {
                                for (String answer : actualAnswers) {
                                    if(answer.contains("Yes")){
                                        if(ans.getName().equalsIgnoreCase("Arc or Stick")){
                                            answersToSave.add(ans);
                                        }
                                    }
                                }

                            }
                        }
                        if(questionAsked.getNumber().equalsIgnoreCase("3")){
                            for(PossibleAnswerVO ans: answers) {
                                for (String answer : actualAnswers) {
                                    if(answer.contains("Yes")){
                                        if(ans.getName().equalsIgnoreCase("Yes")){
                                            answersToSave.add(ans);
                                        }
                                    }
                                }

                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            for (String answer : actualAnswers) {
                                if(answer.contains("Yes")){
                                    if(ans.getName().contains("flocking")){
                                        answersToSave.add(ans);
                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                for(String answer: actualAnswers){
                                    if(!answer.trim().equalsIgnoreCase("")){
                                        boolean alreadyAdded = false;
                                        for(PossibleAnswerVO ans1:answersToSave){
                                            if(ans1.getName().equalsIgnoreCase(answer)){
                                                alreadyAdded = true;
                                            }
                                        }
                                        if(!alreadyAdded){
                                            if(!answer.equalsIgnoreCase("Yes")){
                                                ans.setName(answer);
                                                answersToSave.add(ans);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    else if(selectedJobModule.equalsIgnoreCase("WATE")){
                        boolean alreadyAdded = false;
                        if(questionAsked.getNumber().equalsIgnoreCase("3A1A1D1")){
                            for(PossibleAnswerVO ans: answers) {
                                for (String answer : actualAnswers) {
                                    if(answer.contains("Yes")){
                                        if(ans.getName().equalsIgnoreCase("Sprayed on lagging (flocking)")){
                                            answersToSave.add(ans);
                                        }
                                    } else if(answer.contains("No")){
                                        if(ans.getName().equalsIgnoreCase("Don't know")){
                                            answersToSave.add(ans);
                                        }
                                    }
                                }

                            }
                        } else if(questionAsked.getNumber().equalsIgnoreCase("3A1A1")){
                            for(PossibleAnswerVO ans: answers) {
                                for (String answer : actualAnswers) {
                                    if(answer.contains("nsulation")){
                                        if(ans.getName().contains("nsulation")){
                                            answersToSave.add(ans);
                                            alreadyAdded = true;
                                        }
                                    } else if(answer.contains("illboard")){
                                        if(ans.getName().contains("illboard")){
                                            answersToSave.add(ans);
                                        }
                                    }
                                }

                            }
                        } else if(questionAsked.getNumber().equalsIgnoreCase("3A2")){
                            for(PossibleAnswerVO ans: answers) {
                                for (String answer : actualAnswers) {
                                    if(answer.contains("Yes")){
                                        if(ans.getName().equalsIgnoreCase("Yes")){
                                            answersToSave.add(ans);
                                        }
                                    } else if(answer.contains("No")){
                                        if(ans.getName().equalsIgnoreCase("No")){
                                            answersToSave.add(ans);
                                        }
                                    }
                                }

                            }
                        } else {
                            for (PossibleAnswerVO ans : answers) {
                                for (String answer : actualAnswers) {
                                    if (answer.contains("Submarines")) {
                                        if (ans.getName().contains("Submarines")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("Other military")) {
                                        if (ans.getName().contains("Other military")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("Merchant navy")) {
                                        if (ans.getName().contains("Merchant navy")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("Ocean-going")) {
                                        if (ans.getName().contains("Ocean-going")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("emergency vessels")) {
                                        if (ans.getName().contains("emergency vessels")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("ishing boats")) {
                                        if (ans.getName().contains("ishing boats")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("argo")) {
                                        if (ans.getName().contains("Merchant navy")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("arrier")) {
                                        if (ans.getName().contains("Merchant navy")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("Merchant vessels")) {
                                        if (ans.getName().contains("Merchant navy")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("anker")) {
                                        if (ans.getName().contains("Merchant navy")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("steamships")) {
                                        if (ans.getName().contains("Merchant navy")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("iver barge")) {
                                        if (ans.getName().contains("river boats")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("self propelled")) {
                                        if (ans.getName().contains("river boats")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("Fishing vessels")) {
                                        if (ans.getName().contains("ishing boats")) {
                                            answersToSave.add(ans);
                                        }
                                    } else if (answer.contains("powerboats")) {
                                        if (ans.getName().contains("Small power boats")) {
                                            answersToSave.add(ans);
                                        }
                                    }
                                }
                            }
                        }
                        for(PossibleAnswerVO ans: answers) {
                            if(ans.getType().equalsIgnoreCase("P_freetext")){
                                for(String answer: actualAnswers){
                                    if((!answer.trim().equalsIgnoreCase("")) && (!answer.trim().equalsIgnoreCase("No"))){
                                        for(PossibleAnswerVO ans1:answersToSave){
                                            if(ans1.getName().equalsIgnoreCase(answer)){
                                                alreadyAdded = true;
                                            }
                                        }
                                        if(!alreadyAdded){
                                            if(!answer.equalsIgnoreCase("Yes")){
                                                ans.setName(answer);
                                                answersToSave.add(ans);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }else{
                    int iAnswer = answers.size()-1;
                    PossibleAnswerVO selectedAnswer = answers.get(iAnswer);
                    //selectedAnswer.setDeleted(1);
                    answersToSave.add(selectedAnswer);
                }
                if(answersToSave.size()==0){
                    int iAnswer = answers.size()-1;
                    PossibleAnswerVO selectedAnswer = answers.get(iAnswer);
                    //selectedAnswer.setDeleted(1);
                    answersToSave.add(selectedAnswer);
                }
                saveMultiAnswer3(randomInterview,questionAsked, answersToSave,randomInterviewReport, isRandomAnswers,filterModuleVO);

                saveQuestion3(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                        filterModuleVO,jobInterview);
            } else {
                saveAnswer3(randomInterview, questionAsked, answers, randomInterviewReport, isRandomAnswers,
                        filterModuleVO,jobInterview);
                saveQuestion3(randomInterview, questionAsked, randomInterviewReport, results, isRandomAnswers,
                        filterModuleVO,jobInterview);
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

    private void startInterview(ModuleVO nodeVO, InterviewVO newInterviewVO){

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


        InterviewQuestionVO linkAndQueueQuestions = interviewQuestionService
                .updateInterviewLinkAndQueueQuestions(interviewQuestionVO);
        // get interview
        List<InterviewVO> interviewList = mapper
                .convertToInterviewWithQuestionAnswerList(interviewDao.getInterview(newInterviewVO.getInterviewId()));

        InterviewVO randomInterview = interviewList.get(0);

        List<InterviewQuestionVO> questionList = randomInterview.getQuestionHistory();
        InterviewQuestionVO questionAsked = findNextQuestionQueued(questionList);

        questionAsked.setProcessed(true);
        interviewQuestionService.updateIntQ(questionAsked);
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
    private void saveQuestion1(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                               RandomInterviewReport randomInterviewReport, List<RandomInterviewReport> results, Boolean isRandomAnswers,
                               String[] filterModuleVO, Map<String, String> jobInterview) {
        questionAsked.setProcessed(true);
        interviewQuestionService.updateIntQ(questionAsked);
        randomInterviewReport.getListQuestion().add(questionAsked);
        processRandomQuestionsAndAnswers1(randomInterview, randomInterviewReport, results, isRandomAnswers,
                filterModuleVO,jobInterview);
    }
    private void saveQuestion2(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                               RandomInterviewReport randomInterviewReport, List<RandomInterviewReport> results, Boolean isRandomAnswers,
                               String[] filterModuleVO, Map<String, String> jobInterview) {
        questionAsked.setProcessed(true);
        interviewQuestionService.updateIntQ(questionAsked);
        randomInterviewReport.getListQuestion().add(questionAsked);
        processRandomQuestionsAndAnswers2(randomInterview, randomInterviewReport, results, isRandomAnswers,
                filterModuleVO,jobInterview);
    }
    private void saveQuestion3(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                               RandomInterviewReport randomInterviewReport, List<RandomInterviewReport> results, Boolean isRandomAnswers,
                               String[] filterModuleVO, Map<String, String> jobInterview) {
        questionAsked.setProcessed(true);
        interviewQuestionService.updateIntQ(questionAsked);
        randomInterviewReport.getListQuestion().add(questionAsked);
        processRandomQuestionsAndAnswers3(randomInterview, randomInterviewReport, results, isRandomAnswers,
                filterModuleVO,jobInterview);
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
    private void saveAnswer1(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                             List<PossibleAnswerVO> answers, RandomInterviewReport randomInterviewReport, Boolean isRandomAnswers,
                             String[] filterModuleVO, Map<String, String> jobInterview) {

        PossibleAnswerVO selectedAnswer = null;
        if (!isRandomAnswers) {
            selectedAnswer = answers.get(0);
        } else {
            String prefix = "";
            NodeVO parentModule = questionService.getTopModuleByTopNodeId(questionAsked.getTopNodeId());
            prefix = parentModule.getName().substring(0,4).toLowerCase();
            String questionColumnName = prefix+"_"+questionAsked.getNumber();

            selectedAnswer = chooseRandomAnswer1(answers, filterModuleVO,jobInterview,questionColumnName);
            if(selectedAnswer==null){
                System.out.println("REDCAP_ISSUE:"+randomInterview.getReferenceNumber());
                System.out.println("REDCAP_ISSUE:"+questionColumnName+" No answer found!");
                int iAnswer = answers.size()-1;
                selectedAnswer = answers.get(iAnswer);
                selectedAnswer.setDeleted(1);
            }
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
    private void saveAnswer2(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                             List<PossibleAnswerVO> answers, RandomInterviewReport randomInterviewReport, Boolean isRandomAnswers,
                             String[] filterModuleVO, Map<String, String> jobInterview) {

        PossibleAnswerVO selectedAnswer = null;
        if (!isRandomAnswers) {
            selectedAnswer = answers.get(0);
        } else {
            String prefix = "";
            NodeVO parentModule = questionService.getTopModuleByTopNodeId(questionAsked.getTopNodeId());
            prefix = parentModule.getName().substring(0,4).toLowerCase();
            String questionColumnName = prefix+questionAsked.getQuestionId();
            if(questionColumnName.equalsIgnoreCase("simp73180")){
                questionColumnName = "job_module_name";
            }
            selectedAnswer = chooseRandomAnswer2(answers, filterModuleVO,jobInterview,questionColumnName);
            if(selectedAnswer==null){
                System.out.println("REDCAP_ISSUE:"+randomInterview.getReferenceNumber());
                System.out.println("REDCAP_ISSUE:"+questionColumnName+" No answer found!");
                int iAnswer = answers.size()-1;
                selectedAnswer = answers.get(iAnswer);
                selectedAnswer.setDeleted(1);
            }
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
    private void saveAnswer3(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                             List<PossibleAnswerVO> answers, RandomInterviewReport randomInterviewReport, Boolean isRandomAnswers,
                             String[] filterModuleVO, Map<String, String> jobInterview) {

        PossibleAnswerVO selectedAnswer = null;
        if (!isRandomAnswers) {
            selectedAnswer = answers.get(0);
        } else {
            String prefix = "";
            NodeVO parentModule = questionService.getTopModuleByTopNodeId(questionAsked.getTopNodeId());
            prefix = parentModule.getName().substring(0,4).toLowerCase();
            String questionColumnName = prefix+questionAsked.getQuestionId();
            if(questionColumnName.equalsIgnoreCase("INTR77795")){
                questionColumnName = "job_module_name";
                selectedJobModule = jobInterview.get(questionColumnName);
                for(PossibleAnswerVO ans: answers){
                    if(ans.getName().equalsIgnoreCase(selectedJobModule)){
                        selectedAnswer = ans;
                        break;
                    }
                }

            }else{
                if((!selectedJobModule.equalsIgnoreCase("NONO")) && questionAsked.getNumber().equalsIgnoreCase("1")){
                    String yearStarted = jobInterview.get("JHStarted");
                    String nodeNumber;

                    try {
                        int year = Integer.parseInt(yearStarted);

                        if (year < 1981) {
                            nodeNumber = "1A";
                        } else if (year >= 1981 && year <= 1990) {
                            nodeNumber = "1B";
                        } else if (year > 1990) {
                            nodeNumber = "1C";
                        } else {
                            nodeNumber = "1D";
                        }
                    } catch (NumberFormatException e) {
                        nodeNumber = "1D";
                    }
                    for(PossibleAnswerVO ans: answers){
                        if(ans.getNumber().equalsIgnoreCase(nodeNumber)){
                            selectedAnswer = ans;
                            break;
                        }
                    }
                }else{
                    //find the key based on the nodeNumber
                    String qNodeNumber = questionAsked.getNumber();
                    String columnName = getOldAMRId(qNodeNumber);
                    questionColumnName = columnName;
                    String answer = jobInterview.get(columnName);
                    if(answer == null){
                        answer = "No";
                    } else if (answer.equalsIgnoreCase("")){
                        answer = "No";
                    }
                    for(PossibleAnswerVO ans: answers){
                        if(ans.getName().equalsIgnoreCase(answer)){
                            selectedAnswer = ans;
                            break;
                        }else if(ans.getType().equalsIgnoreCase("P_freetext")){
                            ans.setName(answer);
                            selectedAnswer = ans;
                            if(questionAsked.getNumber().equalsIgnoreCase("9a4b1")){
                                ans.setName(answer);
                                selectedAnswer = ans;
                            }
                        }
                    }
                    if((selectedJobModule.equalsIgnoreCase("TRAD")) && questionAsked.getNumber().equalsIgnoreCase("9A1")){
                        String weeksPeryear = answer;
                        String nodeNumber;
                        try {
                            int weeks = Integer.parseInt(weeksPeryear);
                            if (weeks < 12) {
                                nodeNumber = "9A1D";
                            } else if (weeks >= 12 && weeks <= 46) {
                                nodeNumber = "9A1C";
                            } else if (weeks > 46) {
                                nodeNumber = "9A1B";
                            } else {
                                nodeNumber = "9A1E";
                            }
                        } catch (NumberFormatException e) {
                            nodeNumber = "9A1E";
                        }
                        for(PossibleAnswerVO ans1: answers){
                            if(ans1.getNumber().equalsIgnoreCase(nodeNumber)){
                                selectedAnswer = ans1;
                                break;
                            }
                        }
                    } else if((selectedJobModule.equalsIgnoreCase("WATE")) && questionAsked.getNumber().equalsIgnoreCase("2A2")){
                        List<String> actualAnswers = findValuesContaining(jobInterview,columnName);
                        for(PossibleAnswerVO ans1: answers){
                            for (String answer1 : actualAnswers) {
                                if(answer1.contains("Bridge")){
                                    if(ans1.getName().equalsIgnoreCase("Yes")){
                                        selectedAnswer = ans1;
                                    }
                                } else if(answer1.contains("Engine")){
                                    if(ans1.getName().equalsIgnoreCase("Yes")){
                                        selectedAnswer = ans1;
                                    }
                                } else if(answer1.contains("Auxillary machinery")){
                                    if(ans1.getName().equalsIgnoreCase("Yes")){
                                        selectedAnswer = ans1;
                                    }
                                } else if(answer1.contains("boiler room")){
                                    if(ans1.getName().equalsIgnoreCase("Yes")){
                                        selectedAnswer = ans1;
                                    }
                                } else if(answer1.contains("Gear rooms")){
                                    if(ans1.getName().equalsIgnoreCase("Yes")){
                                        selectedAnswer = ans1;
                                    }
                                } else if(answer1.contains("auxialliary boiler")){
                                    if(ans1.getName().equalsIgnoreCase("Yes")){
                                        selectedAnswer = ans1;
                                    }
                                } else if(answer1.contains("marine technician")){
                                    if(ans1.getName().equalsIgnoreCase("Yes")){
                                        selectedAnswer = ans1;
                                    }
                                } else if(answer1.contains("mostly boiler repairs")){
                                    if(ans1.getName().equalsIgnoreCase("Yes")){
                                        selectedAnswer = ans1;
                                    }
                                }
                            }
                        }


                    } else if(selectedJobModule.equalsIgnoreCase("NONO")) {

                        if(qNodeNumber.toLowerCase().contains("9a2")){

                            for(PossibleAnswerVO ans1: answers){
                                try {
                                    int year = Integer.parseInt(answer);
                                    if (year < 3) {
                                        if(ans1.getName().contains("ne or two times")){
                                            selectedAnswer = ans1;
                                        }
                                    } else if (year > 2 ) {
                                        if(ans1.getName().contains("ore than 2 times")){
                                            selectedAnswer = ans1;
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Issue with :"+randomInterview.getReferenceNumber());
                                    System.out.println("Defaulting to Last possible answer for:"+answer);
                                }
                            }
                        }else if(qNodeNumber.toLowerCase().contains("9a3")){

                            for(PossibleAnswerVO ans1: answers){
                                try {
                                    int year = Integer.parseInt(answer);
                                    if (year < 5) {
                                        if(ans1.getName().contains("ot more than 5")){
                                            selectedAnswer = ans1;
                                        }
                                    } else if (year > 4 && year < 21 ) {
                                        if(ans1.getName().contains("5 to 20")){
                                            selectedAnswer = ans1;
                                        }
                                    } else if (year > 20 ) {
                                        if(ans1.getName().contains("ore than 20")){
                                            selectedAnswer = ans1;
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Issue with :"+randomInterview.getReferenceNumber());
                                    System.out.println("Defaulting to Last possible answer for:"+answer);
                                }
                            }
                        }


                    }
                }
            }


            //selectedAnswer = chooseRandomAnswer3(answers, filterModuleVO,jobInterview,questionColumnName);
            if(selectedAnswer==null){
                System.out.println("AMR_ISSUE:"+randomInterview.getReferenceNumber());
                System.out.println("AMR_ISSUE:"+questionColumnName+" lookup id");
                System.out.println("AMR_ISSUE:"+questionAsked.getNumber()+" No answer found!");
                int iAnswer = answers.size()-1;
                selectedAnswer = answers.get(iAnswer);
                //selectedAnswer.setDeleted(1);
            }
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
    private List<String> findValuesContaining(Map<String, String> jobInterview, String searchString) {
        List<String> matchingValues = new ArrayList<>();

        for (Map.Entry<String, String> entry : jobInterview.entrySet()) {
            if (entry.getKey() != null && entry.getKey().contains(searchString)) {
                matchingValues.add(entry.getValue());
            }
        }

        return matchingValues;
    }
    private String getOldAMRId(String nodeNumber) {
        String csvFile = "/opt/data/amr/"+selectedJobModule+"_idLookUp.csv";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header line
            br.readLine();

            // Read each line
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(csvSplitBy);

                // Check if NodeNumber matches
                if (columns.length >= 2 && columns[0].trim().equalsIgnoreCase(nodeNumber)) {
                    return columns[1].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return null if not found
        return null;
    }
    private void saveMultiAnswer1(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                             List<PossibleAnswerVO> answersToSave, RandomInterviewReport randomInterviewReport, Boolean isRandomAnswers,
                             String[] filterModuleVO) {


        for(PossibleAnswerVO answer: answersToSave) {
            InterviewAnswerVO interviewAnswer = populateInterviewAnswer(randomInterview, answer);
            interviewAnswer.setInterviewQuestionId(questionAsked.getId());
            interviewAnswer.setIsProcessed(true);
            List<InterviewAnswerVO> listOfAnswers = new ArrayList<>();
            listOfAnswers.add(interviewAnswer);
            List<InterviewQuestionVO> questions = interviewAnswerService
                    .saveIntervewAnswersAndGetChildQuestion(listOfAnswers);
            randomInterviewReport.getListAnswer().add(interviewAnswer);
            refreshUnprocessedQuestions(questions, randomInterview);
        }
    }
    private void saveMultiAnswer2(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                                  List<PossibleAnswerVO> answersToSave, RandomInterviewReport randomInterviewReport, Boolean isRandomAnswers,
                                  String[] filterModuleVO) {


        for(PossibleAnswerVO answer: answersToSave) {
            InterviewAnswerVO interviewAnswer = populateInterviewAnswer(randomInterview, answer);
            interviewAnswer.setInterviewQuestionId(questionAsked.getId());
            interviewAnswer.setIsProcessed(true);
            List<InterviewAnswerVO> listOfAnswers = new ArrayList<>();
            listOfAnswers.add(interviewAnswer);
            List<InterviewQuestionVO> questions = interviewAnswerService
                    .saveIntervewAnswersAndGetChildQuestion(listOfAnswers);
            randomInterviewReport.getListAnswer().add(interviewAnswer);
            refreshUnprocessedQuestions(questions, randomInterview);
        }
    }
    private void saveMultiAnswer3(InterviewVO randomInterview, InterviewQuestionVO questionAsked,
                                  List<PossibleAnswerVO> answersToSave, RandomInterviewReport randomInterviewReport, Boolean isRandomAnswers,
                                  String[] filterModuleVO) {


        for(PossibleAnswerVO answer: answersToSave) {
            InterviewAnswerVO interviewAnswer = populateInterviewAnswer(randomInterview, answer);
            interviewAnswer.setInterviewQuestionId(questionAsked.getId());
            interviewAnswer.setIsProcessed(true);
            List<InterviewAnswerVO> listOfAnswers = new ArrayList<>();
            listOfAnswers.add(interviewAnswer);
            List<InterviewQuestionVO> questions = interviewAnswerService
                    .saveIntervewAnswersAndGetChildQuestion(listOfAnswers);
            randomInterviewReport.getListAnswer().add(interviewAnswer);
            refreshUnprocessedQuestions(questions, randomInterview);
        }
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
    private PossibleAnswerVO chooseRandomAnswer1(List<PossibleAnswerVO> answers, String[] filterModuleVO, Map<String, String> jobInterview, String questionColumnName) {
    /*    if (filterModuleVO != null && filterModuleVO.length > 0) {
            String filterModuleId = filterModuleVO[0];
            // choose an answer that would have the link module
            for (PossibleAnswerVO ans : answers) {
                List<Long> linksByAnswerId = interviewDao.getLinksByAnswerId(ans.getIdNode());
                if (linksByAnswerId.contains(Long.valueOf(filterModuleId))) {
                    return ans;
                }
            }
        }*/
        int rnd = new Random().nextInt(answers.size());
        if (rnd == answers.size()) {
            rnd = rnd - 1;
        }
        String actualAnswerNumber = getByKeySuffix(jobInterview,questionColumnName);//jobInterview.get(questionColumnName);
        if(actualAnswerNumber != null){
            int index = actualAnswerNumber.lastIndexOf("_");
            actualAnswerNumber = actualAnswerNumber.substring(index+1);
        }
        PossibleAnswerVO retValue = null;
        for(PossibleAnswerVO ans: answers){
            if(ans.getNumber().equalsIgnoreCase(actualAnswerNumber)){
                retValue = ans;
                break;
            }
        }
        if(retValue == null){
            //retValue = answers.get(rnd);
            //System.out.println(questionColumnName+" No answer found!");
        }


        return retValue;
    }

    private String selectedJobModule = "";

    private PossibleAnswerVO chooseRandomAnswer2(List<PossibleAnswerVO> answers, String[] filterModuleVO, Map<String, String> jobInterview, String questionColumnName) {
    /*    if (filterModuleVO != null && filterModuleVO.length > 0) {
            String filterModuleId = filterModuleVO[0];
            // choose an answer that would have the link module
            for (PossibleAnswerVO ans : answers) {
                List<Long> linksByAnswerId = interviewDao.getLinksByAnswerId(ans.getIdNode());
                if (linksByAnswerId.contains(Long.valueOf(filterModuleId))) {
                    return ans;
                }
            }
        }*/
        int rnd = new Random().nextInt(answers.size());
        if (rnd == answers.size()) {
            rnd = rnd - 1;
        }
        String actualAnswerNumber = "";
        if(questionColumnName.equalsIgnoreCase("job_module_name")){
            actualAnswerNumber = getByKeySuffix(jobInterview,questionColumnName);
            selectedJobModule = actualAnswerNumber.substring(0,4);
        }else{
            actualAnswerNumber = getByKeyPrefixAndSuffix(jobInterview,selectedJobModule,questionColumnName);
        }
        PossibleAnswerVO retValue = null;

        if(questionColumnName.equalsIgnoreCase("job_module_name")){
            for(PossibleAnswerVO ans: answers){
                if(ans.getName().equalsIgnoreCase(selectedJobModule)){
                    retValue = ans;
                    break;
                }
            }
        }else{
            for(PossibleAnswerVO ans: answers){
                if(ans.getNumber().equalsIgnoreCase(actualAnswerNumber)){
                    retValue = ans;
                    break;
                }
            }
        }
        if(retValue == null){
            //retValue = answers.get(rnd);
            //System.out.println(questionColumnName+" No answer found!");
        }


        return retValue;
    }
    private PossibleAnswerVO chooseRandomAnswer3(List<PossibleAnswerVO> answers, String[] filterModuleVO, Map<String, String> jobInterview, String questionColumnName) {

        int rnd = new Random().nextInt(answers.size());
        if (rnd == answers.size()) {
            rnd = rnd - 1;
        }
        String actualAnswerNumber = "";
        if(questionColumnName.equalsIgnoreCase("job_module_name")){
            actualAnswerNumber = getByKeySuffix(jobInterview,questionColumnName);
            selectedJobModule = actualAnswerNumber.substring(0,4);
        }else{
            actualAnswerNumber = getByKeyPrefixAndSuffix(jobInterview,selectedJobModule,questionColumnName);
        }
        PossibleAnswerVO retValue = null;

        if(questionColumnName.equalsIgnoreCase("job_module_name")){
            for(PossibleAnswerVO ans: answers){
                if(ans.getName().equalsIgnoreCase(selectedJobModule)){
                    retValue = ans;
                    break;
                }
            }
        }else{
            for(PossibleAnswerVO ans: answers){
                if(ans.getNumber().equalsIgnoreCase(actualAnswerNumber)){
                    retValue = ans;
                    break;
                }
            }
        }
        if(retValue == null){
            //retValue = answers.get(rnd);
            //System.out.println(questionColumnName+" No answer found!");
        }


        return retValue;
    }

    private String getByKeySuffix(Map<String,String> map, String suffix) {
        return map.entrySet().stream()
                .filter(e -> e.getKey().endsWith(suffix))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
    private String getByKeyPrefixAndSuffix(Map<String,String> map, String prefix, String suffix) {
        return map.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().startsWith(prefix.toLowerCase())
                        && e.getKey().toLowerCase().endsWith(suffix.toLowerCase()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
    private String getByOldAMRID(Map<String,String> map, String prefix, String suffix) {
        return map.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().startsWith(prefix.toLowerCase())
                        && e.getKey().toLowerCase().endsWith(suffix.toLowerCase()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }
    private Map<String, String> getEntriesByKeyContains(Map<String, String> map, String substring) {
        return map.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().contains(substring.toLowerCase()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
    private String getEntryByKey(Map<String, String> map, String substring) {
        return map.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().contains(substring.toLowerCase()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
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

                boolean isStudySpecificQuestion = false;
                try {
                    SystemPropertyVO introModule = systemPropertyService.getByName(Constant.STUDY_INTRO);
                    if(!introModule.getValue().equalsIgnoreCase(String.valueOf(vo.getTopNodeId()))){
                        String[] listOfIdNodes = studyAgentUtil.getStudyAgentCSV(String.valueOf(vo.getTopNodeId()));
                        for(String id: listOfIdNodes){
                            if(id.equalsIgnoreCase(String.valueOf(vo.getQuestionId()))){
                                isStudySpecificQuestion = true;
                                break;
                            }
                        }
                    }else{
                        isStudySpecificQuestion = true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(isStudySpecificQuestion){
                    intQuestionVO = vo;
                    break;
                }

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
        if (length < 5) {
            for (int i = length; i < 5; i++) {
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

    @Override
    public SystemPropertyVO preloadFilterStudyAgent(Long idNode) {
        return interviewQuestionDao.preloadFilterStudyAgent(idNode);
    }

    @Override
    public void autoAssessedRules() {
        log.info("Started assessing the rules.");
        List<Interview> interviews = interviewDao.getAllInterviewsWithAnswersAndAssessments();
        List<Long> listAgentIds = agentDao.getStudyAgentIds();
        CompletableFuture<Interview>[] interviewsToBeAssessed = new CompletableFuture[interviews.size()];
        AtomicInteger count = new AtomicInteger();

        for (Interview interview : interviews) {
            int countVar = count.incrementAndGet();
            interviewsToBeAssessed[countVar - 1] = autoAssessmentService.autoAssessedRule(listAgentIds, interview)
                    .whenComplete((i, e) ->  log.info("completed processing interview id {} and is {} of {}"
                            , i.getIdinterview(), countVar, interviews.size()));

        }

        List<Interview> interviewsToBeProcessed = Stream.of(interviewsToBeAssessed)
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        bulkUpdate(interviewsToBeProcessed);
        if (qualtricsConfig.isConfigured()) {
        	interviewsToBeProcessed.forEach(interview -> updateQualtricsResults(interview.getIdinterview()));
        }
        
        log.info("Completed assessing the rules count {}.", interviews.size());
    }

    @Override
    public InterviewVO updateFiredRule(long interviewId) {
        Interview interview = interviewDao.getInterviewWithAnswersAndAssessments(interviewId);
        autoAssessmentService.deleteOldAutoAssessments(interview);
        autoAssessmentService.updateNotes(interview);
        autoAssessmentService.updateManualAssessedRules(interview);
        List<Rule> firedRules = autoAssessmentService.determineFiredRules(interview);
        log.info("fired rules identified for interview {} is {}"
                , interview.getIdinterview()
                , firedRules.size());
        interview.setFiredRules(firedRules);
        update(interview);
        return mapper.convertToInterviewVO(interview);
    }

    @Override
    public void updateQualtricsResults(long interviewId) {
        if(interviewId != -1){
            Interview interview = interviewDao.get(interviewId);
            List<Long> listAgentIds = agentDao.getStudyAgentIds();
            String strWorkShiftHours = iqsfService.getWorkshift(interview);
            if(!strWorkShiftHours.equalsIgnoreCase("N/A")) {
                BigDecimal workshift = new BigDecimal(strWorkShiftHours);
                iqsfService.saveAssessmentResults(interview.getReferenceNumber(), listAgentIds, interview, workshift);
            }
        }

    }


}