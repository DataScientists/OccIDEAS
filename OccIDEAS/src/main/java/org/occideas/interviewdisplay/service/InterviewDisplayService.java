package org.occideas.interviewdisplay.service;

import java.util.List;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewDisplayVO;

public interface InterviewDisplayService extends BaseService<InterviewDisplayVO>{

	List<InterviewDisplayVO> updateList(List<InterviewDisplayVO> json);
	
}
