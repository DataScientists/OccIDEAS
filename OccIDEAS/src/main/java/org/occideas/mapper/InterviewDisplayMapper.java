package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewDisplay;
import org.occideas.vo.InterviewDisplayVO;

public interface InterviewDisplayMapper {

	InterviewDisplayVO convertToInterviewDisplayVO(InterviewDisplay entity);

	List<InterviewDisplayVO> convertToInterviewDisplayVOList(List<InterviewDisplay> entityList);

	InterviewDisplay convertToInterviewDisplay(InterviewDisplayVO vo);
	
	List<InterviewDisplay> convertToInterviewDisplayList(List<InterviewDisplayVO> voList);
	
}
