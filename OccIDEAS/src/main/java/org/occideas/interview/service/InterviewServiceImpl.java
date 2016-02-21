package org.occideas.interview.service;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.*;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.mapper.InterviewMapper;
import org.occideas.mapper.QuestionMapper;
import org.occideas.vo.InterviewVO;
import org.occideas.vo.InterviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Autowired
    private BaseDao dao;

    @Autowired
    private InterviewMapper mapper;

    @Override
    public List<InterviewVO> listAll() {
        return mapper.convertToInterviewVOList(dao.getAll(Interview.class));
    }

    @Override
    public List<InterviewVO> findById(Long id) {
        Interview interview = dao.get(Interview.class, id);
        InterviewVO InterviewVO = mapper.convertToInterviewVO(interview);
        List<InterviewVO> list = new ArrayList<InterviewVO>();
        list.add(InterviewVO);
        return list;
    }

    @Override
    public InterviewVO create(InterviewVO o) {
        // TODO: Hotfix - Just don't understand why it returns interviewId instead of object
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
    public void delete(InterviewVO o) {
        dao.delete(mapper.convertToInterview(o));
    }

    @Override
    public void saveAnswer(InterviewVO interviewVO) {
        if ("multiple".equals(interviewVO.getType())) {
            for (Long answerId : interviewVO.getMultipleAnswerId()) {
                InterviewQuestionAnswer iqa = new InterviewQuestionAnswer();

                PossibleAnswer answer = dao.get(PossibleAnswer.class, answerId);
                if ("P_freetext".equals(answer.getType())) {
                    iqa.setInterviewQuestionAnswerFreetext(interviewVO.getFreeText());
                }
                iqa.setInterview(new Interview(interviewVO.getInterviewId()));
                iqa.setQuestion(new Question(interviewVO.getQuestionId()));
                iqa.setAnswer(new PossibleAnswer(answerId));

                dao.save(iqa);
            }
        } else if ("single".equals(interviewVO.getType())) {
            InterviewQuestionAnswer iqa = new InterviewQuestionAnswer();
            PossibleAnswer answer = dao.get(PossibleAnswer.class, interviewVO.getSingleAnswerId());
            if ("P_freetext".equals(answer.getType())) {
                iqa.setInterviewQuestionAnswerFreetext(interviewVO.getFreeText());
            }
            iqa.setInterview(new Interview(interviewVO.getInterviewId()));
            iqa.setQuestion(new Question(interviewVO.getQuestionId()));
            iqa.setAnswer(new PossibleAnswer(interviewVO.getSingleAnswerId()));

            dao.save(iqa);
        }
    }
}
