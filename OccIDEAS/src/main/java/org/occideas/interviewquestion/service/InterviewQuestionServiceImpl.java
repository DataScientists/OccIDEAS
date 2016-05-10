package org.occideas.interviewquestion.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewQuestion;
import org.occideas.interviewquestion.dao.InterviewQuestionDao;
import org.occideas.mapper.InterviewQuestionMapper;
import org.occideas.vo.InterviewQuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewQuestionServiceImpl implements InterviewQuestionService {

    @Autowired
    private InterviewQuestionDao dao;

    @Autowired
    private InterviewQuestionMapper mapper;

    @Override
    public List<InterviewQuestionVO> listAll() {
        return mapper.convertToInterviewQuestionVOList(dao.getAllActive());
    }
    
    public List<InterviewQuestionVO> listAllAssessments() {
        return mapper.convertToInterviewQuestionVOList(dao.getAllActive());
    }
    @Override
    public List<InterviewQuestionVO> findById(Long id) {
        InterviewQuestion interview = dao.get(id);
        InterviewQuestionVO InterviewQuestionVO = mapper.convertToInterviewQuestionVO(interview);
        List<InterviewQuestionVO> list = new ArrayList<InterviewQuestionVO>();
        list.add(InterviewQuestionVO);
        return list;
    }

    @Override
    public InterviewQuestionVO create(InterviewQuestionVO o) {
        // TODO: Hotfix - Just don't understand why it returns interviewId instead of object
        Object obj = dao.save(mapper.convertToInterviewQuestion(o));
        InterviewQuestion intervew = null;
        if (obj instanceof InterviewQuestion) {
            intervew = (InterviewQuestion) obj;
        } else if (obj instanceof Long) {
            intervew = dao.get((Long) obj);
        }

        return mapper.convertToInterviewQuestionVO(intervew);
    }

    @Override
    public InterviewQuestionVO updateIntQ(InterviewQuestionVO o) {
        return mapper.convertToInterviewQuestionVO(dao.saveOrUpdate(mapper.convertToInterviewQuestion(o)));
    }

    @Override
    public void delete(InterviewQuestionVO o) {
        dao.delete(mapper.convertToInterviewQuestion(o));
    }

	@Override
	public List<InterviewQuestionVO> findByInterviewId(Long id) {
		List<InterviewQuestion> modules = dao.findByInterviewId(id);
		ArrayList<InterviewQuestionVO> modulesVO = new ArrayList<InterviewQuestionVO>();
		modulesVO.addAll(mapper.convertToInterviewQuestionVOList(modules));
		return modulesVO;
	}

	@Override
	public void update(InterviewQuestionVO o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InterviewQuestionVO findIntQuestion(long idInterview, long questionId, Integer modCount) {
		return mapper.convertToInterviewQuestionVO(dao.findIntQuestion(idInterview,questionId,modCount));
	}
}
