package org.occideas.interview.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.occideas.agent.service.AgentService;
import org.occideas.base.rest.BaseRestController;
import org.occideas.entity.Interview;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.fragment.service.FragmentService;
import org.occideas.interview.service.InterviewService;
import org.occideas.interviewanswer.service.InterviewAnswerService;
import org.occideas.interviewautoassessment.service.InterviewAutoAssessmentService;
import org.occideas.interviewfiredrules.service.InterviewFiredRulesService;
import org.occideas.interviewmanualassessment.service.InterviewManualAssessmentService;
import org.occideas.interviewmodule.service.InterviewModuleService;
import org.occideas.module.service.ModuleService;
import org.occideas.modulerule.service.ModuleRuleService;
import org.occideas.question.service.QuestionService;
import org.occideas.systemproperty.service.SystemPropertyService;
import org.occideas.utilities.AssessmentStatusEnum;
import org.occideas.utilities.StudyAgentUtil;
import org.occideas.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.math.BigInteger;
import java.util.*;

@Path("/interview")
public class InterviewRestController implements BaseRestController<InterviewVO> {

  private static final Logger log = LogManager.getLogger(InterviewRestController.class);

  @Autowired
  private InterviewService service;

  @Autowired
  private InterviewAutoAssessmentService autoAssessmentService;

  @Autowired
  private InterviewManualAssessmentService manualAssessmentService;

  @Autowired
  private InterviewFiredRulesService firedRulesService;

  @Autowired
  private InterviewAnswerService interviewAnswerService;

  @Autowired
  private QuestionService questionService;

  @Autowired
  private InterviewModuleService modService;

  @Autowired
  private AgentService agentService;

  @Autowired
  private ModuleRuleService moduleRuleService;

  @Autowired
  private SystemPropertyService sysPropService;

  @Autowired
  private ModuleService moduleService;

  @Autowired
  private FragmentService fragmentService;

  @Autowired
  private StudyAgentUtil studyAgentUtil;

  @GET
  @Path(value = "/getlist")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response listAll() {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.listAll();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getInterviewsWithoutAnswers")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response listAllInterviewsWithoutAnswers() {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.listAllInterviewsWithoutAnswers();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getAllInterviewId")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getAllInterviewId() {
    List<Long> interviewIdlist = new ArrayList<Long>();
    try {
      interviewIdlist = service.getInterviewIdlist();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(interviewIdlist).build();
  }

  @GET
  @Path(value = "/getlistwithanswers")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response listAllWithAnswers() {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.listAllWithAnswers();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getassessments")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response listAssessments() {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.listAssessments();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/get")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response get(@QueryParam("id") Long id) {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.findById(id);
      list.get(0).setActualQuestion(sort(list.get(0).getQuestionHistory(), id));
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getInterviewWithRules")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getInterviewWithRules(@QueryParam("id") Long id) {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.findByIdWithRules(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getInterviewsListWithRules")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getInterviewsWithRules() {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.listAllWithRulesVO(null);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/getUnprocessedQuestions")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getUnprocessedQuestions(@QueryParam("id") Long id) {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.getUnprocessedQuestions(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/preloadActiveIntro")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response preloadActiveIntro() {
    try {
      SystemPropertyVO systemPropertyVO = service.preloadActiveIntro();
      if (systemPropertyVO == null || systemPropertyVO.getValue().toLowerCase().trim().equals("false")) {
        log.error("You should set filterStudyAgent as true");
        return Response.status(Status.EXPECTATION_FAILED).type("text/plain").entity("You should set filterStudyAgent as true in admin config").build();
      }
//			this.testStudySpecificModules();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @GET
  @Path(value = "/preloadAllModules")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response preloadAllModules() {
    try {
      service.preloadAllModules();
//			this.testStudySpecificModules();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }


  @GET
  @Path(value = "/getbyref")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getByRef(@QueryParam("ref") String referenceNumber) {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.findByReferenceNumber(referenceNumber);
      if (list.size() > 0) {
        return Response.ok(list).build();
      } else {
        return Response.status(Response.Status.NO_CONTENT).build();
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }

  }

  @GET
  @Path(value = "/updateAutoAssessments")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response updateAutoAssessments() {
    List<InterviewVO> list = service.listAllInterviewsWithoutAnswers();
    try {

      List<AgentVO> listAgents = agentService.getStudyAgents();
      int iSize = list.size();
      int iCount = 0;
      for (InterviewVO interviewVO : list) {
        iCount++;
        log.info("auto assessment for " + interviewVO.getInterviewId() + " triggered " + iCount + " of " + iSize);
        System.out.println("ID " + interviewVO.getInterviewId() + " triggered " + iCount + " of " + iSize);
        interviewVO = this.determineFiredRules(interviewVO);
        // get interviewautoassessment for each interview
        // can change if we can do a batch retrieve for all interviews
        List<InterviewAutoAssessmentVO> assessments = autoAssessmentService.findByInterviewId(interviewVO.getInterviewId());
        ArrayList<RuleVO> autoAssessedRules = new ArrayList<RuleVO>();
        for (InterviewAutoAssessmentVO assessment : assessments) {
          assessment.getRule().setDeleted(1);
          autoAssessedRules.add(assessment.getRule());
        }
        List<InterviewFiredRulesVO> interviewFiredRules = firedRulesService.findByInterviewId(interviewVO.getInterviewId());
        ArrayList<RuleVO> firedRules = new ArrayList<RuleVO>();
        for (InterviewFiredRulesVO ifiredRule : interviewFiredRules) {
          RuleVO firedRule = ifiredRule.getRules().get(0);
          firedRules.add(firedRule);
        }

        setRuleLevelNoExposure(listAgents, autoAssessedRules, firedRules);
        evaluateAssessmentStatus(interviewVO);

        interviewVO.setAutoAssessedRules(autoAssessedRules);
        System.out.println("Status is" + interviewVO.getAssessedStatus());
        service.update(interviewVO);

      }
      log.info("Assessment completed for interviewIds");
    } catch (Throwable e) {
      log.error("Error Occured on assessment", e.getMessage());
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  private void evaluateAssessmentStatus(InterviewVO interviewVO) {
    if (hasManualAssessedRules(interviewVO)) {
      interviewVO.setAssessedStatus(AssessmentStatusEnum.MANUALLYASSESSED.getDisplay());
    } else {
      if (interviewVO.getAssessedStatus().equalsIgnoreCase(AssessmentStatusEnum.NOTASSESSED.getDisplay())) {
        interviewVO.setAssessedStatus(AssessmentStatusEnum.AUTOASSESSED.getDisplay());
      } else {
        //leave status the same for now
      }

    }
  }

  private boolean hasManualAssessedRules(InterviewVO interviewVO) {
    return interviewVO.getManualAssessedRules() != null &&
      !interviewVO.getManualAssessedRules().isEmpty();
  }

  private void setRuleLevelNoExposure(List<AgentVO> listAgents, ArrayList<RuleVO> autoAssessedRules, ArrayList<RuleVO> firedRules) {
    for (AgentVO agent : listAgents) {
      RuleVO rule = new RuleVO();
      rule.setLevel("noExposure");
      rule.setLevelValue(5);
      rule.setAgentId(agent.getIdAgent());
      for (RuleVO ruleFired : firedRules) {
        if (ruleFired.getAgentId() == agent.getIdAgent()) {
          if (ruleFired.getLevelValue() <= rule.getLevelValue()) {
            rule.setLevel(ruleFired.getLevel());
            rule.setLevelValue(ruleFired.getLevelValue());
            rule.setAgentId(ruleFired.getAgentId());
            rule.setLegacyRuleId(ruleFired.getIdRule());
          }
        }
      }
      autoAssessedRules.add(rule);
    }
  }

  @GET
  @Path(value = "/getfiredrules")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getfiredrules(@QueryParam("id") Long id) {

    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.findByIdWithRules(id, false);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/updatefiredrules")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response updateFiredRules(@QueryParam("id") Long id) {
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    try {
      list = service.findByIdWithRules(id);
      for (InterviewVO interviewVO : list) {
        interviewVO = this.determineFiredRules(interviewVO);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @Path(value = "/create")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response create(InterviewVO json) {
    InterviewVO interviewVO;
    try {
      interviewVO = service.create(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(interviewVO).build();
  }

  @Path(value = "/update")
  @POST
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response update(InterviewVO json) {
    try {

      service.update(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @Path(value = "/delete")
  @POST
  public Response delete(InterviewVO json) {
    try {
      service.delete(json);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok().build();
  }

  @POST
  @Path(value = "/nextquestion")
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getNextQuestion(@QueryParam("parentId") String parentId, @QueryParam("number") String number) {
    try {
      QuestionVO questionVO = getNearestQuestion(parentId, number);
      if (questionVO != null) {
        return Response.ok(questionVO).build();
      } else {
        return Response.status(Response.Status.NO_CONTENT).build();
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @POST
  @Path(value = "/nextquestionfromqueue")
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getNextQuestion(InterviewVO interview) {
    try {
      QuestionVO questionVO = null;
      for (InterviewQuestionVO historyQuestion : interview.getQuestionHistory()) {
        if (!historyQuestion.isProcessed()) {
          for (QuestionVO question : questionService.findById(historyQuestion.getId())) {
            questionVO = question;
          }
          break;
        }
      }
      if (questionVO != null) {
        return Response.ok(questionVO).build();
      } else {
        return Response.status(Response.Status.NO_CONTENT).build();
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @POST
  @Path(value = "/saveMod")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  public Response saveMod(InterviewModuleVO vo) {
    modService.update(vo);
    return Response.ok(vo).build();
  }

  @GET
  @Path(value = "/getInterview")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getInterview(@QueryParam("interviewId") long idinterview) {
    try {
      List<InterviewVO> vo = service.getInterview(idinterview);
      if (vo != null) {
        return Response.ok(vo).build();
      } else {
        return Response.status(Response.Status.NO_CONTENT).build();
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  @GET
  @Path(value = "/getInterviewQuestionAnswer")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getInterviewQuestionAnswer(@QueryParam("interviewId") long idinterview) {
    try {
      List<InterviewVO> vo = service.getInterviewQuestionAnswerVO(idinterview);
      if (vo != null) {
        return Response.ok(vo).build();
      } else {
        return Response.status(Response.Status.NO_CONTENT).build();
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  private QuestionVO getNearestQuestion(String parentId, String number) {
    return questionService.determineNextQuestionByCurrentNumber(parentId, number);
  }

  private InterviewVO determineFiredRules(InterviewVO interview) {
    ArrayList<RuleVO> firedRules = new ArrayList<RuleVO>();
    ArrayList<RuleVO> rules = new ArrayList<RuleVO>();
    // TODO need to move this call every after interview that is finished
    service.cleanDeletedAnswers(interview.getInterviewId());
    // get all answers by interviewId

    /**
     * TODO START refactor to use 1 sql query to get the answerhistory joined with modulerule table
     */
    List<InterviewAnswerVO> answerHistory = interviewAnswerService.findByInterviewId(interview.getInterviewId());
    //h
    List<ModuleRuleVO> moduleRules = new ArrayList<ModuleRuleVO>();
    for (InterviewAnswerVO ia : answerHistory) {
      //for(InterviewAnswerVO ia:iq.getAnswers()){
      List<ModuleRuleVO> nodeModuleRules = moduleRuleService.findByIdNode(ia.getAnswerId());

      moduleRules.addAll(nodeModuleRules);

      //}
    }
    /**
     * TODO END refactor to use 1 sql query to get the answerhistory joined with modulerule table
     */
    for (ModuleRuleVO moduleRule : moduleRules) {
      if (!rules.contains(moduleRule.getRule())) {
        rules.add(moduleRule.getRule());
      }
    }
    //get list of answer nodes
    List<InterviewAnswerVO> allActualAnswers = answerHistory;
    allActualAnswers = removeDeletedAnswers(allActualAnswers);
    for (RuleVO rule : rules) {
      boolean bFired = false;
      for (PossibleAnswerVO pa : rule.getConditions()) {
        boolean bPartialFired = false;
        for (InterviewAnswerVO ia : allActualAnswers) {
          if (pa.getIdNode() == ia.getAnswerId()) {
            bPartialFired = true;
            bFired = true;
            break;
          }
        }
        if (!bPartialFired) {
          bFired = false;
          break;
        }
      }
      if (bFired) {
        if (!firedRules.contains(rule)) {
          firedRules.add(rule);
        }
      }
    }
    interview.setFiredRules(firedRules);
    NoteVO note = new NoteVO();
    note.setInterviewId(interview.getInterviewId());
    note.setText("Ran determineFiredRules");
    note.setType("System");
    interview.getNotes().add(note);
    List<InterviewManualAssessmentVO> assessmentRules = manualAssessmentService.findByInterviewId(interview.getInterviewId());
    ArrayList<RuleVO> manualAssessedRules = new ArrayList<RuleVO>();
    for (InterviewManualAssessmentVO assessment : assessmentRules) {
      manualAssessedRules.add(assessment.getRule());
    }
    interview.setManualAssessedRules(manualAssessedRules);
    //System.out.print("Assessment status before update:"+interview.getAssessedStatus());
    service.update(interview);
    return interview;
  }

  private List<InterviewAnswerVO> removeDeletedAnswers(List<InterviewAnswerVO> allActualAnswers) {
    ArrayList<InterviewAnswerVO> retValue = new ArrayList<InterviewAnswerVO>();
    for (InterviewAnswerVO answer : allActualAnswers) {
      if (answer.getDeleted() == 0) {
        retValue.add(answer);
      }
    }
    return retValue;
  }

  private ArrayList<RuleVO> removeDuplicates(List<RuleVO> rules) {
    ArrayList<RuleVO> retValue = new ArrayList<RuleVO>();
    for (RuleVO rule : rules) {
      if (!retValue.contains(rule)) {
        retValue.add(rule);
      }
    }
    return retValue;
  }

  @GET
  @Path(value = "/getAssessmentSize")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getAutoAssessedSize(@QueryParam("status") String status) {
    try {
      BigInteger count = service.listAllWithRuleCount(status);
      return Response.ok(count).build();
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
  }

  private List<InterviewQuestionVO> sort(
    List<InterviewQuestionVO> inputQuestionHistory,
    Long interviewId) {
    System.out.println("Sort start " + new Date());
    List<InterviewQuestionVO> sortedQuestionHistory = new ArrayList<InterviewQuestionVO>();

    // Get sorted intro module
    Long moduleId = service.getIntroModuleId(interviewId);
    List<ModuleVO> sortedQuestion = moduleService.findById(moduleId);
    List<NodeVO> sortedQuestions = new ArrayList<NodeVO>();
    sortedQuestions.addAll(sortedQuestion);

    List<Long> moduleList = new ArrayList<Long>();
    // Make a map of the question history, populate the module list
    Map<Long, InterviewQuestionVO> questionMap = getMap(inputQuestionHistory,
      moduleList,
      sortedQuestionHistory);

    //Get linked module
    for (Long id : moduleList) {
      List<ModuleVO> sortedLinkQuestion = moduleService.findById(Long.valueOf(id));
      linkQuestion(sortedLinkQuestion.get(0).getChildNodes(), sortedQuestions);
    }

    //Sort
    for (NodeVO module : sortedQuestions) {
      if (module instanceof ModuleVO) {
        findQuestion(((ModuleVO) module).getChildNodes(),
          sortedQuestionHistory,
          questionMap);
      } else if (module instanceof QuestionVO) {
        findQuestion(sortedQuestionHistory, questionMap, (QuestionVO) module);
      }
    }

    System.out.println("Sort end " + new Date());
    return sortedQuestionHistory;
  }

  private void linkQuestion(List<QuestionVO> childNodes, List<NodeVO> sortedQuestions) {

    for (QuestionVO vo : childNodes) {
      if (vo.getLink() != 0) {
        //Get linked module
        List<ModuleVO> sortedLinkQuestion = moduleService.findById(Long.valueOf(vo.getLink()));

        if (!sortedLinkQuestion.isEmpty() && sortedLinkQuestion.get(0) != null) {
          sortedQuestions.addAll(sortedLinkQuestion);
          linkQuestion(sortedLinkQuestion.get(0).getChildNodes(), sortedQuestions);
        } else {
          List<FragmentVO> sortedLinkFragment = fragmentService.findById(Long.valueOf(vo.getLink()));
          if (sortedLinkFragment != null && sortedLinkFragment.get(0) != null) {
            linkQuestion(sortedLinkFragment.get(0).getChildNodes(), sortedQuestions);
          }
        }
      } else {
        sortedQuestions.add(vo);
        linkAnswer(vo.getChildNodes(), sortedQuestions);
      }
    }
  }

  private void linkAnswer(List<PossibleAnswerVO> childNodes, List<NodeVO> sortedQuestions) {
    for (PossibleAnswerVO vo : childNodes) {
      linkQuestion(vo.getChildNodes(), sortedQuestions);
    }
  }

  private Map<Long, InterviewQuestionVO> getMap(
    List<InterviewQuestionVO> questionHistory,
    List<Long> moduleList,
    List<InterviewQuestionVO> sortedQuestionHistory) {

    //Map of the actual unsorted question history
    Map<Long, InterviewQuestionVO> map = new HashMap<Long, InterviewQuestionVO>();

    for (InterviewQuestionVO vo : questionHistory) {

      if (vo.getQuestionId() == 0) {
        if ("Q_linkedmodule".equalsIgnoreCase(vo.getType())) {
          moduleList.add(vo.getTopNodeId());
        }
        //sortedQuestionHistory will contain modules at this point
        sortedQuestionHistory.add(vo);
      } else {
        map.put(vo.getQuestionId(), vo);
      }
    }
    return map;
  }

  private void findQuestion(List<QuestionVO> childNodes, List<InterviewQuestionVO> sortedQuestionHistory,
                            Map<Long, InterviewQuestionVO> questionMap) {

    for (QuestionVO vo : childNodes) {
      findQuestion(sortedQuestionHistory, questionMap, vo);
      if (vo.getChildNodes() != null) {
        findAnswer(sortedQuestionHistory, questionMap, vo.getChildNodes());
      }
    }
  }

  private void findAnswer(List<InterviewQuestionVO> sortedQuestionHistory, Map<Long, InterviewQuestionVO> questionMap,
                          List<PossibleAnswerVO> childNodes) {

    for (PossibleAnswerVO vo : childNodes) {

      if (questionMap.containsKey(vo.getIdNode())) {
        sortedQuestionHistory.add(questionMap.get(vo.getIdNode()));
      }

      if (vo.getChildNodes() != null) {
        findQuestion(vo.getChildNodes(), sortedQuestionHistory, questionMap);
      }
    }
  }

  private void findQuestion(List<InterviewQuestionVO> sortedQuestionHistory, Map<Long, InterviewQuestionVO> questionMap,
                            QuestionVO vo) {

    if (questionMap.containsKey(vo.getIdNode())) {
      sortedQuestionHistory.add(questionMap.get(vo.getIdNode()));
    }
  }

  /**
   * Make a module object out of the interview question list
   *
   * @param interviewId
   * @return
   */
  @GET
  @Path(value = "/getModuleForInterview")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getModuleForInterview(@QueryParam("id") Long interviewId) {
    List<ModuleVO> list = new ArrayList<ModuleVO>();
    SystemPropertyVO vo = null;
    Long moduleId = service.getIntroModuleId(interviewId);

    try {

      //Will return the intro module
      list = moduleService.findByIdNoRules(moduleId);
      if (list.get(0) != null) {
        list.add(mapToModule(list.remove(0).getChildNodes(), interviewId, false, moduleId, false));
      }

    } catch (Throwable e) {

      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  private ModuleVO mapToModule(List<QuestionVO> childNodes,
                               Long interviewId,
                               boolean isSubModule,
                               long linkId,
                               boolean isExpanded) {

    //Get the interview
    List<Interview> interview = service.getInterviewQuestionAnswer(interviewId);

    Map<Long, InterviewQuestion> questionMap = new HashMap<Long, InterviewQuestion>();
    Map<Long, InterviewAnswer> answerMap = new HashMap<Long, InterviewAnswer>();

    //Create a map of the questions and answers
    getMap(interview.get(0).getQuestionHistory(), interview.get(0).getAnswerHistory(), questionMap, answerMap, linkId, isSubModule, isExpanded);


    //Create a new ModuleVO
    ModuleVO moduleVo = new ModuleVO();

    if (isExpanded) {
      mapExpandedQuestionNodes(childNodes, moduleVo, questionMap, answerMap, isSubModule);
    } else {
      mapQuestionNodes(childNodes, moduleVo, questionMap, answerMap, isSubModule);
    }

    return moduleVo;
  }

  private void mapExpandedQuestionNodes(List<QuestionVO> nodes,
                                        ModuleVO moduleVo,
                                        Map<Long, InterviewQuestion> questionMap,
                                        Map<Long, InterviewAnswer> answerMap, boolean isSubModule) {

    List<QuestionVO> newNodes = new ArrayList<QuestionVO>();

    for (QuestionVO vo : nodes) {

      if (questionMap.containsKey(Long.valueOf(vo.getIdNode()))) {

        if (vo.getChildNodes() != null) {
          mapExpandedAnswerNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
        }
        newNodes.add(vo);
      }
    }

    moduleVo.setChildNodes(newNodes);
  }

  private void mapExpandedAnswerNodes(List<PossibleAnswerVO> nodes,
                                      ModuleVO moduleVo,
                                      Map<Long, InterviewQuestion> questionMap,
                                      Map<Long, InterviewAnswer> answerMap,
                                      QuestionVO newQuestionVO,
                                      boolean isSubModule) {

    List<PossibleAnswerVO> newNodes = new ArrayList<PossibleAnswerVO>();

    for (PossibleAnswerVO vo : nodes) {
      if (vo.getIdNode() == 0l && vo.getChildNodes() != null) {
        mapExpandedQuestionNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
        vo.setName("Ignore");
        newNodes.add(vo);
      } else if (answerMap.containsKey(Long.valueOf(vo.getIdNode()))) {

        if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
          mapExpandedQuestionNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
        }

        vo.setName(answerMap.get(Long.valueOf(vo.getIdNode())).getAnswerFreetext());
        vo.setDeleted(answerMap.get(Long.valueOf(vo.getIdNode())).getDeleted());
        newNodes.add(vo);
      }
    }
    newQuestionVO.setChildNodes(newNodes);

  }

  private void mapExpandedQuestionNodes(List<QuestionVO> nodes, ModuleVO moduleVo,
                                        Map<Long, InterviewQuestion> questionMap, Map<Long, InterviewAnswer> answerMap,
                                        PossibleAnswerVO newPossibleAnswerVo,
                                        boolean isSubModule) {

    List<QuestionVO> newNodes = new ArrayList<QuestionVO>();
    PossibleAnswerVO element = new PossibleAnswerVO();
    List<PossibleAnswerVO> pChildNodes = new ArrayList<PossibleAnswerVO>();

    for (QuestionVO vo : nodes) {

      if (vo.getLink() != 0) {
        if (questionMap.containsKey(Long.valueOf(vo.getLink())) || answerMap.containsKey(Long.valueOf(vo.getLink()))) {
          element = new PossibleAnswerVO();
          pChildNodes.clear();

          if ("Q_linkedmodule".equals(vo.getType())) {

            List<ModuleVO> list = moduleService.findById(vo.getLink());
            if (!list.isEmpty() && list.get(0) != null) {
              element.setChildNodes(list.get(0).getChildNodes());
              pChildNodes.add(element);
              vo.setChildNodes(pChildNodes);
            }
          } else if ("Q_linkedajsm".equals(vo.getType())) {
            List<FragmentVO> list = fragmentService.findById(vo.getLink());

            if (!list.isEmpty() && list.get(0) != null) {
              element.setChildNodes(list.get(0).getChildNodes());
              pChildNodes.add(element);
              vo.setChildNodes(pChildNodes);
            }
          }

          if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
            mapExpandedAnswerNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
          }
          newNodes.add(vo);

        }
      } else if (questionMap.containsKey(Long.valueOf(vo.getIdNode()))) {
        if (vo.getChildNodes() != null && !vo.getChildNodes().isEmpty()) {
          mapExpandedAnswerNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
        }
        newNodes.add(vo);
      }
    }

    newPossibleAnswerVo.setChildNodes(newNodes);
  }

  /**
   * Map first level child nodes
   *
   * @param nodes
   * @param moduleVo
   * @param questionMap
   * @param answerMap
   * @param isSubModule
   */
  private void mapQuestionNodes(List<QuestionVO> nodes,
                                ModuleVO moduleVo,
                                Map<Long, InterviewQuestion> questionMap,
                                Map<Long, InterviewAnswer> answerMap, boolean isSubModule) {

    List<QuestionVO> newNodes = new ArrayList<QuestionVO>();

    for (QuestionVO vo : nodes) {

      if (vo.getLink() != 0) {
        if (questionMap.containsKey(Long.valueOf(vo.getLink()))) {

          newNodes.add(vo);
        }
      } else if (questionMap.containsKey(Long.valueOf(vo.getIdNode()))) {

        if (vo.getChildNodes() != null) {
          mapAnswerNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
        }
        newNodes.add(vo);
      }
    }

    moduleVo.setChildNodes(newNodes);
  }

  private void mapQuestionNodes(List<QuestionVO> nodes,
                                ModuleVO moduleVo,
                                Map<Long, InterviewQuestion> questionMap,
                                Map<Long, InterviewAnswer> answerMap,
                                PossibleAnswerVO newPossibleAnswerVo, boolean isSubModule) {

    List<QuestionVO> newNodes = new ArrayList<QuestionVO>();

    for (QuestionVO vo : nodes) {

      if (vo.getLink() != 0) {
        if (questionMap.containsKey(Long.valueOf(vo.getTopNodeId()))) {
          if (isSubModule && vo.getChildNodes() != null) {
            mapAnswerNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
          }
          newNodes.add(vo);
        }
      } else if (questionMap.containsKey(Long.valueOf(vo.getIdNode()))) {

        if (vo.getChildNodes() != null) {
          mapAnswerNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
        }
        newNodes.add(vo);
      }
    }

    newPossibleAnswerVo.setChildNodes(newNodes);

  }

  private void mapAnswerNodes(List<PossibleAnswerVO> nodes,
                              ModuleVO moduleVo,
                              Map<Long, InterviewQuestion> questionMap,
                              Map<Long, InterviewAnswer> answerMap,
                              QuestionVO newQuestionVO,
                              boolean isSubModule) {

    List<PossibleAnswerVO> newNodes = new ArrayList<PossibleAnswerVO>();

    for (PossibleAnswerVO vo : nodes) {

      if (answerMap.containsKey(Long.valueOf(vo.getIdNode()))) {

        if (vo.getChildNodes() != null) {
          mapQuestionNodes(vo.getChildNodes(), moduleVo, questionMap, answerMap, vo, isSubModule);
        }
        vo.setName(answerMap.get(Long.valueOf(vo.getIdNode())).getAnswerFreetext());
        newNodes.add(vo);
      }
    }
    newQuestionVO.setChildNodes(newNodes);

  }

  /**
   * Create a map of the questions and answers
   *
   * @param questionHistory
   * @param questionMap
   * @param answerMap
   * @param linkId
   * @param isSubModule
   */
  private void getMap(List<InterviewQuestion> questionHistory, List<InterviewAnswer> answerHistory,
                      Map<Long, InterviewQuestion> questionMap,
                      Map<Long, InterviewAnswer> answerMap, long linkId, boolean isSubModule, boolean isExpanded) {
    //int iSize = questionHistory.size();
    //int iCount = 0;
    for (InterviewQuestion vo : questionHistory) {
      //iCount++;
      //System.out.println(iCount+" q of "+iSize+" "+new Date());
      if (!vo.isProcessed()) {
        continue;
      }
      if (vo.getDeleted() == 1) {
        continue;
      }

      if (!isExpanded && linkId != 0) {
        if (vo.getTopNodeId() != linkId && !"Q_linkedajsm".equals(vo.getType())) {
          continue;
        }
      }

      if (vo.getQuestionId() == 0) {
        questionMap.put(Long.valueOf(vo.getTopNodeId()), vo);
      } else {
        questionMap.put(Long.valueOf(vo.getQuestionId()), vo);
      }

    }
    //List<InterviewAnswer> answerHistory = vo.getAnswers();
    //int iPACount = 0;
    //int iPASize = answerHistory.size();
    for (InterviewAnswer answer : answerHistory) {
      //iPACount++;
      //System.out.println(iPACount+" pa of "+iPASize+" ");
      //if(vo.isProcessed()){
      if (answer.getDeleted() == 0) {
        answerMap.put(Long.valueOf(answer.getAnswerId()), answer);
      }
      //}
    }
  }

  /**
   * Get nested module or fragment
   *
   * @param interviewId
   * @param linkId
   * @return
   */
  @GET
  @Path(value = "/getModuleForSubModule")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getModuleForSubModule(
    @QueryParam("id") Long interviewId,
    @QueryParam("linkId") Long linkId) {

    List<ModuleVO> list = new ArrayList<ModuleVO>();

    try {

      list = moduleService.findById(linkId);

      if (list != null) {
        if (!list.isEmpty() && list.get(0) != null) {
          //Get module
          list.add(mapToModule(list.remove(0).getChildNodes(), interviewId, true, linkId, false));
        } else {
          //Get fragment
          List<FragmentVO> fragmentVoList = fragmentService.findById(linkId);
          if (!fragmentVoList.isEmpty() && fragmentVoList.get(0) != null) {
            list.add(0, mapToModule(fragmentVoList.remove(0).getChildNodes(), interviewId, false, linkId, false));
          }
        }
      }

    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  @GET
  @Path(value = "/checkQuestionAnswered")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response checkQuestionAnswered(
    @QueryParam("idInterview") Long interviewId,
    @QueryParam("nodeId") Long nodeId) {

    boolean result = false;
    try {
      result = service.isQuestionAnswered(interviewId, nodeId);
    } catch (Throwable e) {
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(result).build();
  }

  @POST
  @Path(value = "/checkFragmentProcessed")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response checkFragmentProcessed(InterviewModuleFragmentVO vo) {

    Long id;
    try {
      id = service.checkFragmentProcessed(vo.getIdFragment(), vo.getInterviewPrimaryKey());
    } catch (Throwable e) {
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(id).build();
  }

  /**
   * Make a module object out of the interview question list
   *
   * @param interviewId
   * @return
   */
  @GET
  @Path(value = "/getExpandedModule")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getExpandedModule(@QueryParam("id") Long interviewId) {
    List<ModuleVO> list = new ArrayList<ModuleVO>();
    SystemPropertyVO vo = null;
    Long moduleId = getIntroModuleId(interviewId);

    try {

      //Will return the intro module
      list = moduleService.findByIdNoRules(moduleId);
      if (list.get(0) != null) {
        list.add(mapToModule(list.remove(0).getChildNodes(), interviewId, false, moduleId, true));
      }

    } catch (Throwable e) {

      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }

  private Long getIntroModuleId(Long interviewId) {
    return service.getIntroModuleId(interviewId);
  }

  @GET
  @Path(value = "/getLinksByModule")
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response getLinksByModule(@QueryParam("id") Long id) {
    List<QuestionVO> list = null;
    try {
      list = service.getLinksByModule(id);
    } catch (Throwable e) {
      e.printStackTrace();
      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(list).build();
  }


  @POST
  @Path(value = "/createRandomInterviews")
  @Consumes(value = MediaType.APPLICATION_JSON_VALUE)
  @Produces(value = MediaType.APPLICATION_JSON_VALUE)
  public Response createRandomInterviews(RandomInterviewVO randomInterview) {
    List<RandomInterviewReport> results = null;
    try {
      results = service.createRandomInterviews(randomInterview.getCount(), randomInterview.isRandomAnswers(),
        randomInterview.getFilterModule());

    } catch (Throwable e) {

      return Response.status(Status.BAD_REQUEST).type("text/plain").entity(e.getMessage()).build();
    }
    return Response.ok(results).build();
  }

}