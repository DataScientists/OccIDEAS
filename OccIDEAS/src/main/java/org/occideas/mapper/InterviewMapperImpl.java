package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.Interview;
import org.occideas.entity.InterviewQuestionAnswer;
import org.occideas.vo.InterviewQuestionAnswerVO;
import org.occideas.vo.InterviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterviewMapperImpl implements InterviewMapper {
    
    @Autowired
	private ModuleMapper moduleMapper;
    
    @Autowired
	private FragmentMapper fragmentMapper;
    
    @Autowired
	private InterviewQuestionAnswerMapper iqaMapper;

    @Override
    public InterviewVO convertToInterviewVO(Interview interview,boolean includeChildNodes) {
        if (interview == null) {
            return null;
        }
        InterviewVO interviewVO = new InterviewVO();
        interviewVO.setInterviewId(interview.getIdinterview());
        interviewVO.setReferenceNumber(interview.getReferenceNumber());
        interviewVO.setModule(moduleMapper.convertToModuleVO(interview.getModule(),false));
        interviewVO.setFragment(fragmentMapper.convertToFragmentVO(interview.getFragment(),false));
        if(includeChildNodes){
        	List<InterviewQuestionAnswer> questionsAsked = interview.getInterviewQuestionAnswers();
        	interviewVO.setQuestionsAsked(iqaMapper.convertToInterviewQuestionAnswerVOList(questionsAsked));
        }      
        
        
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
        interview.setReferenceNumber(interviewVO.getReferenceNumber());
        interview.setModule(moduleMapper.convertToModule(interviewVO.getModule()));
        interview.setFragment(fragmentMapper.convertToFragment(interviewVO.getFragment()));
        List<InterviewQuestionAnswerVO> questionsAsked = interviewVO.getQuestionsAsked();
        interview.setInterviewQuestionAnswers(iqaMapper.convertToInterviewQuestionAnswerList(questionsAsked));
        
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
