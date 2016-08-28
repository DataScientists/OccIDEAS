package org.occideas.interviewmodulefragment.service;

import java.util.List;

import javax.transaction.Transactional;

import org.occideas.entity.InterviewModuleFragment;
import org.occideas.interviewmodulefragment.dao.InterviewModuleFragmentDao;
import org.occideas.mapper.InterviewModuleFragmentMapper;
import org.occideas.vo.InterviewModuleFragmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class InterviewModuleFragmentServiceImpl implements InterviewModuleFragmentService{

	@Autowired
	private InterviewModuleFragmentDao dao;
	@Autowired
	private InterviewModuleFragmentMapper mapper;
	
	@Override
	public List<InterviewModuleFragmentVO> findInterviewByFragmentId(long id) {
		List<InterviewModuleFragment> list = dao.getModFragmentById(id);
		return mapper.convertToVOList(list);
	}

}
