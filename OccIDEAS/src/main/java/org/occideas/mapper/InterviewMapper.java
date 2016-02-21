package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.Interview;
import org.occideas.vo.InterviewVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

	InterviewVO convertToInterviewVO(Interview Interview);

	List<InterviewVO> convertToInterviewVOList(List<Interview> InterviewList);

	Interview convertToInterview(InterviewVO InterviewVO);
	
	List<Interview> convertToInterviewList(List<InterviewVO> InterviewVO);
	
}
