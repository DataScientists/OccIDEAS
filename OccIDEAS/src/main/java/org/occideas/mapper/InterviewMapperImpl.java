package org.occideas.mapper;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Interview;
import org.occideas.entity.Module;
import org.occideas.vo.InterviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterviewMapperImpl implements InterviewMapper {
    @Autowired
    private BaseDao dao;

    @Override
    public InterviewVO convertToInterviewVO(Interview interview) {
        if (interview == null) {
            return null;
        }

        InterviewVO interviewVO = new InterviewVO();
        interviewVO.setInterviewId(interview.getIdinterview());
        interviewVO.setModuleId(interview.getModule().getIdNode());
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
    public Interview convertToInterview(InterviewVO interviewVO) {
        if (interviewVO == null) {
            return null;
        }
        Interview interview = new Interview();
        Module module = dao.get(Module.class, interviewVO.getModuleId());
        interview.setModule(module);
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
