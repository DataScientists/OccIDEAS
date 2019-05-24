package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewModule;
import org.occideas.vo.InterviewModuleVO;

public interface InterviewModuleMapper {

	List<InterviewModuleVO> convertToInterviewModuleVOList(List<InterviewModule> entity);

	InterviewModule convertToInterviewModule(InterviewModuleVO vo);
	
	List<InterviewModule> convertToInterviewModuleList(List<InterviewModuleVO> voList);

	InterviewModuleVO convertToInterviewModuleVO(InterviewModule entity);
	
}
