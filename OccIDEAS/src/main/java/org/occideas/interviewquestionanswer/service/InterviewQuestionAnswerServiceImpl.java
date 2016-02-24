package org.occideas.interviewquestionanswer.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.InterviewQuestionAnswer;
import org.occideas.mapper.InterviewQuestionAnswerMapper;
import org.occideas.vo.InterviewQuestionAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewQuestionAnswerServiceImpl implements InterviewQuestionAnswerService {

    @Autowired
    private BaseDao dao;

    @Autowired
    private InterviewQuestionAnswerMapper mapper;

    @Override
    public List<InterviewQuestionAnswerVO> listAll() {
        return mapper.convertToInterviewQuestionAnswerVOList(dao.getAll(InterviewQuestionAnswer.class));
    }

    @Override
    public List<InterviewQuestionAnswerVO> findById(Long id) {
        InterviewQuestionAnswer interview = dao.get(InterviewQuestionAnswer.class, id);
        InterviewQuestionAnswerVO InterviewQuestionAnswerVO = mapper.convertToInterviewQuestionAnswerVO(interview);
        List<InterviewQuestionAnswerVO> list = new ArrayList<InterviewQuestionAnswerVO>();
        list.add(InterviewQuestionAnswerVO);
        return list;
    }

    @Override
    public InterviewQuestionAnswerVO create(InterviewQuestionAnswerVO o) {
        // TODO: Hotfix - Just don't understand why it returns interviewId instead of object
        Object obj = dao.save(mapper.convertToInterviewQuestionAnswer(o));
        InterviewQuestionAnswer intervew = null;
        if (obj instanceof InterviewQuestionAnswer) {
            intervew = (InterviewQuestionAnswer) obj;
        } else if (obj instanceof Long) {
            intervew = dao.get(InterviewQuestionAnswer.class, (Long) obj);
        }

        return mapper.convertToInterviewQuestionAnswerVO(intervew);
    }

    @Override
    public void update(InterviewQuestionAnswerVO o) {
        dao.saveOrUpdate(mapper.convertToInterviewQuestionAnswer(o));
    }

    @Override
    public void delete(InterviewQuestionAnswerVO o) {
        dao.delete(mapper.convertToInterviewQuestionAnswer(o));
    }
}
