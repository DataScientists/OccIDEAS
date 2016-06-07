package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.Interview;
import org.occideas.vo.InterviewVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

	
	List<InterviewVO> convertToInterviewVOList(List<Interview> InterviewList);

	Interview convertToInterview(InterviewVO InterviewVO);
	
	List<Interview> convertToInterviewList(List<InterviewVO> InterviewVO);
	
	List<InterviewVO> convertToInterviewVOnoQsList(List<Interview> InterviewList);
	
	InterviewVO convertToInterviewVOnoQs(Interview interview);

	InterviewVO convertToInterviewVO(Interview interview);

	InterviewVO convertToInterviewWithRulesVO(Interview interview);

	List<InterviewVO> convertToInterviewWithRulesVOList(List<Interview> interviewEntity);
	
	InterviewVO convertToInterviewWithModulesVO(Interview interview);

	List<InterviewVO> convertToInterviewWithModulesVOList(List<Interview> interviewEntity);
	
}
