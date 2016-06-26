package org.occideas.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.occideas.base.dao.BaseDao;
import org.occideas.entity.Interview;
import org.occideas.interview.dao.InterviewDao;
import org.occideas.mapper.InterviewMapper;
import org.occideas.security.audit.Auditable;
import org.occideas.security.audit.AuditingActionType;
import org.occideas.vo.InterviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {

	@Autowired
    private BaseDao dao;
	
	@Autowired
    private InterviewDao interviewDao;

    @Autowired
    private InterviewMapper mapper;

    @Override
    public List<InterviewVO> listAll() {
        return mapper.convertToInterviewVOList(interviewDao.getAll());
    }
    @Override
    public List<InterviewVO> listAllWithAnswers() {
        return mapper.convertToInterviewVOList(interviewDao.getAll());
    }
    @Override
    public List<InterviewVO> listAssessments() {
        return mapper.convertToInterviewVOList(interviewDao.getAssessments());
    }

    @Override
    public List<InterviewVO> findById(Long id) {
        Interview interview = interviewDao.get( id);
        InterviewVO InterviewVO = mapper.convertToInterviewVO(interview);
        List<InterviewVO> list = new ArrayList<InterviewVO>();
        list.add(InterviewVO);
        return list;
    }
    @Override
    public List<InterviewVO> findByReferenceNumber(String referenceNumber) {
    	List<Interview> list = interviewDao.findByReferenceNumber(referenceNumber);
    	List<InterviewVO> listVO = mapper.convertToInterviewVOList(list);
        return listVO;
    }
    @Override
    public List<InterviewVO> findByIdWithRules(Long id) {
        Interview interview = interviewDao.get( id);
        InterviewVO InterviewVO = mapper.convertToInterviewWithRulesVO(interview);
        List<InterviewVO> list = new ArrayList<InterviewVO>();
        list.add(InterviewVO);
        return list;
    }

    @Auditable(actionType = AuditingActionType.CREATE_INTERVIEW)
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
    public void merge(InterviewVO o) {
        dao.merge(mapper.convertToInterview(o));
    }
                                                                                                     
    @Override
    public void delete(InterviewVO o) {
        dao.delete(mapper.convertToInterview(o));
    }
   
	@Override
	public List<InterviewVO> getInterview(long interviewId) {
		return mapper.convertToInterviewWithModulesVOList(interviewDao.getInterview(interviewId));
	}
	@Override
	public List<Long> getInterviewIdlist() {
		return mapper.convertToInterviewIdList(interviewDao.getInterviewIdList());
	}
}
