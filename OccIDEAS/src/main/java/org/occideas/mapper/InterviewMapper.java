package org.occideas.mapper;

import org.mapstruct.Mapper;
import org.occideas.entity.Interview;
import org.occideas.vo.InterviewVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InterviewMapper {

	
	List<InterviewVO> convertToInterviewVOList(List<Interview> InterviewList, boolean includeChildNodes);

	Interview convertToInterview(InterviewVO InterviewVO);
	
	List<Interview> convertToInterviewList(List<InterviewVO> InterviewVO);

	InterviewVO convertToInterviewVO(Interview interview, boolean includeChildNodes);
	
}
