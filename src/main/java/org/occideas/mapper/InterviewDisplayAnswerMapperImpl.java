package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewDisplayAnswer;
import org.occideas.vo.InterviewDisplayAnswerVO;
import org.springframework.stereotype.Component;

@Component
public class InterviewDisplayAnswerMapperImpl implements InterviewDisplayAnswerMapper{

	@Override
	public InterviewDisplayAnswerVO convertToInterviewDisplayAnswerVO(InterviewDisplayAnswer entity) {
		if(entity == null){
			return null;
		}
		InterviewDisplayAnswerVO vo = new InterviewDisplayAnswerVO();
		vo.setAnswerFreetext(entity.getAnswerFreetext());
		vo.setAnswerId(entity.getAnswerId());
		vo.setDeleted(entity.getDeleted());
		vo.setId(entity.getId());
		vo.setInterviewDisplayId(entity.getInterviewDisplayId());
		vo.setLastUpdated(entity.getLastUpdated());
		vo.setName(entity.getName());
		vo.setNodeClass(entity.getNodeClass());
		vo.setNumber(entity.getNumber());
		vo.setType(entity.getType());
		return vo;
	}

	@Override
	public InterviewDisplayAnswer convertToInterviewDisplayAnswer(InterviewDisplayAnswerVO vo) {
		if(vo == null){
			return null;
		}
		InterviewDisplayAnswer entity = new InterviewDisplayAnswer();
		entity.setAnswerFreetext(vo.getAnswerFreetext());
		entity.setAnswerId(vo.getAnswerId());
		entity.setDeleted(vo.getDeleted());
		entity.setId(vo.getId());
		entity.setInterviewDisplayId(vo.getInterviewDisplayId());
		entity.setLastUpdated(vo.getLastUpdated());
		entity.setName(vo.getName());
		entity.setNodeClass(vo.getNodeClass());
		entity.setNumber(vo.getNumber());
		entity.setType(vo.getType());
		return entity;
	}

	@Override
	public List<InterviewDisplayAnswerVO> convertToInterviewDisplayAnswerVOList(List<InterviewDisplayAnswer> entity) {
		if (entity == null) {
			return null;
		}
		
		List<InterviewDisplayAnswerVO> list = new ArrayList<>();
		for(InterviewDisplayAnswer ans:entity){
			list.add(convertToInterviewDisplayAnswerVO(ans));
		}
		return list;
	}

	@Override
	public List<InterviewDisplayAnswer> convertToInterviewDisplayAnswerList(List<InterviewDisplayAnswerVO> vo) {
		if (vo == null) {
			return null;
		}
		
		List<InterviewDisplayAnswer> list = new ArrayList<>();
		for(InterviewDisplayAnswerVO ans:vo){
			list.add(convertToInterviewDisplayAnswer(ans));
		}
		return list;
	}

}
