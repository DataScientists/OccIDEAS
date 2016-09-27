package org.occideas.interviewintromodulemodule.service;

import java.util.List;

import org.occideas.vo.InterviewIntroModuleModuleVO;

public interface InterviewIntroModuleModuleService {

	public List<InterviewIntroModuleModuleVO> findInterviewByModuleId(long idModule);
	public List<InterviewIntroModuleModuleVO> findModulesByInterviewId(long idInterview);
	public List<InterviewIntroModuleModuleVO> getDistinctModules();
	public List<InterviewIntroModuleModuleVO> findInterviewIdByModuleId(long idModule);
	
}
