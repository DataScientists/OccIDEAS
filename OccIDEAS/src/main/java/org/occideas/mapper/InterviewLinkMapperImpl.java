package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewLinked;
import org.occideas.vo.InterviewLinkedVO;
import org.springframework.stereotype.Component;

@Component
public class InterviewLinkMapperImpl implements InterviewLinkMapper{

	@Override
	public InterviewLinkedVO convertToInterviewLinkVO(InterviewLinked entity) {
		if(entity == null){
			return null;
		}
		InterviewLinkedVO vo = new InterviewLinkedVO();
		vo.setDeleted(entity.getDeleted());
		vo.setDescription(entity.getDescription());
		vo.setIdInterview(entity.getIdInterview());
		vo.setLinkedId(entity.getLinkedId());
		vo.setName(entity.getName());
		vo.setParentQuestionId(entity.getParentQuestionId());
		return vo;
	}

	@Override
	public List<InterviewLinkedVO> convertToInterviewLinkVOList(List<InterviewLinked> entityList) {
		 if (entityList == null) {
	            return null;
	        }
	        List<InterviewLinkedVO> list = new ArrayList<InterviewLinkedVO>();
	        for (InterviewLinked entity : entityList) {
	            list.add(convertToInterviewLinkVO(entity));
	        }
	        return list;
	}

	@Override
	public InterviewLinked convertToInterviewLink(InterviewLinkedVO vo) {
		if(vo == null){
			return null;
		}
		InterviewLinked entity = new InterviewLinked();
		entity.setDeleted(vo.getDeleted());
		entity.setDescription(vo.getDescription());
		entity.setIdInterview(vo.getIdInterview());
		entity.setLinkedId(vo.getLinkedId());
		entity.setName(vo.getName());
		entity.setParentQuestionId(vo.getParentQuestionId());
		return entity;
	}

	@Override
	public List<InterviewLinked> convertToInterviewLinkedList(List<InterviewLinkedVO> voList) {
		 if (voList == null) {
	            return null;
	        }
	        List<InterviewLinked> list = new ArrayList<InterviewLinked>();
	        for (InterviewLinkedVO vo : voList) {
	            list.add(convertToInterviewLink(vo));
	        }
	        return list;
	}

}
