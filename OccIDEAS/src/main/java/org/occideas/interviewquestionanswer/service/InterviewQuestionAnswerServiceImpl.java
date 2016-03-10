package org.occideas.interviewquestionanswer.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewQuestionAnswer;
import org.occideas.interviewquestionanswer.dao.InterviewQuestionAnswerDao;
import org.occideas.mapper.InterviewQuestionAnswerMapper;
import org.occideas.vo.InterviewQuestionAnswerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewQuestionAnswerServiceImpl implements InterviewQuestionAnswerService {

    @Autowired
    private InterviewQuestionAnswerDao dao;

    @Autowired
    private InterviewQuestionAnswerMapper mapper;

    @Override
    public List<InterviewQuestionAnswerVO> listAll() {
        return mapper.convertToInterviewQuestionAnswerVOList(dao.getAllActive());
    }
    
    public List<InterviewQuestionAnswerVO> listAllAssessments() {
        return mapper.convertToInterviewQuestionAnswerVOList(dao.getAllActive());
    }
    @Override
    public List<InterviewQuestionAnswerVO> findById(Long id) {
        InterviewQuestionAnswer interview = dao.get(id);
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
            intervew = dao.get((Long) obj);
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

	@Override
	public List<InterviewQuestionAnswerVO> findByInterviewId(Long id) {
		List<InterviewQuestionAnswer> modules = dao.findByInterviewId(id);
		ArrayList<InterviewQuestionAnswerVO> modulesVO = new ArrayList<InterviewQuestionAnswerVO>();
		modulesVO.addAll(mapper.convertToInterviewQuestionAnswerVOList(modules));
		return modulesVO;
	}
}
