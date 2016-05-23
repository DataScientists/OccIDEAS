package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewDisplay;
import org.occideas.vo.InterviewDisplayVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterviewDisplayMapperImpl implements InterviewDisplayMapper{

	@Autowired
	private InterviewAnswerMapper mapper;
	
	@Override
	public InterviewDisplayVO convertToInterviewDisplayVO(InterviewDisplay entity) {
		if (entity == null) {
			return null;
		}
		InterviewDisplayVO vo = new InterviewDisplayVO();
		vo.setId(entity.getId());
		vo.setCount(entity.getCount());
		vo.setDeleted(entity.getDeleted());
		vo.setIdInterview(entity.getIdInterview());
		vo.setIdNode(entity.getIdNode());
		vo.setLastUpdated(entity.getLastUpdated());
		vo.setName(entity.getName());
		vo.setNumber(entity.getNumber());
		vo.setQuestionId(entity.getQuestionId());
		vo.setType(entity.getType());
		vo.setSequence(entity.getSequence());
		if(entity.getQuestionId() != 0L){
			vo.setAnswers(mapper.convertToInterviewAnswerVOList(entity.getAnswers()));
		}
		return vo;
	}

	@Override
	public List<InterviewDisplayVO> convertToInterviewDisplayVOList(List<InterviewDisplay> entityList) {
		if (entityList == null) {
			return null;
		}

		List<InterviewDisplayVO> list = new ArrayList<>();
		for (InterviewDisplay disp : entityList) {
			list.add(convertToInterviewDisplayVO(disp));
		}

		return list;
	}

	@Override
	public InterviewDisplay convertToInterviewDisplay(InterviewDisplayVO vo) {
		if (vo == null) {
			return null;
		}
				
		InterviewDisplay entity = new InterviewDisplay();
		entity.setCount(vo.getCount());
		entity.setDeleted(vo.getDeleted());
		entity.setId(vo.getId());
		entity.setIdInterview(vo.getIdInterview());
		entity.setIdNode(vo.getIdNode());
		entity.setLastUpdated(vo.getLastUpdated());
		entity.setName(vo.getName());
		entity.setNumber(vo.getNumber());
		entity.setQuestionId(vo.getQuestionId());
		entity.setType(vo.getType());
		entity.setSequence(vo.getSequence());
		if(vo.getQuestionId() != 0L){
			entity.setAnswers(mapper.convertToInterviewAnswerList(vo.getAnswers()));
		}
		return entity;
	}

	@Override
	public List<InterviewDisplay> convertToInterviewDisplayList(List<InterviewDisplayVO> voList) {
		if (voList == null) {
			return null;
		}

		List<InterviewDisplay> list = new ArrayList<>();
		for (InterviewDisplayVO disp : voList) {
			list.add(convertToInterviewDisplay(disp));
		}

		return list;
	}

}
