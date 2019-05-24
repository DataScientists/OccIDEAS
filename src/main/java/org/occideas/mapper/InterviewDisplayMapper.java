package org.occideas.mapper;

import org.occideas.entity.InterviewDisplay;
import org.occideas.vo.InterviewDisplayVO;

import java.util.List;

public interface InterviewDisplayMapper {

  InterviewDisplayVO convertToInterviewDisplayVO(InterviewDisplay entity);

  List<InterviewDisplayVO> convertToInterviewDisplayVOList(List<InterviewDisplay> entityList);

  InterviewDisplay convertToInterviewDisplay(InterviewDisplayVO vo);

  List<InterviewDisplay> convertToInterviewDisplayList(List<InterviewDisplayVO> voList);

}
