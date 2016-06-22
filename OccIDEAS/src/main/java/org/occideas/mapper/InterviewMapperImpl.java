package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Interview;
import org.occideas.entity.InterviewAnswer;
import org.occideas.entity.InterviewQuestion;
import org.occideas.entity.Note;
import org.occideas.entity.Rule;
import org.occideas.vo.InterviewQuestionVO;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.NoteVO;
import org.occideas.vo.RuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		//interviewVO.setFragment(fragmentMapper.convertToInterviewFragmentVO(interview.getFragment()));
		interviewVO.setModules(modMapper.convertToInterviewModuleVOList(interview.getModules()));
		interviewVO.setModule(moduleMapper.convertToInterviewModuleVO(interview.getModule()));
		//List<InterviewQuestion> questionsAsked = interview.getActualQuestion();
		//interviewVO.setActualQuestion(qsMapper.convertToInterviewQuestionVOList(questionsAsked));
		
		List<InterviewQuestion> questionHistory = interview.getQuestionHistory();
		interviewVO.setQuestionHistory(qsMapper.convertToInterviewQuestionVOList(questionHistory));

		List<InterviewAnswer> answerHistory = interview.getAnswerHistory();
		interviewVO.setAnswerHistory(asMapper.convertToInterviewAnswerVOList(answerHistory));

		//List<Rule> firedRules = interview.getFiredRules();
		//interviewVO.setFiredRules(ruleMapper.convertToRuleVOExcPaList(firedRules));
		//List<Rule> autoAssessedRules = interview.getAutoAssessedRules();
		//interviewVO.setAutoAssessedRules(ruleMapper.convertToRuleVOExcPaList(autoAssessedRules));
		//List<Rule> manualAssessedRules = interview.getManualAssessedRules();
		//interviewVO.setManualAssessedRules(ruleMapper.convertToRuleVOExcPaList(manualAssessedRules));

		//interviewVO.setParticipant(participantMapper.convertToParticipantVO(interview.getParticipant(), false));
		interviewVO.setParentId(interview.getParentId());
		List<Interview> childInterviews = interview.getInterviews();
		interviewVO.setInterviews(this.convertToInterviewVOList(childInterviews));
		List<Note> notes = interview.getNotes();
		interviewVO.setNotes(noteMapper.convertToNoteVOList(notes));

		return interviewVO;
	}

	@Override
	public InterviewVO convertToInterviewWithRulesVO(Interview interview) {
		if (interview == null) {
			return null;
		}
		InterviewVO interviewVO = new InterviewVO();
		interviewVO.setInterviewId(interview.getIdinterview());
		interviewVO.setReferenceNumber(interview.getReferenceNumber());
		interviewVO.setModule(moduleMapper.convertToInterviewModuleVO(interview.getModule()));
		interviewVO.setFragment(fragmentMapper.convertToInterviewFragmentVO(interview.getFragment()));
		List<InterviewQuestion> questionsAsked = interview.getActualQuestion();
		interviewVO.setActualQuestion(qsMapper.convertToInterviewQuestionWithRulesVOList(questionsAsked));

		List<Rule> firedRules = interview.getFiredRules();
		interviewVO.setFiredRules(ruleMapper.convertToRuleVOExcPaList(firedRules));
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
		for (Interview interview : interviewEntity) {
			list.add(convertToInterviewWithRulesVO(interview));
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
		interviewVO.setFragment(fragmentMapper.convertToInterviewFragmentVO(interview.getFragment()));
		interviewVO.setModules(modMapper.convertToInterviewModuleVOList(interview.getModules()));
		List<InterviewQuestion> questionsAsked = interview.getActualQuestion();
		interviewVO.setActualQuestion(qsMapper.convertToInterviewQuestionVOList(questionsAsked));
		interviewVO.setParticipant(participantMapper.convertToParticipantVO(interview.getParticipant(), false));
		interviewVO.setParentId(interview.getParentId());
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
//		interviewVO.setModule(moduleMapper.convertToInterviewModuleVO(interview.getModule()));
//		interviewVO.setFragment(fragmentMapper.convertToInterviewFragmentVO(interview.getFragment()));
//		interviewVO.setModules(modMapper.convertToInterviewModuleVOList(interview.getModules()));
		interviewVO.setParentId(interview.getParentId());
		List<Note> notes = interview.getNotes();
		interviewVO.setNotes(noteMapper.convertToNoteVOList(notes));
		return interviewVO;
	}
}
