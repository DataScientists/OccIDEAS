package org.occideas.mapper;

import org.occideas.entity.InterviewModule;
import org.occideas.vo.InterviewModuleVO;

import java.util.List;

public interface InterviewModuleMapper {

  List<InterviewModuleVO> convertToInterviewModuleVOList(List<InterviewModule> entity);

  InterviewModule convertToInterviewModule(InterviewModuleVO vo);

  List<InterviewModule> convertToInterviewModuleList(List<InterviewModuleVO> voList);

  InterviewModuleVO convertToInterviewModuleVO(InterviewModule entity);

}
