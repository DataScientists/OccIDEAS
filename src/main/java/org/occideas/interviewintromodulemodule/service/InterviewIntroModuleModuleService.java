package org.occideas.interviewintromodulemodule.service;

import org.occideas.vo.InterviewIntroModuleModuleVO;

import java.util.List;

public interface InterviewIntroModuleModuleService {

  List<InterviewIntroModuleModuleVO> findInterviewByModuleId(long idModule);

  List<InterviewIntroModuleModuleVO> findModulesByInterviewId(long idInterview);

  List<InterviewIntroModuleModuleVO> getDistinctModules();

  List<InterviewIntroModuleModuleVO> findInterviewIdByModuleId(long idModule);

  List<InterviewIntroModuleModuleVO> findNonIntroById(Long valueOf);

}
