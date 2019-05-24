package org.occideas.interviewmodulefragment.service;

import org.occideas.vo.InterviewModuleFragmentVO;

import java.util.List;

public interface InterviewModuleFragmentService {

  List<InterviewModuleFragmentVO> findInterviewByFragmentId(long id);

  List<InterviewModuleFragmentVO> findFragmentByInterviewId(long id);

}
