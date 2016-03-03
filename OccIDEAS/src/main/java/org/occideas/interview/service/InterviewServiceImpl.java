package org.occideas.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Interview;
import org.occideas.mapper.InterviewMapper;
import org.occideas.vo.InterviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Autowired
    private BaseDao dao;

    @Autowired
    private InterviewMapper mapper;

    @Override
    public List<InterviewVO> listAll() {
        return mapper.convertToInterviewVOList(dao.getAll(Interview.class),true);
    }

    @Override
    public List<InterviewVO> findById(Long id) {
        Interview interview = dao.get(Interview.class, id);
        InterviewVO InterviewVO = mapper.convertToInterviewVO(interview,true);
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

        return mapper.convertToInterviewVO(intervew,true);
    }

    @Override
    public void update(InterviewVO o) {
        dao.saveOrUpdate(mapper.convertToInterview(o));
    }
    @Override
    public void merge(InterviewVO o) {
        dao.merge(mapper.convertToInterview(o));
    }

    @Override
    public void delete(InterviewVO o) {
        dao.delete(mapper.convertToInterview(o));
    }
}
