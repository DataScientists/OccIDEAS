package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Interview;
import org.occideas.entity.InterviewQuestionAnswer;
import org.occideas.entity.Rule;
import org.occideas.vo.InterviewQuestionAnswerVO;
import org.occideas.vo.InterviewVO;
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
	private InterviewQuestionAnswerMapper iqaMapper;

    @Override
    public InterviewVO convertToInterviewVO(Interview interview,boolean includeChildNodes) {
        if (interview == null) {
            return null;
        }
        InterviewVO interviewVO = new InterviewVO();
        interviewVO.setInterviewId(interview.getIdinterview());
        interviewVO.setModule(moduleMapper.convertToModuleVO(interview.getModule(),true));
//        interviewVO.setFragment(fragmentMapper.convertToFragmentVO(interview.getFragment(),true));
        if(includeChildNodes){
        	List<InterviewQuestionAnswer> questionsAsked = interview.getInterviewQuestionAnswers();
        	interviewVO.setQuestionsAsked(iqaMapper.convertToInterviewQuestionAnswerVOList(questionsAsked));
        }      
        List<Rule> firedRules = interview.getFiredRules();
        interviewVO.setFiredRules(ruleMapper.convertToRuleVOExcPaList(firedRules));
        return interviewVO;
    }

    @Override
    public List<InterviewVO> convertToInterviewVOList(List<Interview> interviewEntity,boolean includeChildNodes) {
        if (interviewEntity == null) {
            return null;
        }
        List<InterviewVO> list = new ArrayList<InterviewVO>();
        for (Interview interview : interviewEntity) {
            list.add(convertToInterviewVO(interview,includeChildNodes));
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
        interview.setModule(moduleMapper.convertToModule(interviewVO.getModule(),true));
//        interview.setFragment(fragmentMapper.convertToFragment(interviewVO.getFragment(),true));
        List<InterviewQuestionAnswerVO> questionsAsked = interviewVO.getQuestionsAsked();
        interview.setInterviewQuestionAnswers(iqaMapper.convertToInterviewQuestionAnswerList(questionsAsked));
        List<RuleVO> firedRules = interviewVO.getFiredRules();
        interview.setFiredRules(ruleMapper.convertToRuleExcPaList(firedRules));
        
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
}
