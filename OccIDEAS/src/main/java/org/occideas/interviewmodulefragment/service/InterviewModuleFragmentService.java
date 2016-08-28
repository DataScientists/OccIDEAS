package org.occideas.interviewmodulefragment.service;

import java.util.List;

import org.occideas.vo.InterviewModuleFragmentVO;

public interface InterviewModuleFragmentService {

	public List<InterviewModuleFragmentVO> findInterviewByFragmentId(long id);
	
}
