package org.occideas.interviewdisplay.service;

import org.occideas.base.service.BaseService;
import org.occideas.vo.InterviewDisplayAnswerVO;
import org.occideas.vo.InterviewDisplayVO;

import java.util.List;

public interface InterviewDisplayService extends BaseService<InterviewDisplayVO> {

  List<InterviewDisplayVO> updateList(List<InterviewDisplayVO> json);

  List<InterviewDisplayAnswerVO> updateDisplayAnswerList(List<InterviewDisplayAnswerVO> list);
}
