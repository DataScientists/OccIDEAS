package org.occideas.interviewintromodulemodule.service;

import java.util.List;

import org.occideas.vo.InterviewIntroModuleModuleVO;

public interface InterviewIntroModuleModuleService {

	public List<InterviewIntroModuleModuleVO> findInterviewByModuleId(long idModule);
	
}
