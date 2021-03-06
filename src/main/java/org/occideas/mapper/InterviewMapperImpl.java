package org.occideas.mapper;

import org.occideas.entity.Interview;
import org.occideas.entity.InterviewQuestion;
import org.occideas.entity.Note;
import org.occideas.entity.Rule;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.NoteVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class InterviewMapperImpl implements InterviewMapper {

  @Autowired
  private ModuleMapper moduleMapper;

  @Autowired
  private FragmentMapper fragmentMapper;

  @Autowired
  private RuleMapper ruleMapper;

  @Autowired
  private NoteMapper noteMapper;

  @Autowired
  private ParticipantMapper participantMapper;

  @Autowired
  private InterviewModuleMapper modMapper;

  @Autowired
  private InterviewQuestionMapper qsMapper;

  @Autowired
  private InterviewAnswerMapper asMapper;

  @Override
  public InterviewVO convertToInterviewVO(Interview interview) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());
    interviewVO.setAssessedStatus(interview.getAssessedStatus());
    interviewVO.setQuestionHistory(qsMapper.convertToInterviewQuestionVOList(interview.getQuestionHistory()));

    interviewVO.setAnswerHistory(asMapper.convertToInterviewAnswerVOList(interview.getAnswerHistory()));

    interviewVO.setParentId(interview.getParentId());

    interviewVO.setNotes(noteMapper.convertToNoteVOList(interview.getNotes()));
    interviewVO.setAutoAssessedRules(ruleMapper.convertToRuleVOList(interview.getAutoAssessedRules()));
    interviewVO.setManualAssessedRules(ruleMapper.convertToRuleVOList(interview.getManualAssessedRules()));
    interviewVO.setFiredRules(ruleMapper.convertToRuleVOList(interview.getFiredRules()));
    return interviewVO;
  }


  @Override
  public InterviewVO convertToInterviewWithRulesVO(Interview interview, boolean isIncludeAnswer) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());
    interviewVO.setAssessedStatus(interview.getAssessedStatus());

    System.out.println("1.1:convertToInterviewWithRulesVO:" + new Date());

    interviewVO.setQuestionHistory(qsMapper.convertToInterviewQuestionVOList(interview.getQuestionHistory(), isIncludeAnswer));
    System.out.println("1.2:convertToInterviewWithRulesVO:" + new Date());

    interviewVO.setAnswerHistory(asMapper.convertToInterviewAnswerWithRulesVOList(interview.getAnswerHistory()));
    System.out.println("1.3:convertToInterviewWithRulesVO:" + new Date());

    interviewVO.setFiredRules(ruleMapper.convertToRuleVOExcPaList(interview.getFiredRules()));

    interviewVO.setAutoAssessedRules(ruleMapper.convertToRuleVOExcPaList(interview.getAutoAssessedRules()));

    interviewVO.setManualAssessedRules(ruleMapper.convertToRuleVOExcPaList(interview.getManualAssessedRules()));
    System.out.println("1.4:convertToInterviewWithRulesVO:" + new Date());
    interviewVO.setParticipant(participantMapper.convertToParticipantVO(interview.getParticipant(), false));
    interviewVO.setParentId(interview.getParentId());

    interviewVO.setNotes(noteMapper.convertToNoteVOList(interview.getNotes()));

    return interviewVO;
  }

  @Override
  public InterviewVO convertToInterviewWithRulesNoAnswersVO(Interview interview) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());
    interviewVO.setModule(moduleMapper.convertToInterviewModuleVO(interview.getModule()));
    interviewVO.setModuleList(moduleMapper.convertToInterviewModuleListVO(interview.getModuleList()));
    interviewVO.setFragment(fragmentMapper.convertToInterviewFragmentVO(interview.getFragment()));

    interviewVO.setQuestionHistory(qsMapper.convertToInterviewQuestionNoAnswersVOList(interview.getQuestionHistory()));

    interviewVO.setAnswerHistory(asMapper.convertToInterviewAnswerWithRulesVOList(interview.getAnswerHistory()));

    interviewVO.setFiredRules(ruleMapper.convertToRuleVOExcPaList(interview.getFiredRules()));

    interviewVO.setAutoAssessedRules(ruleMapper.convertToRuleVOExcPaList(interview.getAutoAssessedRules()));
    interviewVO.setManualAssessedRules(ruleMapper.convertToRuleVOExcPaList(interview.getManualAssessedRules()));

    interviewVO.setParticipant(participantMapper.convertToParticipantVO(interview.getParticipant(), false));
    interviewVO.setParentId(interview.getParentId());
    interviewVO.setNotes(noteMapper.convertToNoteVOList(interview.getNotes()));
    interviewVO.setAssessedStatus(interview.getAssessedStatus());

    return interviewVO;
  }

  @Override
  public InterviewVO convertToInterviewWithAssessmentsVO(Interview interview) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());
    interviewVO.setModule(moduleMapper.convertToInterviewModuleVO(interview.getModule()));
    interviewVO.setModuleList(moduleMapper.convertToInterviewModuleListVO(interview.getModuleList()));
    interviewVO.setFragment(fragmentMapper.convertToInterviewFragmentVO(interview.getFragment()));

    List<Rule> autoAssessedRules = interview.getAutoAssessedRules();
    interviewVO.setAutoAssessedRules(ruleMapper.convertToRuleVOExcPaList(autoAssessedRules));
    List<Rule> manualAssessedRules = interview.getManualAssessedRules();
    interviewVO.setManualAssessedRules(ruleMapper.convertToRuleVOExcPaList(manualAssessedRules));

    interviewVO.setParticipant(participantMapper.convertToParticipantVO(interview.getParticipant(), false));
    interviewVO.setParentId(interview.getParentId());
    List<Note> notes = interview.getNotes();
    interviewVO.setNotes(noteMapper.convertToNoteVOList(notes));

    return interviewVO;
  }

  @Override
  public List<InterviewVO> convertToInterviewVOList(List<Interview> interviewEntity) {
    if (interviewEntity == null) {
      return null;
    }
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    for (Interview interview : interviewEntity) {
      list.add(convertToInterviewVO(interview));
    }
    return list;
  }

  @Override
  public List<InterviewVO> convertToInterviewWithRulesVOList(List<Interview> interviewEntity) {
    if (interviewEntity == null) {
      return null;
    }
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    int iCount = 0;
    int iSize = interviewEntity.size();
    for (Interview interview : interviewEntity) {
      list.add(convertToInterviewWithRulesVO(interview, true));
      System.out.println((iCount++) + " of " + iSize + " convertToInterviewWithRulesVOList:" + new Date());
    }
    return list;
  }

  @Override
  public List<InterviewVO> convertToInterviewWithRulesNoAnswersVOList(List<Interview> interviewEntity) {
    if (interviewEntity == null) {
      return null;
    }
    List<InterviewVO> list = new ArrayList<InterviewVO>();

    for (Interview interview : interviewEntity) {
      list.add(convertToInterviewWithRulesNoAnswersVO(interview));
    }
    return list;
  }

  @Override
  public List<InterviewVO> convertToInterviewWithAssessmentsVOList(List<Interview> interviewEntity) {
    if (interviewEntity == null) {
      return null;
    }
    List<InterviewVO> list = new ArrayList<InterviewVO>();
    int iCount = 0;
    for (Interview interview : interviewEntity) {
      list.add(convertToInterviewWithAssessmentsVO(interview));
      System.out.println((iCount++) + "convertToInterviewWithAssessmentsVOList:" + new Date());
    }
    return list;
  }

  @Override
  public Interview convertToInterview(InterviewVO interviewVO) {
    if (interviewVO == null) {
      return null;
    }
    Interview interview = new Interview();

    interview.setIdinterview(interviewVO.getInterviewId());
    interview.setReferenceNumber(interviewVO.getReferenceNumber());
    interview.setAssessedStatus(interviewVO.getAssessedStatus());
    interview.setModule(moduleMapper.convertToModule(interviewVO.getModule(), true));
    interview.setFragment(fragmentMapper.convertToFragment(interviewVO.getFragment(), true));
    List<InterviewQuestionVO> questionsAsked = interviewVO.getActualQuestion();
    interview.setActualQuestion(qsMapper.convertToInterviewQuestionList(questionsAsked));
    List<RuleVO> firedRules = interviewVO.getFiredRules();
    interview.setFiredRules(ruleMapper.convertToRuleExcPaList(firedRules));
    List<RuleVO> autoAssessedRules = interviewVO.getAutoAssessedRules();
    interview.setAutoAssessedRules(ruleMapper.convertToRuleExcPaList(autoAssessedRules));
    List<RuleVO> manualAssessedRules = interviewVO.getManualAssessedRules();
    interview.setManualAssessedRules(ruleMapper.convertToRuleExcPaList(manualAssessedRules));
    interview.setParticipant(participantMapper.convertToParticipant(interviewVO.getParticipant(), false));
    interview.setParentId(interviewVO.getParentId());
    List<NoteVO> notes = interviewVO.getNotes();
    interview.setNotes(noteMapper.convertToNoteList(notes));
    return interview;
  }

  @Override
  public List<Interview> convertToInterviewList(List<InterviewVO> interviewVO) {
    if (interviewVO == null) {
      return null;
    }

    List<Interview> list = new ArrayList<Interview>();
    for (InterviewVO interviewVO_ : interviewVO) {
      list.add(convertToInterview(interviewVO_));
    }

    return list;
  }

  @Override
  public InterviewVO convertToInterviewWithModulesVO(Interview interview) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());
    interviewVO.setModule(moduleMapper.convertToInterviewModuleVO(interview.getModule()));
    interviewVO.setModuleList(moduleMapper.convertToInterviewModuleListVO(interview.getModuleList()));
    interviewVO.setFragment(fragmentMapper.convertToInterviewFragmentVO(interview.getFragment()));
    interviewVO.setModules(modMapper.convertToInterviewModuleVOList(interview.getModules()));
    List<InterviewQuestion> questionsAsked = interview.getActualQuestion();
    interviewVO.setActualQuestion(qsMapper.convertToInterviewQuestionVOList(questionsAsked));
    interviewVO.setParticipant(participantMapper.convertToParticipantVO(interview.getParticipant(), false));
    interviewVO.setParentId(interview.getParentId());
    List<Note> notes = interview.getNotes();
    interviewVO.setNotes(noteMapper.convertToNoteVOList(notes));
    interviewVO.setAssessedStatus(interview.getAssessedStatus());

    return interviewVO;
  }

  @Override
  public List<InterviewVO> convertToInterviewWithModulesVOList(List<Interview> interviewEntity) {
    if (interviewEntity == null) {
      return null;
    }

    List<InterviewVO> list = new ArrayList<>();
    for (Interview interview : interviewEntity) {
      list.add(convertToInterviewWithModulesVO(interview));
    }

    return list;
  }

  @Override
  public List<InterviewVO> convertToInterviewVOnoQsList(List<Interview> voList) {
    if (voList == null) {
      return null;
    }

    List<InterviewVO> list = new ArrayList<>();
    for (Interview interview : voList) {
      list.add(convertToInterviewVOnoQs(interview));
    }

    return list;
  }

  @Override
  public InterviewVO convertToInterviewVOnoQs(Interview interview) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());

    interviewVO.setParentId(interview.getParentId());
    List<Note> notes = interview.getNotes();
    interviewVO.setNotes(noteMapper.convertToNoteVOList(notes));
    return interviewVO;
  }

  @Override
  public List<Long> convertToInterviewIdList(List<Interview> interviewIdList) {
    List<Long> list = new ArrayList<>();
    for (Interview entity : interviewIdList) {
      list.add(entity.getIdinterview());
    }
    return list;
  }

  @Override
  public InterviewVO convertToInterviewWithoutAnswers(Interview interview) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setNotes(noteMapper.convertToNoteVOList(interview.getNotes()));
    interviewVO.setAssessedStatus(interview.getAssessedStatus());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());
    interviewVO.setParentId(interview.getParentId());
    interviewVO.setQuestionHistory(qsMapper.convertToInterviewQuestionWithoutAnswersList(interview.getQuestionHistory()));

    return interviewVO;
  }

  @Override
  public List<InterviewVO> convertToInterviewWithoutAnswersList(List<Interview> interviewEntity) {
    if (interviewEntity == null) {
      return null;
    }

    List<InterviewVO> list = new ArrayList<>();
    for (Interview interview : interviewEntity) {
      list.add(convertToInterviewWithoutAnswers(interview));
    }

    return list;
  }

  @Override
  public InterviewVO convertToInterviewWithQuestionAnswer(Interview interview) {
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());
    List<InterviewQuestion> questionsAsked = interview.getQuestionHistory();
    interviewVO.setQuestionHistory(qsMapper.convertToInterviewQuestionVOList(questionsAsked));
    interviewVO.setParentId(interview.getParentId());
    interviewVO.setParticipant(participantMapper.convertToParticipantVO(interview.getParticipant(), false));

    return interviewVO;
  }

  @Override
  public List<InterviewVO> convertToInterviewWithQuestionAnswerList(List<Interview> interviewEntity) {
    if (interviewEntity == null) {
      return null;
    }

    List<InterviewVO> list = new ArrayList<>();
    for (Interview interview : interviewEntity) {
      list.add(convertToInterviewWithQuestionAnswer(interview));
    }

    return list;
  }

  @Override
  public InterviewVO convertToInterviewUnprocessQuestion(Interview interview) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());

    List<InterviewQuestion> questionQueueUnprocessed = interview.getQuestionQueueUnprocessed();
    interviewVO.setQuestionQueueUnprocessed(qsMapper.convertToInterviewQuestionUnprocessedVOList(questionQueueUnprocessed));

    return interviewVO;
  }

  @Override
  public InterviewVO convertToInterviewVOWithFiredRules(Interview interview) {
    if (interview == null) {
      return null;
    }
    InterviewVO interviewVO = new InterviewVO();
    interviewVO.setInterviewId(interview.getIdinterview());
    interviewVO.setReferenceNumber(interview.getReferenceNumber());
    List<Rule> firedRules = interview.getFiredRules();
    interviewVO.setFiredRules(ruleMapper.convertToRuleVOExcPaList(firedRules));
    return interviewVO;
  }
}
