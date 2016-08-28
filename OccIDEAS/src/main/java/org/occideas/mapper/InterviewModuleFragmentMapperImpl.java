package org.occideas.mapper;

import java.util.ArrayList;
import java.util.List;

import org.occideas.entity.InterviewModuleFragment;
import org.occideas.vo.InterviewModuleFragmentVO;
import org.springframework.stereotype.Component;

@Component
public class InterviewModuleFragmentMapperImpl implements InterviewModuleFragmentMapper {

	@Override
	public InterviewModuleFragmentVO convertToVO(InterviewModuleFragment entity) {
		if(entity == null){
			return null;
		}
		InterviewModuleFragmentVO vo = new InterviewModuleFragmentVO();
		vo.setFragmentNodeName(entity.getFragmentNodeName());
		vo.setIdFragment(entity.getIdFragment());
		vo.setInterviewFragmentName(entity.getInterviewFragmentName());
		vo.setInterviewId(entity.getInterviewId());
		vo.setPrimaryKey(entity.getPrimaryKey());
		vo.setInterviewPrimaryKey(entity.getInterviewPrimaryKey());
		return vo;
	}

	@Override
	public List<InterviewModuleFragmentVO> convertToVOList(List<InterviewModuleFragment> entityList) {
		if(entityList == null){
			return null;
		}
		List<InterviewModuleFragmentVO> list = new ArrayList<>();
		for(InterviewModuleFragment entity:entityList){
			list.add(convertToVO(entity));
		}
		
		return list;
	}

}
