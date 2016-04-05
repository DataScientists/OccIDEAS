package org.occideas.mapper;

import java.util.List;

import org.occideas.entity.InterviewLinked;
import org.occideas.vo.InterviewLinkedVO;

public interface InterviewLinkMapper {


	InterviewLinkedVO convertToInterviewLinkVO(InterviewLinked entity);

	List<InterviewLinkedVO> convertToInterviewLinkVOList(List<InterviewLinked> entityList);

	InterviewLinked convertToInterviewLink(InterviewLinkedVO vo);
	
	List<InterviewLinked> convertToInterviewLinkedList(List<InterviewLinkedVO> voList);
	
}
